package com.fitbit.api.common.model.units;

/**
 * User: gkutlu
 * Date: Mar 25, 2010
 * Time: 4:08:00 PM
 */
public enum DistanceUnits {
    METRIC("km"),
    US("miles");

    String text;
    DistanceUnits(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}