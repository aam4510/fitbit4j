package com.fitbit.api.common.model.user;

import org.json.JSONException;
import org.json.JSONObject;

public class Account {

    private final String encodedId;
    private final String profileUpdateUuid;

    public Account(String encodedId, String profileUpdateUuid) {
        this.encodedId = encodedId;
        this.profileUpdateUuid = profileUpdateUuid;
    }

    public Account(JSONObject json) throws JSONException {
        encodedId = json.getString("encodedId");
        profileUpdateUuid = json.getString("profileUpdateUuid");
    }


    public String getEncodedId() {
        return this.encodedId;
    }

    public String getProfileUpdateUuid() {
        return this.profileUpdateUuid;
    }
}
