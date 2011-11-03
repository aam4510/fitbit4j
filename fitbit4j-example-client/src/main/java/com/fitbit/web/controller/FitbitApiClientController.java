package com.fitbit.web.controller;

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.FitbitApiError;
import com.fitbit.api.client.*;
import com.fitbit.api.client.http.PostParameter;
import com.fitbit.api.client.service.FitbitAPIClientService;
import com.fitbit.api.common.model.activities.Activities;
import com.fitbit.api.common.model.foods.*;
import com.fitbit.api.common.model.sleep.Sleep;
import com.fitbit.api.common.model.sleep.SleepLog;
import com.fitbit.api.common.model.units.UnitSystem;
import com.fitbit.api.common.model.user.UserInfo;
import com.fitbit.api.common.service.FitbitApiService;
import com.fitbit.api.model.*;
import com.fitbit.web.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class FitbitApiClientController implements InitializingBean {
    protected Log log = LogFactory.getLog(getClass());

    private static final int APP_USER_COOKIE_TTL = 60 * 60 * 24 * 7 * 4;
    private static final String APP_USER_COOKIE_NAME = "exampleClientUid";

    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String OAUTH_VERIFIER = "oauth_verifier";

    private static final String NOTIFICATION_UPDATES_SUBSCRIBER_ID = "1";

    @Resource
    private FitbitAPIEntityCache entityCache;
    @Resource
    private FitbitApiCredentialsCache credentialsCache;
    @Resource
    private FitbitApiSubscriptionStorage subscriptionStore;

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

    private FitbitAPIClientService<FitbitApiClientAgent> apiClientService;

    @Override
    public void afterPropertiesSet() throws Exception {
        apiClientService = new FitbitAPIClientService<FitbitApiClientAgent>(
                new FitbitApiClientAgent(getApiBaseUrl(), getFitbitSiteBaseUrl(), credentialsCache),
                clientConsumerKey,
                clientSecret,
                credentialsCache,
                entityCache,
                subscriptionStore
        );
    }

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
        try {
            request.setAttribute("subscriptions", apiClientService.getClient().getSubscriptions(context.getOurUser()));
        } catch (FitbitAPIException e) {
            log.error("Subscription error: " + e, e);
        }

        return "subscriptions";
    }

    @RequestMapping("/subscribe")
    public String showSubscribe(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
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
         try {
            request.setAttribute("subscriptions", apiClientService.getClient().getSubscriptions(context.getOurUser()));
        } catch (FitbitAPIException e) {
            log.error("Subscription error: " + e, e);
        }

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
         try {
            request.setAttribute("subscriptions", apiClientService.getClient().getSubscriptions(context.getOurUser()));
        } catch (FitbitAPIException e) {
            log.error("Subscription error: " + e, e);
        }

        return "subscriptions";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfileForm(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);

        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        try {
            apiClientService.getClient().getSubscriptions(context.getOurUser(), APICollectionType.activities);
        } catch (FitbitAPIException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        request.setAttribute("unitSystem", UnitSystem.getUnitSystem(Locale.US));

        try {
            UserInfo userInfo = context.getApiClientService().getClient().getUserInfo(context.getOurUser());
            request.setAttribute("userInfo", userInfo);
        } catch (FitbitAPIException e) {
            request.setAttribute("errors", Collections.singletonList(e.getMessage()));
            log.error(e);
        }

        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    protected String processProfileForm(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);

        String fullName = request.getParameter("fullName");
        String nickname = request.getParameter("nickname");
        String gender = request.getParameter("gender");
        String dateOfBirth = request.getParameter("dateOfBirth");
        String height = request.getParameter("height");
        String weight = request.getParameter("weight");
        String timezone = request.getParameter("timezone");
        List<String> messages = new ArrayList<String>();
        try {
            List<PostParameter> parameters = new ArrayList<PostParameter>();
            parameters.add(new PostParameter("fullname", fullName));
            parameters.add(new PostParameter("nickname", nickname));
            parameters.add(new PostParameter("gender", gender));
            parameters.add(new PostParameter("birthday", dateOfBirth));
            parameters.add(new PostParameter("height", height));
            parameters.add(new PostParameter("weight", weight));
            parameters.add(new PostParameter("timezone", timezone));

            UserInfo userInfo = context.getApiClientService().getClient().updateUserInfo(context.getOurUser(), parameters);
            String message = "Profile is successfully updated";
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error : e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error during updating profile.", e);
        }

        request.setAttribute("messages", messages);
        return showProfileForm(request, response);
    }

    @RequestMapping(value = "/water", method = RequestMethod.GET)
    public String showLogWater(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);

        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        List<String> messages = new ArrayList<String>();
        try {
            Water loggedWater = context.getApiClientService().getClient().getLoggedWater(context.getOurUser(), FitbitUser.CURRENT_AUTHORIZED_USER, context.getParsedLocalDate());
            request.setAttribute("water", loggedWater);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error : e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error during logging water.", e);
        }
        request.setAttribute("messages", messages);

        return "water";
    }

    @RequestMapping(value = "/water", method = RequestMethod.POST)
    public String showLogWaterPost(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);

        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        List<String> messages = new ArrayList<String>();
        try {
            String amountString = request.getParameter("amount");
            float amount = Float.parseFloat(amountString);
            context.getApiClientService().getClient().logWater(context.getOurUser(), amount, context.getParsedLocalDate());
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error : e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error during logging water.", e);
            request.setAttribute("messages", messages);
            return "water";
        } catch (NumberFormatException e) {
            messages.add(e.getMessage());
            log.error("Error during logging water.", e);
            request.setAttribute("messages", messages);
            return "water";
        }

        return "redirect:/app/water";
    }

    @RequestMapping(value = "/deleteWater", method = RequestMethod.GET)
    public String deleteLogWater(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);

        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        List<String> messages = new ArrayList<String>();
        try {
            String logId = request.getParameter("id");
            context.getApiClientService().getClient().deleteWater(context.getOurUser(), logId);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error : e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error during deleting water.", e);
            request.setAttribute("messages", messages);
            return "water";
        }

        return "redirect:/app/water";
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

        NutritionalValuesEntry nutritionalValuesEntry = new NutritionalValuesEntry();
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
        List<String> messages = new ArrayList<String>();
        try {
            Food food = context.getApiClientService().getClient().createFood(context.getOurUser(), name, description,
                    defaultFoodMeasurementUnitId, defaultServingSize, formType, nutritionalValuesEntry);
            String message = "Food created :: foodId = " + food.getFoodId() + ", name = " + food.getName();
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error : e.getApiErrors()) {
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

    @RequestMapping(value = "/invitations", method = RequestMethod.GET)
    public String showInvitationsPage(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }
        return "invitations";
    }

    @RequestMapping(value = "/sendInvitation", method = RequestMethod.POST)
    public String sendInvitation(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        String invitationMethod = request.getParameter("invitationMethod");
        String invitedUserIdOrEMail = request.getParameter("invitedUserIdOrEMail");

        log.info("Sending invitation :: invitationMethod = " + invitationMethod + ", invitedUserIdOrEMail = " + invitedUserIdOrEMail);
        List<String> messages = new ArrayList<String>();
        try {
            if (invitationMethod.equals("USER")) {
                context.getApiClientService().getClient().inviteByUserId(context.getOurUser(), invitedUserIdOrEMail);
            } else if (invitationMethod.equals("EMAIL")) {
                context.getApiClientService().getClient().inviteByEmail(context.getOurUser(), invitedUserIdOrEMail);
            } else {
                throw new IllegalArgumentException("Unknown invitation method.");
            }
            String message = "Invitation for invitedUserIdOrEMail =  " + invitedUserIdOrEMail + "successfully sent.";
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error : e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error during sending invitation.", e);
        }
        request.setAttribute("messages", messages);
        //return attributes back
        request.setAttribute("invitationMethod", invitationMethod);
        request.setAttribute("invitedUserIdOrEMail", invitedUserIdOrEMail);

        return "invitations";
    }

    @RequestMapping(value = "/acceptInvitation", method = RequestMethod.POST)
    public String acceptInvitation(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        String senderUserId = request.getParameter("senderUserId");

        log.info("Accept invitation :: senderUserId = " + senderUserId);
        List<String> messages = new ArrayList<String>();
        try {
            context.getApiClientService().getClient().acceptInvitationFromUser(context.getOurUser(), new FitbitUser(senderUserId));
            String message = "Invitation from senderUserId =  " + senderUserId + " successfully accepted. Congratulations - you are friends.";
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            if (e.getApiErrors() != null) {
                for (FitbitApiError error : e.getApiErrors()) {
                    messages.add(error.getMessage());
                }
            } else {
                messages.add(e.getMessage());
            }
            log.error("Error during accepting invitation.", e);
        }
        request.setAttribute("messages", messages);
        //return attributes back
        request.setAttribute("senderUserId", senderUserId);

        return "invitations";
    }

    @RequestMapping(value = "/sleep", method = RequestMethod.GET)
    public String showSleepPage(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }
        return "sleep";
    }

    @RequestMapping(value = "/getSleepLogs", method = RequestMethod.POST)
    public String getSleepLogs(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        String userId = request.getParameter("userId");
        String date = request.getParameter("date");

        log.info("Get sleep by date :: userId = " + userId + ", date = " + date);
        List<String> messages = new ArrayList<String>();
        Sleep sleep = null;
        try {
            sleep = context.getApiClientService().getClient().getSleep(context.getOurUser(), new FitbitUser(userId), FitbitApiService.getValidLocalDateOrNull(date));
            String message = "Sleep for userId =  '" + userId + "'  received.";
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            populateMessages(messages, e);
            log.error("Error during getting sleep.", e);
        }
        request.setAttribute("messages", messages);
        //return attributes back
        request.setAttribute("sleep", sleep);
        request.setAttribute("userId", userId);
        request.setAttribute("date", date);

        return "sleep";
    }

    @RequestMapping(value = "/logSleep", method = RequestMethod.POST)
    public String logSleep(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        String logDate = request.getParameter("logDate");
        String startTime = request.getParameter("startTime");
        String duration = request.getParameter("duration");

        log.info("Log sleep :: date =  " + logDate + ", startTime = " + startTime + ", duration = " + duration);
        List<String> messages = new ArrayList<String>();
        SleepLog sleepLog;
        try {
            sleepLog = context.getApiClientService().getClient().logSleep(
                    context.getOurUser(),
                    FitbitApiService.getValidLocalDateOrNull(logDate),
                    FitbitApiService.getValidTimeOrNull(startTime),
                    Long.valueOf(duration)
            );
            String message = "Log sleep added. logId =  " + sleepLog.getLogId() + ".";
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            populateMessages(messages, e);
            log.error("Error during log sleep.", e);
        }
        request.setAttribute("messages", messages);
        //return attributes back
        request.setAttribute("logDate", logDate);
        request.setAttribute("startTime", startTime);
        request.setAttribute("duration", duration);
        return "sleep";
    }

    @RequestMapping(value = "/deleteSleepLog", method = RequestMethod.POST)
    public String deleteSleepLog(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext();
        populate(context, request, response);
        if (!isAuthorized(context, request)) {
            showAuthorize(request, response);
        }

        String sleepLogId = request.getParameter("sleepLogId");

        log.info("Delete sleep log :: sleepLogId =  " + sleepLogId);
        List<String> messages = new ArrayList<String>();
        try {
            context.getApiClientService().getClient().deleteSleepLog(context.getOurUser(), Long.valueOf(sleepLogId));
            String message = "Sleep log was deleted. sleepLogId = " + sleepLogId;
            messages.add(message);
            log.info(message);
        } catch (FitbitAPIException e) {
            populateMessages(messages, e);
            log.error("Error during delete sleep log.", e);
        }
        request.setAttribute("messages", messages);
        //return attributes back
        request.setAttribute("sleepLogId", sleepLogId);
        return "sleep";
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
                ApiRateLimitStatus clientAndViewerRateLimitStatus = context.getApiClientService().getClientAndViewerRateLimitStatus(context.getOurUser());
                request.setAttribute("clientAndViewerRateLimitStatus", clientAndViewerRateLimitStatus);
            } catch (FitbitAPIException e) {
                errors.add(e.getMessage());
                log.error(e);
            }
        }

        try {
            ApiRateLimitStatus clientRateLimitStatus = context.getApiClientService().getClientRateLimitStatus();
            request.setAttribute("clientRateLimitStatus", clientRateLimitStatus);
        } catch (FitbitAPIException e) {
            errors.add(e.getMessage());
            log.error(e);
        }

        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
        }


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
        context.setApiClientService(apiClientService);

        context.setOurUser(getOrMakeExampleAppUser(request, response));

        APIResourceCredentials resourceCredentials = context.getApiClientService().getResourceCredentialsByUser(context.getOurUser());
        boolean isAuthorized = resourceCredentials != null && resourceCredentials.isAuthorized();
        boolean isSubscribed = false;
        if (isAuthorized) {
            List<ApiSubscription> subscriptions = Collections.emptyList();
            try {
                subscriptions = apiClientService.getClient().getSubscriptions(context.getOurUser());
            } catch (FitbitAPIException e) {
                log.error("Subscription error: " + e, e);
            }
            if (null != context.getOurUser() && subscriptions.size() > 0) {
                isSubscribed = true;
            }
        }
        request.setAttribute("actionBean", context);
        request.setAttribute("isSubscribed", isSubscribed);
        request.setAttribute("exampleBaseUrl", getExampleBaseUrl());
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
            uidCookie.setPath("/");
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

    private void populateMessages(List<String> messages, FitbitAPIException e) {
        if (e.getApiErrors() != null) {
            for (FitbitApiError error : e.getApiErrors()) {
                messages.add(error.getMessage());
            }
        } else {
            messages.add(e.getMessage());
        }
    }
}
