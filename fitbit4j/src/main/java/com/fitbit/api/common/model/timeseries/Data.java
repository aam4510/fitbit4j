package com.fitbit.api.common.model.timeseries;

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.client.http.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, List<Data>> constructDataListMap(Response res) throws FitbitAPIException {
        try {
            JSONArray nameArray = res.asJSONObject().names();
            Map<String, List<Data>> dataListMap = new HashMap<String, List<Data>>(nameArray.length());
            for (int i = 0; i < nameArray.length(); i++) {
                String name = nameArray.getString(i);
                dataListMap.put(name, jsonArrayToDataList(res.asJSONObject().getJSONArray(name)));
            }
            return dataListMap;
         } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
        }
    }

    private static List<Data> jsonArrayToDataList(JSONArray array) throws JSONException {
        List<Data> foodReferenceList = new ArrayList<Data>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonData = array.getJSONObject(i);
            foodReferenceList.add(new Data(jsonData));
        }
        return foodReferenceList;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getValue() {
        return value;
    }
}
