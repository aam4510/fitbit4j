package com.fitbit.api.common.model.units;

/**
 * User: gkutlu
 * Date: May 19, 2010
 * Time: 2:21:23 PM
 */
public enum VolumeUnits {
    ML("ml"),
    FL_OZ("fl oz");

    String text;

    VolumeUnits(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
