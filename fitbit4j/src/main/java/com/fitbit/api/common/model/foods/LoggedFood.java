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
 * Date: May 22, 2010
 * Time: 4:27:21 PM
 */
public class LoggedFood extends Food {
    private final int calories;
    private final double amount;
    private final FoodUnit unit;
    private final byte mealTypeId;

    public LoggedFood(long foodId, String name, String brand, String accessLevel, int calories, double amount, FoodUnit unit, byte mealTypeId, int[] units) {
        super(foodId, name, brand, accessLevel, units);
        this.calories = calories;
        this.amount = amount;
        this.unit = unit;
        this.mealTypeId = mealTypeId;
    }

    public LoggedFood(JSONObject json) throws JSONException {
        super(json);
        calories = json.getInt("calories");
        amount = json.getDouble("amount");
        unit = new FoodUnit(json.getJSONObject("unit"));
        //noinspection NumericCastThatLosesPrecision
        mealTypeId = (byte) json.getInt("mealTypeId");
    }

    public static List<LoggedFood> constructLoggedFoodReferenceList(Response res) throws FitbitAPIException {
        try {
            return jsonArrayToLoggedFoodReferenceList(res.asJSONArray());
        } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
        }
    }

    private static List<LoggedFood> jsonArrayToLoggedFoodReferenceList(JSONArray array) throws JSONException {
        List<LoggedFood> loggedFoodList = new ArrayList<LoggedFood>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonLoggedFoodReference = array.getJSONObject(i);
            loggedFoodList.add(new LoggedFood(jsonLoggedFoodReference));
        }
        return loggedFoodList;
    }

    public final int getCalories() {
        return calories;
    }

    public final double getAmount() {
        return amount;
    }

    public final FoodUnit getUnit() {
        return unit;
    }

    public final byte getMealTypeId() {
        return mealTypeId;
    }

}