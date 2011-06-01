package com.fitbit.api.common.model.activities;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLevel {
    long id;
    String name;
    float minSpeedMPH;
    float maxSpeedMPH;

    public ActivityLevel(long id, String name, float minSpeedMPH, float maxSpeedMPH) {
        this.id = id;
        this.name = name;
        this.minSpeedMPH = minSpeedMPH;
        this.maxSpeedMPH = maxSpeedMPH;
    }

    @SuppressWarnings({"NumericCastThatLosesPrecision"})
    public ActivityLevel(JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        minSpeedMPH = (float)json.getDouble("minSpeedMPH");
        maxSpeedMPH = (float)json.getDouble("maxSpeedMPH");
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getMaxSpeedMPH() {
        return maxSpeedMPH;
    }

    public float getMinSpeedMPH() {
        return minSpeedMPH;
    }

}
