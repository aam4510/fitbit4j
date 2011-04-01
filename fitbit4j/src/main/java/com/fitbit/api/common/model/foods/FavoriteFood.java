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
 * Date: 1/18/11
 * Time: 10:42 AM
 */
public class FavoriteFood extends Food {
    private final int calories;

    public FavoriteFood(long foodId, String name, String brand, int[] units, int calories) {
        super(foodId, name, brand, units);
        this.calories = calories;
    }

    public FavoriteFood(JSONObject json) throws JSONException {
        super(json);
        calories = json.getInt("calories");
    }

    public int getCalories() {
        return calories;
    }

    public static List<FavoriteFood> constructFavoriteFoodList(Response res) throws FitbitAPIException {
        try {
            return jsonArrayToFavoriteFoodList(res.asJSONArray());
        } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
        }
    }

    private static List<FavoriteFood> jsonArrayToFavoriteFoodList(JSONArray array) throws JSONException {
        List<FavoriteFood> favoriteFoodList = new ArrayList<FavoriteFood>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonFavoriteFood = array.getJSONObject(i);
            favoriteFoodList.add(new FavoriteFood(jsonFavoriteFood));
        }
        return favoriteFoodList;
    }

}
