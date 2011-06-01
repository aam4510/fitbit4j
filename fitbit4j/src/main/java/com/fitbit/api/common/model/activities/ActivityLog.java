package com.fitbit.api.common.model.activities;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLog extends LoggedActivityReference {
    long logId;
    String startTime;
    boolean isFavorite;

    public ActivityLog(long logId, long activityId, String name, String description, String startTime,
                       int duration, double distance, int calories, boolean isFavorite) {
        super(activityId, name, description, calories, duration, distance);
        this.logId = logId;
        this.startTime = startTime;
        this.isFavorite = isFavorite;
    }

    public ActivityLog(JSONObject json) throws JSONException {
        super(json);
        logId = json.getLong("logId");
        startTime = json.getString("startTime");
        isFavorite = json.getBoolean("isFavorite");
    }

    public long getLogId() {
        return logId;
    }

    public String getStartTime() {
        return startTime;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

}