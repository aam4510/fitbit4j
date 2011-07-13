package com.fitbit.api.common.model.activities;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLog extends LoggedActivityReference {
    long logId;
    String startTime;
    boolean isFavorite;

    public ActivityLog(long logId, long activityId, String name, String description, Long activityParentId, String activityParentName, String startTime,
                       int duration, Double distance, int calories, Integer steps, boolean isFavorite) {
        super(activityId, name, description, activityParentId, activityParentName, calories, duration, distance, steps);
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