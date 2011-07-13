package com.fitbit.api.model;

public enum ApiQuotaType {
    ANY,
    CLIENT,
    CLIENT_AND_VIEWER,
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
