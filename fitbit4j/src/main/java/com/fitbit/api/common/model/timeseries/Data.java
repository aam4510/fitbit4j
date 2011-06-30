package com.fitbit.api.common.model.timeseries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: gkutlu
 * Date: Aug 14, 2010
 * Time: 11:22:33 PM
 */
public class Data {
    String dateTime;
    double value;

    public Data(String dateTime, double value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public Data(JSONObject json) throws JSONException {
        dateTime = json.getString("dateTime");
        value = json.getDouble("value");
    }

    public static List<Data> jsonArrayToDataList(JSONArray array) throws JSONException {
        List<Data> dataList = new ArrayList<Data>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonData = array.getJSONObject(i);
            dataList.add(new Data(jsonData));
        }
        return dataList;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getValue() {
        return value;
    }
}
