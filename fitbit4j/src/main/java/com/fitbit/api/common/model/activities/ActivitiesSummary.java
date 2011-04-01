package com.fitbit.api.common.model.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: gkutlu
 * Date: Mar 2, 2010
 * Time: 7:26:43 PM
 */
public class ActivitiesSummary {
    private int caloriesOut;
    private int activeScore;
    private int steps;
    private int sedentaryMinutes;
    private int lightlyActiveMinutes;
    private int fairlyActiveMinutes;
    private int veryActiveMinutes;
    private List<ActivityDistance> distances;

    public ActivitiesSummary() {
    }
    
    public ActivitiesSummary(JSONObject json) throws JSONException {
        caloriesOut = json.getInt("caloriesOut");
        activeScore = json.getInt("activeScore");
        steps = json.getInt("steps");
        sedentaryMinutes = json.getInt("sedentaryMinutes");
        lightlyActiveMinutes = json.getInt("lightlyActiveMinutes");
        fairlyActiveMinutes = json.getInt("fairlyActiveMinutes");
        veryActiveMinutes = json.getInt("veryActiveMinutes");
        distances = jsonArrayToActivityList(json.getJSONArray("distances"));
    }

    private static List<ActivityDistance> jsonArrayToActivityList(JSONArray array) throws JSONException {
        List<ActivityDistance> trends = new ArrayList<ActivityDistance>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject distance = array.getJSONObject(i);
            trends.add(new ActivityDistance(distance));
        }
        return trends;
    }

    public int getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(int caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public int getActiveScore() {
        return activeScore;
    }

    public void setActiveScore(int activeScore) {
        this.activeScore = activeScore;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getSedentaryMinutes() {
        return sedentaryMinutes;
    }

    public void setSedentaryMinutes(int sedentaryMinutes) {
        this.sedentaryMinutes = sedentaryMinutes;
    }

    public int getLightlyActiveMinutes() {
        return lightlyActiveMinutes;
    }

    public void setLightlyActiveMinutes(int lightlyActiveMinutes) {
        this.lightlyActiveMinutes = lightlyActiveMinutes;
    }

    public int getFairlyActiveMinutes() {
        return fairlyActiveMinutes;
    }

    public void setFairlyActiveMinutes(int fairlyActiveMinutes) {
        this.fairlyActiveMinutes = fairlyActiveMinutes;
    }

    public int getVeryActiveMinutes() {
        return veryActiveMinutes;
    }

    public void setVeryActiveMinutes(int veryActiveMinutes) {
        this.veryActiveMinutes = veryActiveMinutes;
    }

    public List<ActivityDistance> getDistances() {
        return distances;
    }

    public void setDistances(List<ActivityDistance> distances) {
        this.distances = distances;
    }
}
