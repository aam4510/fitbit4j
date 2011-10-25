package com.fitbit.api.common.model.activities;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayableActivity {

    private long id;
    private  String name;
    private  String accessLevel;

    public DisplayableActivity(JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        accessLevel = json.getString("accessLevel");
    }

    public DisplayableActivity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

}
