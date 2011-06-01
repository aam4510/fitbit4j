package com.fitbit.api.common.model.body;

import org.json.JSONException;
import org.json.JSONObject;

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
