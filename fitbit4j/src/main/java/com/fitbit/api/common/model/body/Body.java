package com.fitbit.api.common.model.body;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: gkutlu
 * Date: May 25, 2010
 * Time: 9:39:30 PM
 */
public class Body {
    double weight;

    public Body(double weight) {
        this.weight = weight;
    }

    public Body(JSONObject json) throws JSONException {
        JSONObject bodyJson = json.getJSONObject("body");
        weight = bodyJson.getDouble("weight");
    }

    public double getWeight() {
        return weight;
    }
}
