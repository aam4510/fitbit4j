package com.fitbit.web.controller;

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.client.*;
import com.fitbit.api.client.service.FitbitAPIClientService;
import com.fitbit.api.common.model.activities.Activities;
import com.fitbit.api.common.model.foods.Foods;
import com.fitbit.api.common.model.units.UnitSystem;
import com.fitbit.api.model.APICollectionType;
import com.fitbit.api.model.APIResourceCredentials;
import com.fitbit.api.model.ApiRateLimitStatus;
import com.fitbit.web.context.RequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

public class FitbitClientExampleAppServlet extends HttpServlet {
    protected Log log = LogFactory.getLog(getClass());

    private static final int APP_USER_COOKIE_TTL = 60 * 60 * 24 * 7 * 4;
    private static final String APP_USER_COOKIE_NAME = "exampleClientUid";

    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String OAUTH_VERIFIER = "oauth_verifier";

    private static final String NOTIFICATION_UPDATES_SUBSCRIBER_ID = "1";

    private static final FitbitAPIEntityCache entityCache = new FitbitApiEntityCacheMapImpl();
    private static final FitbitApiCredentialsCache credentialsCache = new FitbitApiCredentialsCacheMapImpl();
    private static final FitbitApiSubscriptionStorage subscriptionStore = new FitbitApiSubscriptionStorageInMemoryImpl();

    private String fitbitSiteBaseUrl;
    private String apiBaseUrl;
    private String exampleBaseUrl;

    private String clientConsumerKey;
    private String clientSecret;

    public void init() throws ServletException {
        Properties configProperties = new Properties();
        try {
            configProperties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            fitbitSiteBaseUrl = configProperties.getProperty("fitbitSiteBaseUrl");
            apiBaseUrl = configProperties.getProperty("apiBaseUrl");
            exampleBaseUrl = configProperties.getProperty("exampleBaseUrl");
            clientConsumerKey = configProperties.getProperty("clientConsumerKey");
            clientSecret = configProperties.getProperty("clientSecret");
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // If the user does not have token credentials, simply show the page with no data:
        RequestContext context = new RequestContext();

        populate(context, request, response);
        if (request.getParameter("authorize") != null) {
            showAuthorize(context, request, response);
        } else if (request.getParameter("completeAuthorization") != null) {
            showCompleteAuthorization(context, request, response);
        } else if (request.getParameter("expireResourceCredentials") != null) {
            showExpireResourceCredentials(context, request, response);
        } else if (request.getParameter("subscribe") != null) {
            showSubscribe(context, request, response);
        } else if (request.getParameter("unsubscribe") != null) {
            showUnsubscribe(context, request, response);
        } else if (request.getParameter("allSubscriptions") != null) {
            showAllSubscriptions(context, request, response);
        } else {
            showHome(context, request, response);
        }
    }

    protected void showHome(RequestContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (isAuthorized(context, request)) {
            // User has token credentials. Use them to get and display user's activities and foods:
            try {
                Activities activities = context.getApiClientService().getActivities(context.getOurUser(), context.getParsedLocalDate());
                request.setAttribute("activities", activities);
            } catch (FitbitAPIException e) {
//                beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiErrorRetrievingResource", "activities", e.getMessage()));
                log.error(e);
            }
            try {
                Foods foods = context.getApiClientService().getFoods(context.getOurUser(), context.getParsedLocalDate());
                request.setAttribute("foods", foods);
            } catch (FitbitAPIException e) {
//                beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiErrorRetrievingResource", "foods", e.getMessage()));
                log.error(e);
            }
            try {
                ApiRateLimitStatus clientAndUserRateLimitStatus = context.getApiClientService().getClientAndUserRateLimitStatus(context.getOurUser());
                request.setAttribute("clientAndUserRateLimitStatus", clientAndUserRateLimitStatus);
            } catch (FitbitAPIException e) {
//                beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiErrorRetrievingResource", "retrieving the client+owner rate status", e.getMessage()));
                log.error(e);
            }
        }

        try {
            ApiRateLimitStatus ipRateLimitStatus = context.getApiClientService().getIpRateLimitStatus();
            request.setAttribute("ipRateLimitStatus", ipRateLimitStatus);
        } catch (FitbitAPIException e) {
//            beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiError", "retrieving IP-based rate status", e.getMessage()));
            log.error(e);
        }

        request.setAttribute("unitSystem", UnitSystem.getUnitSystem(Locale.US));

        request.getRequestDispatcher("/fitbitClientExampleApp.jsp").forward(request, response);
    }

    protected void showAuthorize(RequestContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Redirect to page where user can authorize the application:
            response.sendRedirect(context.getApiClientService().getResourceOwnerAuthorizationURL(context.getOurUser(), getExampleBaseURL() + "?completeAuthorization="));
        } catch (FitbitAPIException e) {
            log.error(e);
//            beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiError", "getting temporary credentials from Fitbit", e.getMessage()));
            request.getRequestDispatcher("/fitbitClientExampleApp.jsp").forward(request, response);
        }
    }

    protected void showCompleteAuthorization(RequestContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tempTokenReceived = request.getParameter(OAUTH_TOKEN);
        String tempTokenVerifier = request.getParameter(OAUTH_VERIFIER);
        APIResourceCredentials resourceCredentials = context.getApiClientService().getResourceCredentialsByTempToken(tempTokenReceived);

        if (resourceCredentials == null) {
            log.error("Unrecognized temporary token when attempting to complete authorization: " + tempTokenReceived);
//            beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.unrecognizedTemporaryToken", tempTokenReceived));
//            return new SmartResolution();
            throw new RuntimeException();
        }

        // Get token credentials only if necessary:
        if (!resourceCredentials.isAuthorized()) {
            // The verifier is required in the request to get token credentials:
            resourceCredentials.setTempTokenVerifier(tempTokenVerifier);
            try {
                // Get token credentials for user:
                context.getApiClientService().getTokenCredentials(new LocalUserDetail(resourceCredentials.getLocalUserId()));
            } catch (FitbitAPIException e) {
                log.error("Unable to finish authorization with Fitbit.", e);
//                beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiError", "getting token credentials from Fitbit", e.getMessage()));
//                return new SmartResolution();
            }
        }

        showHome(context, request, response);
    }

    protected void showExpireResourceCredentials(RequestContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        context.getApiClientService().expireResourceCredentials(context.getOurUser());
        showHome(context, request, response);
    }

    protected void showSubscribe(RequestContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!isAuthorized(context, request)) {
            showAuthorize(context, request, response);
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
//            beanContext.getMessages().add(new SimpleMessage("You successfully subscribed to your Fitbit activity stream."));
            populate(context, request, response);
            showAllSubscriptions(context, request, response);
        } catch (FitbitAPIException e) {
//            beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiError", "subscribing", e.getMessage()));
            log.error("Unable to subscribe: " + e, e);
        }
    }

    protected void showUnsubscribe(RequestContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!isAuthorized(context, request)) {
            showAuthorize(context, request, response);
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

//            beanContext.getMessages().add(new SimpleMessage("You successfully unsubscribed from your Fitbit activity stream."));
            populate(context, request, response);
            showAllSubscriptions(context, request, response);
        } catch (FitbitAPIException e) {
//            beanContext.getValidationErrors().addGlobalError(new LocalizableError("web.api.client.sampleApp.apiError", "unsubscribing", e.getMessage()));
            log.error("Unable to unsubscribe: " + e, e);
        }
    }

    protected void showAllSubscriptions(RequestContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("showSubscriptions", true);
        request.setAttribute("subscriptions", subscriptionStore.getAllSubscriptions());
        showHome(context, request, response);
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
        request.setAttribute("exampleBaseUrl", getExampleBaseURL());
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

    protected String getFitbitSiteBaseUrl() {
        return fitbitSiteBaseUrl;
    }

    protected String getApiBaseUrl() {
        return apiBaseUrl;
    }

    protected String getExampleBaseURL() {
        return exampleBaseUrl;
    }
}
