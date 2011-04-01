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
 * Date: May 22, 2010
 * Time: 4:27:21 PM
 */
public class LoggedActivityReference extends ActivityReference {
    int calories;
    int duration;
    double distance;

    public LoggedActivityReference(long activityId, String name, String description, int calories, int duration, double distance) {
        super(activityId, name, description);
        this.calories = calories;
        this.duration = duration;
        this.distance = distance;
    }

    public LoggedActivityReference(JSONObject json) throws JSONException {
        super(json);
        calories = json.getInt("calories");
        duration = json.getInt("duration");
        distance = json.getDouble("distance");
    }

    public static List<LoggedActivityReference> constructLoggedActivityReferenceList(Response res) throws FitbitAPIException {
        try {
            return jsonArrayToLoggedActivityReferenceList(res.asJSONArray());
         } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
        }
    }

    private static List<LoggedActivityReference> jsonArrayToLoggedActivityReferenceList(JSONArray array) throws JSONException {
        List<LoggedActivityReference> loggedActivityReferenceList = new ArrayList<LoggedActivityReference>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonLoggedActivityReference = array.getJSONObject(i);
            loggedActivityReferenceList.add(new LoggedActivityReference(jsonLoggedActivityReference));
        }
        return loggedActivityReferenceList;
    }

    public int getCalories() {
        return calories;
    }

    public int getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }
}