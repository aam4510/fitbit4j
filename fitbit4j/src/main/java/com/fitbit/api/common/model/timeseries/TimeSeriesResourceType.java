package com.fitbit.api.common.model.timeseries;

/**
 * User: gkutlu
 * Date: Aug 16, 2010
 * Time: 7:36:20 PM
 */
public enum TimeSeriesResourceType {
    //food
    CALORIES_IN("/foods/log/caloriesIn"),
    WATER("/foods/log/water"),
    //activity
    CALORIES_OUT("/activities/log/calories"),
    STEPS("/activities/log/steps"),
    DISTANCE("/activities/log/distance"),
    MINUTES_SEDENTARY("/activities/log/minutesSedentary"),
    MINUTES_LIGHTLY_ACTIVE("/activities/log/minutesLightlyActive"),
    MINUTES_FAIRLY_ACTIVE("/activities/log/minutesFairlyActive"),
    MINUTES_VERY_ACTIVE("/activities/log/minutesVeryActive"),
    ACTIVE_SCORE("/activities/log/activeScore"),
    ACTIVITY_CALORIES("/activities/log/activityCalories"),
    FLOORS("/activities/log/floors"),
    ELEVATION("/activities/log/elevation"),
    //sleep
    MINUTES_ASLEEP("/sleep/minutesAsleep"),
    MINUTES_AWAKE("/sleep/minutesAwake"),
    AWAKENINGS_COUNT("/sleep/awakeningsCount"),
    TIME_IN_BED("/sleep/timeInBed"),
    MINUTES_TO_FALL_ASLEEP("/sleep/minutesToFallAsleep"),
    MINUTES_AFTER_WAKEUP("/sleep/minutesAfterWakeup"),
    TIME_ENTERED_BED("/sleep/startTime"),
    EFFICIENCY("/sleep/efficiency"),
    //body
    WEIGHT("/body/weight"),
    BMI("/body/bmi"),
    FAT("/body/fat");

    private String resourcePath;

    TimeSeriesResourceType(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}