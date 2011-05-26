package com.fitbit.web.controller;

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.FitbitApiError;
import com.fitbit.api.client.*;
import com.fitbit.api.client.service.FitbitAPIClientService;
import com.fitbit.api.common.model.activities.Activities;
import com.fitbit.api.common.model.foods.Food;
import com.fitbit.api.common.model.foods.FoodFormType;
import com.fitbit.api.common.model.foods.Foods;
import com.fitbit.api.common.model.foods.NutritionalValuesEntry;
import com.fitbit.api.common.model.units.UnitSystem;
import com.fitbit.api.common.model.user.Account;
import com.fitbit.api.model.APICollectionType;
import com.fitbit.api.model.APIResourceCredentials;
import com.fitbit.api.model.ApiRateLimitStatus;
import com.fitbit.web.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class FitbitApiClientController {
    protected Log log = LogFactory.getLog(getClass());

    private static final int APP_USER_COOKIE_TTL = 60 * 60 * 24 * 7 * 4;
    private static final String APP_USER_COOKIE_NAME = "exampleClientUid";

    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String OAUTH_VERIFIER = "oauth_verifier";

    private static final String NOTIFICATION_UPDATES_SUBSCRIBER_ID = "1";

    private static final FitbitAPIEntityCache entityCache = new FitbitApiEntityCacheMapImpl();
    private static final FitbitApiCredentialsCache credentialsCache = new FitbitApiCredentialsCacheMapImpl();
    private static final FitbitApiSubscriptionStorage subscriptionStore = new FitbitApiSubscriptionStorageInMemoryImpl();

    @Value("#{config['fitbitSiteBaseUrl']}")
    private String fitbitSiteBaseUrl;
    @Value("#{config['apiBaseUrl']}")
    private String apiBaseUrl;
    @Value("#{config['exampleBaseUrl']}")
    private String exampleBaseUrl;

    @Value("#{config['clientConsumerKey']}")
    private String clientConsumerKey;
    @Value("#{config['clientSecret']}")
    private String clientSecret;

    @Value("#{config['showAccountRegistrationForm']}")
    private Boolean showAccountRegistrationForm;

    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        // If the user does not have token credentials, simply show the page with no data:
        RequestContext context = new RequestContext();
        populate(context, request, response);

        showHome(context, request, response);
        return "index";
    }

    @RequestMapping("/authorize")
    public String showAuthorize(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        try {
            // Redirect to page where user can authorize the application:
            return "redirect:" + context.getApiClientService().getResourceOwnerAuthorizationURL(context.getOurUser(), getExampleBaseUrl() + "/completeAuthorization");
        } catch (FitbitAPIException e) {
            log.error(e);
            request.setAttribute("errors", Collections.singletonList(e.getMessage()));
            return index(request, response);
        }
    }

    @RequestMapping("/completeAuthorization")
    public String showCompleteAuthorization(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        String tempTokenReceived = request.getParameter(OAUTH_TOKEN);
        String tempTokenVerifier = request.getParameter(OAUTH_VERIFIER);
        APIResourceCredentials resourceCredentials = context.getApiClientService().getResourceCredentialsByTempToken(tempTokenReceived);

        if (resourceCredentials == null) {
            log.error("Unrecognized temporary token when attempting to complete authorization: " + tempTokenReceived);
            request.setAttribute("errors", "Unrecognized temporary token when attempting to complete authorization: " + tempTokenReceived);
        } else {
            // Get token credentials only if necessary:
            if (!resourceCredentials.isAuthorized()) {
                // The verifier is required in the request to get token credentials:
                resourceCredentials.setTempTokenVerifier(tempTokenVerifier);
                try {
                    // Get token credentials for user:
                    context.getApiClientService().getTokenCredentials(new LocalUserDetail(resourceCredentials.getLocalUserId()));
                } catch (FitbitAPIException e) {
                    log.error("Unable to finish authorization with Fitbit.", e);
                    request.setAttribute("errors", Collections.singletonList(e.getMessage()));
                }
            }
        }

        showHome(context, request, response);
        return "index";
    }

    @RequestMapping("/expireResourceCredentials")
    protected String showExpireResourceCredentials(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        context.getApiClientService().expireResourceCredentials(context.getOurUser());
        return "redirect:/";
    }

    @RequestMapping("/allSubscriptions")
    public String showAllSubscriptions(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }
        request.setAttribute("subscriptions", subscriptionStore.getAllSubscriptions());

        return "subscriptions";
    }

    @RequestMapping("/subscribe")
    public String showSubscribe(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize( request, response);
        }

        try {
            context.getApiClientService().subscribe(
                    NOTIFICATION_UPDATES_SUBSCRIBER_ID,
                    context.getOurUser(),
                    APICollectionType.activities, getActivitiesSubscriptionId(context));
            context.getApiClientService().subscribe(
                    NOTIFICATION_UPDATES_SUBSCRIBER_ID,
                    context.getOurUser(),
                    APICollectionType.foods, getFoodSubscriptionId(context));
            populate(context, request, response);
            request.setAttribute("message", "You successfully subscribed to your Fitbit activity stream.");
        } catch (FitbitAPIException e) {
            request.setAttribute("errors", Collections.singletonList(e.getMessage()));
            log.error("Unable to subscribe: " + e, e);
        }
        request.setAttribute("subscriptions", subscriptionStore.getAllSubscriptions());

        return "subscriptions";
    }

    @RequestMapping("/unsubscribe")
    protected String showUnsubscribe(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        try {
            context.getApiClientService().unsubscribe(
                    NOTIFICATION_UPDATES_SUBSCRIBER_ID,
                    context.getOurUser(),
                    APICollectionType.activities, getActivitiesSubscriptionId(context));
            subscriptionStore.delete(subscriptionStore.getBySubscriptionId(getActivitiesSubscriptionId(context)));

            context.getApiClientService().unsubscribe(
                    NOTIFICATION_UPDATES_SUBSCRIBER_ID,
                    context.getOurUser(),
                    APICollectionType.foods, getFoodSubscriptionId(context));
            subscriptionStore.delete(subscriptionStore.getBySubscriptionId(getFoodSubscriptionId(context)));

            populate(context, request, response);
            request.setAttribute("message", "You successfully unsubscribed from your Fitbit activity stream.");
        } catch (FitbitAPIException e) {
            request.setAttribute("errors", Collections.singletonList(e.getMessage()));
            log.error("Unable to unsubscribe: " + e, e);
        }
        request.setAttribute("subscriptions", subscriptionStore.getAllSubscriptions());

        return "subscriptions";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);

        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    protected String processRegistrationForm(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String timezone = request.getParameter("timezone");
        String emailSubscribe = request.getParameter("emailSubscribe");
        log.info("Creating new account :: email = " + email + ", password = " + password + ", timezone = " + timezone +
                ", emailSubscribe = " + (emailSubscribe != null));
        List<String> messages  =  new ArrayList<String>();
        try {
            Account account = context.getApiClientService().getClient().registerAccount(email, password, timezone, emailSubscribe != null);
            String message = "Account registered :: encodedId = " + account.getEncodedId() + ", profileUpdateUuid = " + account.getProfileUpdateUuid();
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error: e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error registering new account.", e);
        }
        request.setAttribute("messages", messages);
        //return attributes back
        request.setAttribute("email", email);
        request.setAttribute("password", password);
        request.setAttribute("timezone", timezone);
        request.setAttribute("emailSubscribe", emailSubscribe != null);

        return "register";
    }

    @RequestMapping(value = "/createFoodForm", method = RequestMethod.GET)
    public String showCreateFoodForm(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }
        return "createFood";
    }

    @RequestMapping(value = "/createFood", method = RequestMethod.POST)
    protected String createFood(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Long defaultFoodMeasurementUnitId = getParameterAsLong(request, "defaultFoodMeasurementUnitId", (long) 0);
        Float defaultServingSize = getParameterAsFloat(request, "defaultServingSize", 0f);
        FoodFormType formType = FoodFormType.valueOf(request.getParameter("formType"));

        int caloriesPerServingSize = getParameterAsInteger(request, "caloriesPerServingSize", 0);
        int caloriesFromFat = getParameterAsInteger(request, "caloriesFromFat", 0);
        float totalFat = getParameterAsFloat(request, "totalFat", 0f);
        float transFat = getParameterAsFloat(request, "transFat", 0f);
        float saturatedFat = getParameterAsFloat(request, "saturatedFat", 0f);
        float cholesterol = getParameterAsFloat(request, "cholesterol", 0f);
        float sodium = getParameterAsFloat(request, "sodium", 0f);
        float potassium = getParameterAsFloat(request, "potassium", 0f);
        float totalCarbohydrate = getParameterAsFloat(request, "totalCarbohydrate", 0f);
        float dietaryFiber = getParameterAsFloat(request, "dietaryFiber", 0f);
        float sugars = getParameterAsFloat(request, "sugars", 0f);
        float protein = getParameterAsFloat(request, "protein", 0f);
        float vitaminA = getParameterAsFloat(request, "vitaminA", 0f);
        float vitaminC = getParameterAsFloat(request, "vitaminC", 0f);
        float iron = getParameterAsFloat(request, "iron", 0f);
        float calcium = getParameterAsFloat(request, "calcium", 0f);
        float thiamin = getParameterAsFloat(request, "thiamin", 0f);
        float riboflavin = getParameterAsFloat(request, "riboflavin", 0f);
        float vitaminB6 = getParameterAsFloat(request, "vitaminB6", 0f);
        float vitaminB12 = getParameterAsFloat(request, "vitaminB12", 0f);
        float vitaminE = getParameterAsFloat(request, "vitaminE", 0f);
        float folicAcid = getParameterAsFloat(request, "folicAcid", 0f);
        float niacin = getParameterAsFloat(request, "niacin", 0f);
        float magnesium = getParameterAsFloat(request, "magnesium", 0f);
        float phosphorus = getParameterAsFloat(request, "phosphorus", 0f);
        float iodine = getParameterAsFloat(request, "iodine", 0f);
        float zinc = getParameterAsFloat(request, "zinc", 0f);
        float copper = getParameterAsFloat(request, "copper", 0f);
        float biotin = getParameterAsFloat(request, "biotin", 0f);
        float pantothenicAcid = getParameterAsFloat(request, "pantothenicAcid", 0f);
        float vitaminD = getParameterAsFloat(request, "vitaminD", 0f);

        NutritionalValuesEntry nutritionalValuesEntry =  new NutritionalValuesEntry();
        nutritionalValuesEntry.setCalories(caloriesPerServingSize);
        nutritionalValuesEntry.setCaloriesFromFat(caloriesFromFat);
        nutritionalValuesEntry.setTotalFat(totalFat);
        nutritionalValuesEntry.setTransFat(transFat);
        nutritionalValuesEntry.setSaturatedFat(saturatedFat);
        nutritionalValuesEntry.setCholesterol(cholesterol);
        nutritionalValuesEntry.setSodium(sodium);
        nutritionalValuesEntry.setPotassium(potassium);
        nutritionalValuesEntry.setTotalCarbohydrate(totalCarbohydrate);
        nutritionalValuesEntry.setDietaryFiber(dietaryFiber);
        nutritionalValuesEntry.setSugars(sugars);
        nutritionalValuesEntry.setProtein(protein);
        nutritionalValuesEntry.setVitaminA(vitaminA);
        nutritionalValuesEntry.setVitaminC(vitaminC);
        nutritionalValuesEntry.setIron(iron);
        nutritionalValuesEntry.setCalcium(calcium);
        nutritionalValuesEntry.setVitaminB6(vitaminB6);
        nutritionalValuesEntry.setVitaminB12(vitaminB12);
        nutritionalValuesEntry.setThiamin(thiamin);
        nutritionalValuesEntry.setRiboflavin(riboflavin);
        nutritionalValuesEntry.setVitaminE(vitaminE);
        nutritionalValuesEntry.setFolicAcid(folicAcid);
        nutritionalValuesEntry.setNiacin(niacin);
        nutritionalValuesEntry.setMagnesium(magnesium);
        nutritionalValuesEntry.setPhosphorus(phosphorus);
        nutritionalValuesEntry.setIodine(iodine);
        nutritionalValuesEntry.setZinc(zinc);
        nutritionalValuesEntry.setCopper(copper);
        nutritionalValuesEntry.setBiotin(biotin);
        nutritionalValuesEntry.setPantothenicAcid(pantothenicAcid);
        nutritionalValuesEntry.setVitaminD(vitaminD);

        log.info("Creating new food :: name = " + name + ", description = " + description + ", defaultFoodMeasurementUnitId = " + defaultFoodMeasurementUnitId +
               ", defaultServingSize" + defaultServingSize + ", caloriesPerServingSize" + caloriesPerServingSize + ", formType = " + formType);
        List<String> messages  =  new ArrayList<String>();
        try {
            Food food = context.getApiClientService().getClient().createFood(context.getOurUser(), name, description,
                    defaultFoodMeasurementUnitId, defaultServingSize, formType, nutritionalValuesEntry);
            String message = "Food created :: foodId = " + food.getFoodId() + ", name = " + food.getName();
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error: e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error creating new food.", e);
        }
        request.setAttribute("messages", messages);
        //return attributes back
        request.setAttribute("name", name);
        request.setAttribute("description", description);
        request.setAttribute("defaultFoodMeasurementUnitId", defaultFoodMeasurementUnitId);
        request.setAttribute("defaultServingSize", defaultServingSize);
        request.setAttribute("caloriesPerServingSize", caloriesPerServingSize);
        request.setAttribute("formType", formType);

        return "createFood";
    }

    protected void showHome(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
        List<String> errors = new ArrayList<String>();
        if (isAuthorized(context, request)) {
            // User has token credentials. Use them to get and display user's activities and foods:
            try {
                Activities activities = context.getApiClientService().getActivities(context.getOurUser(), context.getParsedLocalDate());
                request.setAttribute("activities", activities);
            } catch (FitbitAPIException e) {
                errors.add(e.getMessage());
                log.error(e);
            }
            try {
                Foods foods = context.getApiClientService().getFoods(context.getOurUser(), context.getParsedLocalDate());
                request.setAttribute("foods", foods);
            } catch (FitbitAPIException e) {
                errors.add(e.getMessage());
                log.error(e);
            }
            try {
                ApiRateLimitStatus clientAndUserRateLimitStatus = context.getApiClientService().getClientAndUserRateLimitStatus(context.getOurUser());
                request.setAttribute("clientAndUserRateLimitStatus", clientAndUserRateLimitStatus);
            } catch (FitbitAPIException e) {
                errors.add(e.getMessage());
                log.error(e);
            }
        }

        try {
            ApiRateLimitStatus ipRateLimitStatus = context.getApiClientService().getIpRateLimitStatus();
            request.setAttribute("ipRateLimitStatus", ipRateLimitStatus);
        } catch (FitbitAPIException e) {
            errors.add(e.getMessage());
            log.error(e);
        }

        request.setAttribute("errors", errors);

        request.setAttribute("unitSystem", UnitSystem.getUnitSystem(Locale.US));
    }

    protected boolean isAuthorized(RequestContext context, HttpServletRequest request) {
        // Get cached resource credentials:
        APIResourceCredentials resourceCredentials = context.getApiClientService().getResourceCredentialsByUser(context.getOurUser());
        boolean isAuthorized = resourceCredentials != null && resourceCredentials.isAuthorized();
        request.setAttribute("isAuthorized", isAuthorized);
        if (resourceCredentials != null) {
            request.setAttribute("encodedUserId", resourceCredentials.getResourceId());
            request.setAttribute("userProfileURL", getFitbitSiteBaseUrl() + "/user/" + resourceCredentials.getResourceId());
        }
        return isAuthorized;
    }

    public void populate(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
        context.setApiClientService(
                new FitbitAPIClientService<FitbitApiClientAgent>(
                        new FitbitApiClientAgent(getApiBaseUrl(), getFitbitSiteBaseUrl(), credentialsCache),
                        clientConsumerKey,
                        clientSecret,
                        credentialsCache,
                        entityCache,
                        subscriptionStore
                ));

        context.setOurUser(getOrMakeExampleAppUser(request, response));
        boolean isSubscribed = false;
        if (null != context.getOurUser() && null != subscriptionStore.getBySubscriptionId(getActivitiesSubscriptionId(context))) {
            isSubscribed = true;
        }
        request.setAttribute("actionBean", context);
        request.setAttribute("isSubscribed", isSubscribed);
        request.setAttribute("exampleBaseUrl", getExampleBaseUrl());
        request.setAttribute("showAccountRegistrationForm", showAccountRegistrationForm);
    }

    protected LocalUserDetail getOrMakeExampleAppUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = null;

        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(APP_USER_COOKIE_NAME)) {
                    userId = cookie.getValue();
                    log.info("Returning user " + userId);
                    break;
                }
            }
        }
        if (null == userId || userId.length() < 1) {
            userId = String.valueOf(Math.abs(new Random(System.currentTimeMillis()).nextInt()));
            Cookie uidCookie = new Cookie(APP_USER_COOKIE_NAME, userId);
            uidCookie.setPath(request.getContextPath());
            uidCookie.setMaxAge(APP_USER_COOKIE_TTL);
            response.addCookie(uidCookie);
            log.info("Created new user " + userId);
        }

        return new LocalUserDetail(userId);
    }

    private String getFoodSubscriptionId(RequestContext context) {
        return context.getOurUser().getUserId() + ":1";
    }

    private String getActivitiesSubscriptionId(RequestContext context) {
        return context.getOurUser().getUserId() + ":0";
    }

    public String getFitbitSiteBaseUrl() {
        return fitbitSiteBaseUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getExampleBaseUrl() {
        return exampleBaseUrl;
    }

    public String getClientConsumerKey() {
        return clientConsumerKey;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    private Integer getParameterAsInteger(HttpServletRequest request, String parameterName, Integer defaultValue) {
        String parameterValue = request.getParameter(parameterName);
        return StringUtils.isEmpty(parameterValue) ? defaultValue : Integer.valueOf(parameterValue);
    }

    private Long getParameterAsLong(HttpServletRequest request, String parameterName, Long defaultValue) {
        String parameterValue = request.getParameter(parameterName);
        return StringUtils.isEmpty(parameterValue) ? defaultValue : Long.valueOf(parameterValue);
    }

    private Float getParameterAsFloat(HttpServletRequest request, String parameterName, Float defaultValue) {
        String parameterValue = request.getParameter(parameterName);
        return StringUtils.isEmpty(parameterValue) ? defaultValue : Float.valueOf(parameterValue);
    }
}
