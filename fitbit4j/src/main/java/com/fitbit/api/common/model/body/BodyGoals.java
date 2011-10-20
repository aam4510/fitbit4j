package com.fitbit.api.common.model.body;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class BodyGoals {

    private Double weight;

    public BodyGoals(JSONObject json) throws JSONException {
        if (StringUtils.isNotBlank(json.optString("weight"))) {
            weight = json.getDouble("weight");
        }
    }

    public Double getWeight() {
        return weight;
    }
}
