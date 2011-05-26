package com.fitbit.api.client;

import com.fitbit.api.APIUtil;
import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.client.http.AccessToken;
import com.fitbit.api.client.http.HttpClient;
import com.fitbit.api.client.http.PostParameter;
import com.fitbit.api.client.http.Response;
import com.fitbit.api.client.http.TempCredentials;
import com.fitbit.api.common.model.activities.Activities;
import com.fitbit.api.common.model.activities.Activity;
import com.fitbit.api.common.model.activities.ActivityReference;
import com.fitbit.api.common.model.activities.LoggedActivityReference;
import com.fitbit.api.common.model.body.Body;
import com.fitbit.api.common.model.devices.Device;
import com.fitbit.api.common.model.devices.DeviceType;
import com.fitbit.api.common.model.foods.*;
import com.fitbit.api.common.model.timeseries.Data;
import com.fitbit.api.common.model.timeseries.TimePeriod;
import com.fitbit.api.common.model.timeseries.TimeSeriesResourceType;
import com.fitbit.api.common.model.user.Account;
import com.fitbit.api.common.model.user.UserInfo;
import com.fitbit.api.common.service.FitbitApiService;
import com.fitbit.api.model.*;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * User: gkutlu
 * Date: Feb 17, 2010
 * Time: 7:26:39 PM
 */
@SuppressWarnings({"NonPrivateFieldAccessedInSynchronizedContext"})
public class FitbitApiClientAgent extends FitbitAPIClientSupport implements Serializable {
	private static final FitbitApiCredentialsCache DEFAULT_CREDENTIALS_CACHE = new FitbitApiCredentialsCacheMapImpl();

    private static final String DEFAULT_API_BASE_URL = "api.fitbit.com";
    private static final String DEFAULT_WEB_BASE_URL = "http://www.fitbit.com";
    private static final long serialVersionUID = -1486360080128882436L;
    protected static final String SUBSCRIBER_ID_HEADER_NAME = "X-Fitbit-Subscriber-Id";

    private SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
    private String apiBaseUrl = DEFAULT_API_BASE_URL;
    private APIVersion apiVersion = APIVersion.BETA_1;

    private FitbitApiCredentialsCache credentialsCache;

    public FitbitApiClientAgent() {
        this(DEFAULT_API_BASE_URL, DEFAULT_WEB_BASE_URL, (FitbitApiCredentialsCache) null);
    }

    public FitbitApiClientAgent(String apiBaseUrl, String webBaseUrl, FitbitApiCredentialsCache credentialsCache) {
        this("https://" + apiBaseUrl + "/oauth/request_token", webBaseUrl + "/oauth/authorize", "https://" + apiBaseUrl + "/oauth/access_token");
        this.apiBaseUrl = apiBaseUrl;
        if (null==credentialsCache) {
        	this.credentialsCache = DEFAULT_CREDENTIALS_CACHE;
        } else {
        	this.credentialsCache = credentialsCache;
        }
    }

    public FitbitApiClientAgent(String requestTokenURL, String authorizationURL, String accessTokenURL) {
        super();
        init(requestTokenURL, authorizationURL, accessTokenURL);
    }

    private void init(String requestTokenURL, String authorizationURL, String accessTokenURL) {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        http.setRequestTokenURL(requestTokenURL);
        http.setAuthorizationURL(authorizationURL);
        http.setAccessTokenURL(accessTokenURL);
    }

    /**
     * Returns the base URL
     *
     * @return the base URL
     */
    public String getApiBaseUrl() {
        return "http://" + apiBaseUrl;
    }

    /**
     * Returns the base secured URL
     *
     * @return the secured base URL
     */
    public String getApiBaseSecuredUrl() {
        return "https://" + apiBaseUrl;
    }

    public APIVersion getApiVersion() {
    	return apiVersion;
    }

    /**
     * Retrieves a request token
     *
     * @return retrieved request token@since Fitbit 1.0
     * @throws FitbitAPIException when Fitbit API service or network is unavailable
     * @see <a href="https://berryfit.pbworks.com//OAuth-FAQ">Fitbit API Wiki - OAuth FAQ</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Core 1.0 - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @since Fitbit 1.0
     */
    public TempCredentials getOAuthTempToken() throws FitbitAPIException {
        return http.getOAuthRequestToken();
    }

    public TempCredentials getOAuthTempToken(String callback_url) throws FitbitAPIException {
        return http.getOauthRequestToken(callback_url);
    }

    /**
     * Retrieves an access token assosiated with the supplied request token.
     * @param tempToken the request token
     * @return access token associated with the supplied request token.
     * @throws FitbitAPIException when Fitbit service or network is unavailable, or the user has not authorized
     * @see <a href="http://wiki.fitbit.com/OAuth-Authentication-API">Fitbit API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Fitbit API 1.0
     */
    public synchronized AccessToken getOAuthAccessToken(TempCredentials tempToken) throws FitbitAPIException {
        return http.getOAuthAccessToken(tempToken);
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param tempToken the request token
     * @param pin pin
     * @return access token associsted with the supplied request token.
     * @throws FitbitAPIException when Fitbit service or network is unavailable, or the user has not authorized
     * @see <a href="http://wiki.fitbit.com/OAuth-Authenticaion-API">Fitbit API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Fitbit 2.0.8
     */
    public synchronized AccessToken getOAuthAccessToken(TempCredentials tempToken, String pin) throws FitbitAPIException {
        AccessToken accessToken = http.getOAuthAccessToken(tempToken, pin);
        setUserId(accessToken.getEncodedUserId());
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token.
     * @param token request token
     * @param tokenSecret request token secret
     * @param oauth_verifier oauth_verifier or pin
     * @return access token associsted with the supplied request token.
     * @throws FitbitAPIException when Fitbit service or network is unavailable, or the user has not authorized
     * @see <a href="http://wiki.fitbit.com/OAuth-Authenticaion-API">Fitbit API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Fitbit 2.0.8
     */
    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret, String oauth_verifier) throws FitbitAPIException {
        return http.getOAuthAccessToken(token, tokenSecret, oauth_verifier);
    }

    /**
     * Sets the access token
     * @param accessToken accessToken
     * @since Fitbit 1.0
     */
    public void setOAuthAccessToken(AccessToken accessToken){
        http.setOAuthAccessToken(accessToken);
    }

    /**
     * Sets the access token
     * @param token token
     * @param tokenSecret token secret
     * @since Fitbit 1.0
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }

    public void setOAuthAccessToken(String token, String tokenSecret, String encodedUserId) {
        setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }

    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        http.setOAuthConsumer(consumerKey, consumerSecret);
    }

    protected void setSubscriberId(String subscriberId) {
    	if (null!=subscriberId) {
    		http.setRequestHeader(SUBSCRIBER_ID_HEADER_NAME, subscriberId);
    	}
    }

    public FitbitApiCredentialsCache getCredentialsCache() {
        return credentialsCache;
    }

    public Activities getActivities(LocalUserDetail localUser, FitbitUser fitbitUser, LocalDate date) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/activities/date/2010-02-25.json
        // /1/user/228TQ4/activities/date/2010-02-25.xml
        Response res = getCollectionResponseForDate(localUser, fitbitUser, APICollectionType.activities, date);
        throwExceptionIfError(res);
        return Activities.constructActivities(res);
    }

    public List<ActivityReference> getFavoriteActivities(LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/log/favorite.json
        Response res = getCollectionResponseForProperty(localUser, fitbitUser, APICollectionType.activities, ApiCollectionProperty.favorite);
        throwExceptionIfError(res);
        return ActivityReference.constructActivityReferenceList(res);
    }
    public List<LoggedActivityReference> getRecentActivities(LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/recent.json
        Response res = getCollectionResponseForProperty(localUser, fitbitUser, APICollectionType.activities, ApiCollectionProperty.recent);
        throwExceptionIfError(res);
        return LoggedActivityReference.constructLoggedActivityReferenceList(res);
    }

    public List<LoggedActivityReference> getFrequentActivities(LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/recent.json
        Response res = getCollectionResponseForProperty(localUser, fitbitUser, APICollectionType.activities, ApiCollectionProperty.frequent);
        throwExceptionIfError(res);
        return LoggedActivityReference.constructLoggedActivityReferenceList(res);
    }

    public void logActivity(LocalUserDetail localUser,
                            long activityId,
                            int steps,
                            int durationMillis,
                            float distance,
                            LocalDate date,
                            LocalDate startTime) throws FitbitAPIException {

        List<PostParameter> params = new ArrayList<PostParameter>(5);
        params.add(new PostParameter("activityId", activityId));
        params.add(new PostParameter("steps", steps));
        params.add(new PostParameter("durationMillis", durationMillis));
        params.add(new PostParameter("distance", distance));
        params.add(new PostParameter("date", DateTimeFormat.forPattern("yyyy-MM-dd").print(date)));
        params.add(new PostParameter("startTime", FitbitApiService.LOCAL_TIME_HOURS_MINUTES_FORMATTER.print(startTime)));

        logActivity(localUser, params);
    }

    public void logActivity(LocalUserDetail localUser, List<PostParameter> params) throws FitbitAPIException {
        setAccessToken(localUser);
        // POST /1/user/-/activities.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/activities", APIFormat.JSON);

        Response res;
        try {
            res = httpPost(url, params.toArray(new PostParameter[params.size()]), true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error creating activity: " + e, e);
        }

        if (res.getStatusCode() != HttpServletResponse.SC_CREATED) {
            throw new FitbitAPIException("Error creating activity: " + res.getStatusCode());
        }
    }

    public void deleteActivityLog(LocalUserDetail localUser, String activityLogId) throws FitbitAPIException {
        setAccessToken(localUser);
        // DELETE /1/user/-/activities/123.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/activities/" + activityLogId, APIFormat.JSON);
        try {
            httpDelete(url, true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error deleting activity log entry: " + e, e);
        }
    }

    public Activity getActivity(LocalUserDetail localUser, long activityId) throws FitbitAPIException {
        return getActivity(localUser, String.valueOf(activityId));
    }

    public Activity getActivity(LocalUserDetail localUser, String activityId) throws FitbitAPIException {
        setAccessToken(localUser);

        // GET /1/activities/90001/levels.json
        // GET /1/activities/90001/levels.xml
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/activities/" + activityId, APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        try {
            return Activity.constructActivity(res.asJSONObject());
        } catch (JSONException e) {
             throw new FitbitAPIException("Error retrieving activity: " + e, e);
        }
    }

    public void addFavoriteActivity(LocalUserDetail localUser, String activityId) throws FitbitAPIException {
        setAccessToken(localUser);
        // POST /1/user/-/activities/log/favorite/123.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/activities/log/favorite/" + activityId, APIFormat.JSON);
        try {
            httpPost(url, null, true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error adding favorite activity: " + e, e);
        }
    }

    public void deleteFavoriteActivity(LocalUserDetail localUser, String activityId) throws FitbitAPIException {
        setAccessToken(localUser);
        // DELETE /1/user/-/activities/log/favorite/123.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/activities/log/favorite/" + activityId, APIFormat.JSON);
        try {
            httpDelete(url, true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error deleting favorite activity: " + e, e);
        }

    }

    public Food createFood(LocalUserDetail localUser, String name, String description, long defaultFoodMeasurementUnitId,
                           float defaultServingSize, int caloriesPerServingSize, FoodFormType formType) throws FitbitAPIException {
        NutritionalValuesEntry nutritionalValuesEntry =  new NutritionalValuesEntry();
        nutritionalValuesEntry.setCalories(caloriesPerServingSize);
        return createFood(localUser, name, description, defaultFoodMeasurementUnitId, defaultServingSize, formType, nutritionalValuesEntry);
    }

    public Food createFood(LocalUserDetail localUser, String name, String description, long defaultFoodMeasurementUnitId,
                           float defaultServingSize, FoodFormType formType,
                           NutritionalValuesEntry nutritionalValuesEntry) throws FitbitAPIException {
        setAccessToken(localUser);
        List<PostParameter> params = new ArrayList<PostParameter>();
        params.add(new PostParameter("name", name));
        params.add(new PostParameter("description", description));
        params.add(new PostParameter("defaultFoodMeasurementUnitId", defaultFoodMeasurementUnitId));
        params.add(new PostParameter("defaultServingSize", defaultServingSize));
        params.add(new PostParameter("formType", formType.toString()));

        params.add(new PostParameter("calories", nutritionalValuesEntry.getCalories()));
        params.add(new PostParameter("caloriesFromFat", nutritionalValuesEntry.getCaloriesFromFat()));
        params.add(new PostParameter("totalFat", nutritionalValuesEntry.getTotalFat()));
        params.add(new PostParameter("transFat", nutritionalValuesEntry.getTransFat()));
        params.add(new PostParameter("saturatedFat", nutritionalValuesEntry.getSaturatedFat()));
        params.add(new PostParameter("cholesterol", nutritionalValuesEntry.getCholesterol()));
        params.add(new PostParameter("sodium", nutritionalValuesEntry.getSodium()));
        params.add(new PostParameter("potassium", nutritionalValuesEntry.getPotassium()));
        params.add(new PostParameter("totalCarbohydrate", nutritionalValuesEntry.getTotalCarbohydrate()));
        params.add(new PostParameter("dietaryFiber", nutritionalValuesEntry.getDietaryFiber()));
        params.add(new PostParameter("sugars", nutritionalValuesEntry.getSugars()));
        params.add(new PostParameter("protein", nutritionalValuesEntry.getProtein()));
        params.add(new PostParameter("vitaminA", nutritionalValuesEntry.getVitaminA()));
        params.add(new PostParameter("vitaminC", nutritionalValuesEntry.getVitaminC()));
        params.add(new PostParameter("iron", nutritionalValuesEntry.getIron()));
        params.add(new PostParameter("calcium", nutritionalValuesEntry.getCalcium()));
        params.add(new PostParameter("thiamin", nutritionalValuesEntry.getThiamin()));
        params.add(new PostParameter("riboflavin", nutritionalValuesEntry.getRiboflavin()));
        params.add(new PostParameter("vitaminB6", nutritionalValuesEntry.getVitaminB6()));
        params.add(new PostParameter("vitaminB12", nutritionalValuesEntry.getVitaminB12()));
        params.add(new PostParameter("vitaminE", nutritionalValuesEntry.getVitaminE()));
        params.add(new PostParameter("folicAcid", nutritionalValuesEntry.getFolicAcid()));
        params.add(new PostParameter("niacin", nutritionalValuesEntry.getNiacin()));
        params.add(new PostParameter("magnesium", nutritionalValuesEntry.getMagnesium()));
        params.add(new PostParameter("phosphorus", nutritionalValuesEntry.getPhosphorus()));
        params.add(new PostParameter("iodine", nutritionalValuesEntry.getIodine()));
        params.add(new PostParameter("zinc", nutritionalValuesEntry.getZinc()));
        params.add(new PostParameter("copper", nutritionalValuesEntry.getCopper()));
        params.add(new PostParameter("biotin", nutritionalValuesEntry.getBiotin()));
        params.add(new PostParameter("pantothenicAcid", nutritionalValuesEntry.getPantothenicAcid()));
        params.add(new PostParameter("vitaminD", nutritionalValuesEntry.getVitaminD()));

        // URL :: /1/food/create.json
        String url = APIUtil.contextualizeUrl(getApiBaseSecuredUrl(), getApiVersion(), "/foods/create", APIFormat.JSON);

        Response response = httpPost(url, params.toArray(new PostParameter[params.size()]), true);

        try {
            return new Food(response.asJSONObject().getJSONObject("food"));
        } catch (JSONException e) {
            throw new FitbitAPIException("Error parsing json response to Food object: ", e);
        }
    }

    public Foods getFoods(LocalUserDetail localUser, FitbitUser fitbitUser, LocalDate date) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/date/2010-02-25.json
        Response res = getCollectionResponseForDate(localUser, fitbitUser, APICollectionType.foods, date);
        return Foods.constructFoods(res);
    }

    public List<LoggedFood> getLoggedFoods(LocalUserDetail localUser, FitbitUser fitbitUser, ApiCollectionProperty property) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/log/recent.json
        Response res = getCollectionResponseForProperty(localUser, fitbitUser, APICollectionType.foods, property);
        return LoggedFood.constructLoggedFoodReferenceList(res);
    }

    public List<FavoriteFood> getFavoriteFoods(LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/log/favorite.json
        Response res = getCollectionResponseForProperty(localUser, fitbitUser, APICollectionType.foods, ApiCollectionProperty.favorite);
        return FavoriteFood.constructFavoriteFoodList(res);
    }

    public List<LoggedFood> getRecentFoods(LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/log/recent.json
        return getLoggedFoods(localUser, fitbitUser, ApiCollectionProperty.recent);
    }

    public List<LoggedFood> getFrequentFoods(LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/log/frequent.json
        return getLoggedFoods(localUser, fitbitUser, ApiCollectionProperty.frequent);
    }

    public List<Food> searchFoods(LocalUserDetail localUser, String query) throws FitbitAPIException {
        setAccessToken(localUser);
        // Example URL: http://api.fitbit.com/1/user/228TQ4/foods/search.json?query=apple
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/foods/search", APIFormat.JSON);
        List<PostParameter> params = new ArrayList<PostParameter>(1);
        params.add(new PostParameter("query", query));
        Response res = httpGet(url, params.toArray(new PostParameter[params.size()]), true);
        return Food.constructFoodList(res);
    }

    public List<FoodUnit> getFoodUnits() throws FitbitAPIException {
        clearAccessToken();
        // Example URL: http://api.fitbit.com/1/foods/units.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/foods/units", APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        return FoodUnit.constructFoodUnitList(res);
    }

    public void logFood(LocalUserDetail localUser, long foodId, int mealTypeId, int unitId, String amount, LocalDate date) throws FitbitAPIException {
        List<PostParameter> params = new ArrayList<PostParameter>(5);
        params.add(new PostParameter("foodId", String.valueOf(foodId)));
        params.add(new PostParameter("mealTypeId", mealTypeId));
        params.add(new PostParameter("unitId", String.valueOf(unitId)));
        params.add(new PostParameter("amount", amount));
        params.add(new PostParameter("date", DateTimeFormat.forPattern("yyyy-MM-dd").print(date)));

        logFood(localUser, params);
    }

    public void logFood(LocalUserDetail localUser, List<PostParameter> params) throws FitbitAPIException {
        setAccessToken(localUser);
        // POST /1/user/-/food/log.json?foodId=123&unit=servings&quantityConsumed=1
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/foods/log", APIFormat.JSON);
        try {
            httpPost(url, params.toArray(new PostParameter[params.size()]), true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error creating food log entry: " + e, e);
        }
    }

    public void deleteFoodLog(LocalUserDetail localUser, String foodLogId) throws FitbitAPIException {
        setAccessToken(localUser);
        // DELETE /1/user/-/food/log/123.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/foods/log/" + foodLogId, APIFormat.JSON);
        try {
            httpDelete(url, true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error deleting food log entry: " + e, e);
        }
    }

    public void addFavoriteFood(LocalUserDetail localUser, String foodId) throws FitbitAPIException {
        setAccessToken(localUser);
        // POST /1/user/-/food/log/favorite/123.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/foods/log/favorite/" + foodId, APIFormat.JSON);
        try {
            httpPost(url, null, true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error adding favorite food: " + e, e);
        }
    }

    public void deleteFavoriteFood(LocalUserDetail localUser, String foodId) throws FitbitAPIException {
        setAccessToken(localUser);
        // DELETE /1/user/-/food/log/favorite/123.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/foods/log/favorite/" + foodId, APIFormat.JSON);
        try {
            httpDelete(url, true);
        } catch (Exception e) {
            throw new FitbitAPIException("Error deleting favorite food: " + e, e);
        }

    }

    public List<Meal> getMeals(LocalUserDetail localUser) throws FitbitAPIException {
        setAccessToken(localUser);
        // Example URL: http://api.fitbit.com/1/user/228TQ4/meals.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/meals", APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        try {
            return Meal.constructMeals(res);
        } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ": " + res.asString(), e);
        }
    }

    public List<Device> getDevices(LocalUserDetail localUser) throws FitbitAPIException {
        setAccessToken(localUser);
        // /1/user/-/tracker.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/devices", APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        return Device.constructDeviceList(res);
    }

    public Device getDevice(LocalUserDetail localUser, String deviceId, DeviceType type) throws FitbitAPIException {
        setAccessToken(localUser);
        // /1/user/-/devices/tracker/1234.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/devices/" + type.name().toLowerCase() + '/' + deviceId, APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        try {
            return new Device(res.asJSONObject().getJSONObject("device"));
        } catch (JSONException e) {
             throw new FitbitAPIException("Error retrieving device: " + e, e);
        }
    }

    public Response getCollectionResponseForDate(LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType type, LocalDate date) throws FitbitAPIException {
        setAccessToken(localUser);
        // Example: http://api.fitbit.com/1/user/228TQ4/foods/date/2010-02-25.xml
        // /1/user/228TQ4/foods/date/2010-02-25.xml
        String url = APIUtil.constructFullUrl(getApiBaseUrl(), getApiVersion(), fitbitUser, type, date, APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        return res;
    }

    public Response getCollectionResponseForProperty(LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType type, ApiCollectionProperty property) throws FitbitAPIException {
        setAccessToken(localUser);
        // Example: http://api.fitbit.com/1/user/228TQ4/foods/recent.xml
        // /1/user/228TQ4/foods/recent.xml
        String url = APIUtil.constructFullUrl(getApiBaseUrl(), getApiVersion(), fitbitUser, type, property, APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        return res;
    }

    public Object getCollectionForDate(LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType type, LocalDate date) throws FitbitAPIException {
        switch (type) {
            case activities: return getActivities(localUser, fitbitUser, date);
            case foods: return getFoods(localUser, fitbitUser, date);
            case meals: return getMeals(localUser);
            default: return null;
        }
    }

    public double getWeight(LocalUserDetail localUser, FitbitUser fitbitUser, LocalDate date) throws FitbitAPIException {
        return getBody(localUser, fitbitUser, date).getWeight();
    }

    public double getWeight(LocalUserDetail localUser, String date) throws FitbitAPIException {
        return getBody(localUser, date).getWeight();
    }

    public Body getBody(LocalUserDetail localUser, FitbitUser fitbitUser, LocalDate date) throws FitbitAPIException {
        setAccessToken(localUser);
        // Example: http://api.fitbit.com/1/user/228TQ4/body/2010-02-25.xml
        // /1/user/228TQ4/body/2010-02-25.xml
        String url = APIUtil.constructFullUrl(getApiBaseUrl(), getApiVersion(), fitbitUser, APICollectionType.body, date, APIFormat.JSON);

        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        try {
            return new Body(res.asJSONObject());
        } catch (JSONException e) {
             throw new FitbitAPIException("Error retrieving body: " + e, e);
        }
    }

    public Body getBody(LocalUserDetail localUser, String date) throws FitbitAPIException {
        setAccessToken(localUser);
        // GET /1/user/-/body/date/2010-02-25.xml
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/body/date/" + date, APIFormat.JSON);

        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        try {
            return new Body(res.asJSONObject());
        } catch (JSONException e) {
             throw new FitbitAPIException("Error retrieving body: " + e, e);
        }
    }

    public void logWeight(LocalUserDetail localUser, float weight, LocalDate date) throws FitbitAPIException {
        List<PostParameter> params = new ArrayList<PostParameter>(2);
        params.add(new PostParameter("weight", weight));
        params.add(new PostParameter("date", DateTimeFormat.forPattern("yyyy-MM-dd").print(date)));

        logWeight(localUser, params);
    }

    public void logWeight(LocalUserDetail localUser, List<PostParameter> params) throws FitbitAPIException {
        setAccessToken(localUser);
        // /1/user/-/body/weight.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/-/body/weight", APIFormat.JSON);

        try {
            httpPost(url, params.toArray(new PostParameter[params.size()]), true);
        } catch (FitbitAPIException e) {
            throw new FitbitAPIException("Error logging weight: " + e, e);
        }
    }

    public UserInfo getUserInfo(LocalUserDetail localUser) throws FitbitAPIException {
        return getUserInfo(localUser, FitbitUser.CURRENT_AUTHORIZED_USER);
    }

    public UserInfo getUserInfo(LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
        setAccessToken(localUser);
        // /1/user/-/profile.json
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), getApiVersion(), "/user/" + fitbitUser.getId() + "/profile", APIFormat.JSON);

        try {
            Response response = httpGet(url, true);
            throwExceptionIfError(response);
            return new UserInfo(response.asJSONObject());
        } catch (FitbitAPIException e) {
            throw new FitbitAPIException("Error getting user info: " + e, e);
        } catch (JSONException e) {
             throw new FitbitAPIException("Error getting user info: " + e, e);
        }
    }
    
    public Account registerAccount(String email, String password, String timezone) throws FitbitAPIException {
        return registerAccount(email, password, timezone, false);
    }

    public Account registerAccount(String email, String password, String timezone, boolean emailSubscribe) throws FitbitAPIException {

        List<PostParameter> params = new ArrayList<PostParameter>();
        params.add(new PostParameter("email", email));
        params.add(new PostParameter("password", password));
        params.add(new PostParameter("timezone", timezone));
        params.add(new PostParameter("emailSubscribe", String.valueOf(emailSubscribe)));

        // POST /1/user/-/account/register.json
        String url = APIUtil.contextualizeUrl(getApiBaseSecuredUrl(), getApiVersion(), "/account/register", APIFormat.JSON);

        Response response = httpPost(url, params.toArray(new PostParameter[params.size()]), true);

        try {
            return new Account(response.asJSONObject().getJSONObject("account"));
        } catch (JSONException e) {
            throw new FitbitAPIException("Error parsing json response to Account object: ", e);
        }
    }

    public ApiRateLimitStatus getIpRateLimitStatus() throws FitbitAPIException {
        return getRateLimitStatus(ApiQuotaType.IP_ADDRESS);
    }

    public ApiRateLimitStatus getClientAndUserRateLimitStatus(LocalUserDetail localUser) throws FitbitAPIException {
        setAccessToken(localUser);
        return getRateLimitStatus(ApiQuotaType.CLIENT_AND_OWNER);
    }

    public ApiRateLimitStatus getRateLimitStatus(ApiQuotaType quotaType) throws FitbitAPIException {
        // /1/account/clientAndUserRateLimitStatus.json OR /1/account/ipRateLimitStatus.json
        String relativePath = "/account/" + (quotaType == ApiQuotaType.CLIENT_AND_OWNER ? "clientAndUser" : "ip") + "RateLimitStatus";
        String url = APIUtil.contextualizeUrl(getApiBaseUrl(), APIVersion.BETA_1, relativePath, APIFormat.JSON);
        return new ApiRateLimitStatus(httpGet(url, true));
    }

    public SubscriptionDetail subscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser) throws FitbitAPIException {
    	return nullSafeSubscribe(subscriberId, localUser, fitbitUser, null, null);
    }

    public SubscriptionDetail subscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType collectionType) throws FitbitAPIException {
    	return nullSafeSubscribe(subscriberId, localUser, fitbitUser, collectionType, null);
    }

    public SubscriptionDetail subscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser, String subscriptionId) throws FitbitAPIException {
    	return nullSafeSubscribe(subscriberId, localUser, fitbitUser, null, subscriptionId);
    }

    public SubscriptionDetail subscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType collectionType, String subscriptionId) throws FitbitAPIException {
    	return nullSafeSubscribe(subscriberId, localUser, fitbitUser, collectionType, subscriptionId);
    }

    public void unsubscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser, String subscriptionId) throws FitbitAPIException {
    	nullSafeUnsubscribe(subscriberId, localUser, fitbitUser, null, subscriptionId);
    }

    public void unsubscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType collectionType, String subscriptionId) throws FitbitAPIException {
    	nullSafeUnsubscribe(subscriberId, localUser, fitbitUser, collectionType, subscriptionId);
    }

    /* ********************************************************************* */

    protected SubscriptionDetail nullSafeSubscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType collectionType, String subscriptionId) throws FitbitAPIException {
        setAccessToken(localUser);

        String url =
        	APIUtil.constructFullSubscriptionUrl(
        		getApiBaseUrl(),
        		getApiVersion(),
        		fitbitUser,
        		collectionType,
        		null==subscriptionId ? APIUtil.UNSPECIFIED_SUBSCRIPTION_ID : subscriptionId,
        		APIFormat.JSON
        	);
        setSubscriberId(subscriberId);

        try {
        	return new SubscriptionDetail(httpPost(url, null, true).asJSONObject());
        } catch (FitbitAPIException e) {
            throw e;
        } catch (Exception e) {
        	throw new FitbitAPIException("Could not create subscription: " + e, e);
        }
    }

    protected void nullSafeUnsubscribe(String subscriberId, LocalUserDetail localUser, FitbitUser fitbitUser, APICollectionType collectionType, String subscriptionId) throws FitbitAPIException {
        setAccessToken(localUser);

        String url =
        	APIUtil.constructFullSubscriptionUrl(
        		getApiBaseUrl(),
        		getApiVersion(),
        		fitbitUser,
        		collectionType,
        		subscriptionId,
        		APIFormat.JSON
        	);
        setSubscriberId(subscriberId);

        httpDelete(url, true);
    }

    /*
     * Time Series
     */

    /**
     * Get time series for anonymous user.
     */
    public Map<String, List<Data>> getTimeSeries(FitbitUser user, TimeSeriesResourceType resourceType, LocalDate startDate, TimePeriod period) throws FitbitAPIException {
        return getTimeSeries(null, user, resourceType, startDate.toString(), period.getShortForm());
    }

    public Map<String, List<Data>> getTimeSeries(FitbitUser user, TimeSeriesResourceType resourceType, String startDate, TimePeriod period) throws FitbitAPIException {
        return getTimeSeries(null, user, resourceType, startDate,  period.getShortForm());
    }

    public Map<String, List<Data>> getTimeSeries(FitbitUser user, TimeSeriesResourceType resourceType, LocalDate startDate, LocalDate endDate) throws FitbitAPIException {
        return getTimeSeries(null, user, resourceType, startDate.toString(),  endDate.toString());
    }

    /**
     * Get time series for authorized user.
     */
    public Map<String, List<Data>> getTimeSeries(LocalUserDetail localUser, FitbitUser user, TimeSeriesResourceType resourceType, LocalDate startDate, TimePeriod period) throws FitbitAPIException {
        return getTimeSeries(localUser, user, resourceType, startDate.toString(), period.getShortForm());
    }

    public Map<String, List<Data>> getTimeSeries(LocalUserDetail localUser, FitbitUser user, TimeSeriesResourceType resourceType, String startDate, TimePeriod period) throws FitbitAPIException {
        return getTimeSeries(localUser, user, resourceType, startDate,  period.getShortForm());
    }

    public Map<String, List<Data>> getTimeSeries(LocalUserDetail localUser, FitbitUser user, TimeSeriesResourceType resourceType, LocalDate startDate, LocalDate endDate) throws FitbitAPIException {
        return getTimeSeries(localUser, user, resourceType, startDate.toString(),  endDate.toString());
    }

    public Map<String, List<Data>> getTimeSeries(LocalUserDetail localUser, FitbitUser user, TimeSeriesResourceType resourceType, String startDate, String periodOrEndDate) throws FitbitAPIException {
        if (localUser != null) {
            setAccessToken(localUser);
        } else {
            clearAccessToken();
        }

        String url = APIUtil.constructTimeSeriesUrl(getApiBaseUrl(), getApiVersion(), user, resourceType, startDate, periodOrEndDate, APIFormat.JSON);
        Response res = httpGet(url, true);
        throwExceptionIfError(res);
        return Data.constructDataListMap(res);
    }

    /* ********************************************************************* */

    protected void setAccessToken(LocalUserDetail localUser) {
        // Get the access token for the user:
        APIResourceCredentials resourceCredentials = credentialsCache.getResourceCredentials(localUser);
        // Set the access token in the client:
        setOAuthAccessToken(resourceCredentials.getAccessToken(), resourceCredentials.getAccessTokenSecret(), resourceCredentials.getLocalUserId());
    }

    protected void clearAccessToken() {
        // Set the access token in the client to null:
        setOAuthAccessToken(null);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws FitbitAPIException when Fitbit service or network is unavailable
     */

    protected Response httpGet(String url, boolean authenticate) throws FitbitAPIException {
        return httpGet(url, null, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @return the response
     * @throws FitbitAPIException when Fitbit service or network is unavailable
     */

    protected Response httpGet(String url, String name1, String value1, boolean authenticate) throws FitbitAPIException {
        return httpGet(url, new PostParameter[]{new PostParameter(name1, value1)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @param name2        the name of the second parameter
     * @param value2       the value of the second parameter
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws FitbitAPIException when Fitbit service or network is unavailable
     */
    protected Response httpGet(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws FitbitAPIException {
        return httpGet(url, new PostParameter[]{new PostParameter(name1, value1), new PostParameter(name2, value2)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws FitbitAPIException when Fitbit service or network is unavailable
     */
    protected Response httpGet(String url, PostParameter[] params, boolean authenticate) throws FitbitAPIException {
        return http.get(appendParamsToUrl(url, params), authenticate);
    }

    protected Response httpPost(String url, PostParameter[] params, boolean authenticate) throws FitbitAPIException {
    	return http.post(url, params, authenticate);
    }

    protected Response httpDelete(String url, boolean authenticate) throws FitbitAPIException {
    	return httpDelete(url, null, authenticate);
    }

    protected Response httpDelete(String url, PostParameter[] params, boolean authenticate) throws FitbitAPIException {
    	// We use Sun's HttpURLConnection, which does not like request entities
    	// submitted on HTTP DELETE
    	return http.delete(appendParamsToUrl(url, params), authenticate);
    }

    protected static String appendParamsToUrl(String url, PostParameter[] params) {
        if (null != params && params.length > 0) {
        	return url + '?' + HttpClient.encodeParameters(params);
        }
        return url;
    }

    public static void throwExceptionIfError(Response res) throws FitbitAPIException {
        if (res.isError()) {
            throw new FitbitAPIException(getErrorMessage(res));
        }
    }

    public static String getErrorMessage(Response res) throws FitbitAPIException {
        return res.isError() ? res.asString() : "";
    }

    public void setLocale(Locale locale) {
        if( locale == null ) {
            http.removeRequestHeader("Accept-Language");
        } else {
            http.setRequestHeader("Accept-Language", locale.toString());
        }
    }
}
