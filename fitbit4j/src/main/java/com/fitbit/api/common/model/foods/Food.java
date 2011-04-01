package com.fitbit.api.common.model.foods;

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.client.http.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: gkutlu
 * Date: Mar 4, 2010
 * Time: 2:53:29 PM
 */
public class Food {
    private final long foodId;
    private final String name;
    private final String brand;
    private final int[] units;

    public Food(long foodId, String name, String brand, int[] units) {
        this.foodId = foodId;
        this.name = name;
        this.brand = brand;
        this.units = units;
    }

    public Food(JSONObject json) throws JSONException {
        foodId = json.getLong("foodId");
        name = json.getString("name");
        brand = json.getString("brand");
        units = jsonArrayToUnitIdArray(json.getJSONArray("units"));
    }

    public static List<Food> constructFoodList(Response res) throws FitbitAPIException {
        return constructFoodList(res, "foods");
    }

    public static List<Food> constructFoodList(Response res, String arrayName) throws FitbitAPIException {
        try {
            JSONObject json = res.asJSONObject();
            return jsonArrayToFoodList(json.getJSONArray(arrayName));
         } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
        }
    }

    static List<Food> jsonArrayToFoodList(JSONArray array) throws JSONException {
        List<Food> foodList = new ArrayList<Food>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject food = array.getJSONObject(i);
            foodList.add(new Food(food));
        }
        return foodList;
    }

    static int[] jsonArrayToUnitIdArray(JSONArray array) throws JSONException {
        int[] units = new int[array.length()];
        for (int i = 0; i < array.length(); i++) {
            units[i] = array.getInt(i);
        }
        return units;
    }

    public final long getFoodId() {
        return foodId;
    }

    public final String getName() {
        return name;
    }

    public final String getBrand() {
        return brand;
    }

    public final int[] getUnits() {
        return units;
    }
}