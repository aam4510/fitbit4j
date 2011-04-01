package com.fitbit.api.common.model.activities;

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.client.http.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: gkutlu
 * Date: Mar 4, 2010
 * Time: 2:53:29 PM
 */
public class Activity {
    long id;
    String name;
    boolean hasSpeed;
    List<ActivityLevel> activityLevels;

    public static final List<ActivityLevel> EMPTY_ACTIVITY_LEVEL_LIST = new ArrayList<ActivityLevel>();

    public Activity(long id, String name) {
        this(id, name, false, EMPTY_ACTIVITY_LEVEL_LIST);
    }

    public Activity(long id, String name, boolean hasSpeed, List<ActivityLevel> activityLevels) {
        this.id = id;
        this.name = name;
        this.hasSpeed = hasSpeed;
        this.activityLevels = activityLevels;
    }

    public Activity(JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        hasSpeed = json.getBoolean("hasSpeed");
        activityLevels = jsonArrayToActivityLevelList(json.getJSONArray("activityLevels"));
    }

    public static List<ActivityLevel> constructActivityLevelList(Response res) throws FitbitAPIException {
         try {
             JSONObject json = res.asJSONObject();
             return jsonArrayToActivityLevelList(json.getJSONArray("activityLevels"));
          } catch (JSONException e) {
             throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
         }
     }

     public static List<ActivityLevel> jsonArrayToActivityLevelList(JSONArray array) throws JSONException {
         List<ActivityLevel> activityLevelList = new ArrayList<ActivityLevel>(array.length());
         for (int i = 0; i < array.length(); i++) {
             JSONObject jsonActivityLevel = array.getJSONObject(i);
             activityLevelList.add(new ActivityLevel(jsonActivityLevel));
         }
         return activityLevelList;
     }

    public static Activity constructActivity(JSONObject json) throws JSONException {
        return new Activity(json.getJSONObject("activity"));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getHasSpeed() {
        return hasSpeed;
    }

    public List<ActivityLevel> getActivityLevels() {
        return activityLevels;
    }

    public boolean hasLevels() {
        return !activityLevels.isEmpty();
    }    
}
