package com.fitbit.api.common.model.body;

import org.json.JSONException;
import org.json.JSONObject;

public class Body {

    double weight;

    public Body(JSONObject json) throws JSONException {
        weight = json.getDouble("weight");
    }

    public double getWeight() {
        return weight;
    }
}
