package com.fitbit.api.common.model.activities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: gkutlu
 * Date: Mar 2, 2010
 * Time: 7:30:12 PM
 */
public class ActivityDistance {
    private String activity;
    private double distance;

    public ActivityDistance(String activity, double distance) {
        this.activity = activity;
        this.distance = distance;
    }

    public ActivityDistance(JSONObject json) throws JSONException {
        activity = json.getString("activity");
        distance = json.getDouble("distance");
    }

    public String getActivity() {
        return activity;
    }

    public double getDistance() {
        return distance;
    }
}
