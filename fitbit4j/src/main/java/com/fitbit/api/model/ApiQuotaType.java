package com.fitbit.api.model;

/**
* User: gkutlu
* Date: Apr 19, 2010
* Time: 10:28:12 PM
*/
public enum ApiQuotaType {
    ANY,
    CLIENT,
    CLIENT_AND_OWNER,
    CLIENT_AND_IP,
    IP_ADDRESS,
    OWNER;

    private static int length = values().length;

    public static int numTypes() {
        return length;
    }

    public String getDescription() {
        return name().toLowerCase().replaceAll("_", " ");
    }
}
