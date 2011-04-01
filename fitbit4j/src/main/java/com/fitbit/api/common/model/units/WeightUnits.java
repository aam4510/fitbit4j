package com.fitbit.api.common.model.units;

/**
 * User: gkutlu
 * Date: Mar 25, 2010
 * Time: 4:03:50 PM
 */
public enum WeightUnits {
    KG("kg"),
    POUNDS("lb"),
    STONE("stone");

    String text;

    WeightUnits(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
