package com.fitbit.api.common.model.foods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: Dzmitry Krutko
 * Date: Jan 16, 2011
 * Time: 4:35:16 PM
 */
public class NutritionalValuesEntry {
    private int calories;
    private int caloriesFromFat;
    private float totalFat;
    private float saturatedFat;
    private float transFat;
    private float cholesterol;
    private float sodium;
    private float totalCarbohydrate;
    private float dietaryFiber;
    private float sugars;
    private float protein;
    private float vitaminA;
    private float vitaminC;
    private float iron;
    private float calcium;
    private float potassium;
    private float thiamin;
    private float riboflavin;
    private float niacin;
    private float vitaminD;
    private float vitaminE;
    private float vitaminB6;
    private float folicAcid;
    private float vitaminB12;
    private float phosphorus;
    private float iodine;
    private float magnesium;
    private float zinc;
    private float copper;
    private float biotin;
    private float pantothenicAcid;

    public NutritionalValuesEntry() {
    }

    public NutritionalValuesEntry(JSONObject json) throws JSONException {
        this.calories = json.getInt("calories");
        this.caloriesFromFat = json.optInt("caloriesFromFat");
        this.totalFat = (float) json.optDouble("totalFat");
        this.saturatedFat = (float) json.optDouble("saturatedFat");
        this.transFat = (float) json.optDouble("transFat");
        this.cholesterol = (float) json.optDouble("cholesterol");
        this.sodium = (float) json.optDouble("sodium");
        this.totalCarbohydrate = (float) json.optDouble("totalCarbohydrate");
        this.dietaryFiber = (float) json.optDouble("dietaryFiber");
        this.sugars = (float) json.optDouble("sugars");
        this.protein = (float) json.optDouble("protein");
        this.vitaminA = (float) json.optDouble("vitaminA");
        this.vitaminC = (float) json.optDouble("vitaminC");
        this.iron = (float) json.optDouble("iron");
        this.calcium = (float) json.optDouble("calcium");
        this.potassium = (float) json.optDouble("potassium");
        this.thiamin = (float) json.optDouble("thiamin");
        this.riboflavin = (float) json.optDouble("riboflavin");
        this.niacin = (float) json.optDouble("niacin");
        this.vitaminD = (float) json.optDouble("vitaminD");
        this.vitaminE = (float) json.optDouble("vitaminE");
        this.vitaminB6 = (float) json.optDouble("vitaminB6");
        this.folicAcid = (float) json.optDouble("folicAcid");
        this.vitaminB12 = (float) json.optDouble("vitaminB12");
        this.phosphorus = (float) json.optDouble("phosphorus");
        this.iodine = (float) json.optDouble("iodine");
        this.magnesium = (float) json.optDouble("magnesium");
        this.zinc = (float) json.optDouble("zinc");
        this.copper = (float) json.optDouble("copper");
        this.biotin = (float) json.optDouble("biotin");
        this.pantothenicAcid = (float) json.optDouble("pantothenicAcid");
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCaloriesFromFat() {
        return caloriesFromFat;
    }

    public void setCaloriesFromFat(int caloriesFromFat) {
        this.caloriesFromFat = caloriesFromFat;
    }

    public float getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(float totalFat) {
        this.totalFat = totalFat;
    }

    public float getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(float saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public float getTransFat() {
        return transFat;
    }

    public void setTransFat(float transFat) {
        this.transFat = transFat;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(float sodium) {
        this.sodium = sodium;
    }

    public float getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(float totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public float getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(float dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public float getSugars() {
        return sugars;
    }

    public void setSugars(float sugars) {
        this.sugars = sugars;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getVitaminA() {
        return vitaminA;
    }

    public void setVitaminA(float vitaminA) {
        this.vitaminA = vitaminA;
    }

    public float getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(float vitaminC) {
        this.vitaminC = vitaminC;
    }

    public float getIron() {
        return iron;
    }

    public void setIron(float iron) {
        this.iron = iron;
    }

    public float getCalcium() {
        return calcium;
    }

    public void setCalcium(float calcium) {
        this.calcium = calcium;
    }

    public float getPotassium() {
        return potassium;
    }

    public void setPotassium(float potassium) {
        this.potassium = potassium;
    }

    public float getThiamin() {
        return thiamin;
    }

    public void setThiamin(float thiamin) {
        this.thiamin = thiamin;
    }

    public float getRiboflavin() {
        return riboflavin;
    }

    public void setRiboflavin(float riboflavin) {
        this.riboflavin = riboflavin;
    }

    public float getNiacin() {
        return niacin;
    }

    public void setNiacin(float niacin) {
        this.niacin = niacin;
    }

    public float getVitaminD() {
        return vitaminD;
    }

    public void setVitaminD(float vitaminD) {
        this.vitaminD = vitaminD;
    }

    public float getVitaminE() {
        return vitaminE;
    }

    public void setVitaminE(float vitaminE) {
        this.vitaminE = vitaminE;
    }

    public float getVitaminB6() {
        return vitaminB6;
    }

    public void setVitaminB6(float vitaminB6) {
        this.vitaminB6 = vitaminB6;
    }

    public float getFolicAcid() {
        return folicAcid;
    }

    public void setFolicAcid(float folicAcid) {
        this.folicAcid = folicAcid;
    }

    public float getVitaminB12() {
        return vitaminB12;
    }

    public void setVitaminB12(float vitaminB12) {
        this.vitaminB12 = vitaminB12;
    }

    public float getPhosphorus() {
        return phosphorus;
    }

    public void setPhosphorus(float phosphorus) {
        this.phosphorus = phosphorus;
    }

    public float getIodine() {
        return iodine;
    }

    public void setIodine(float iodine) {
        this.iodine = iodine;
    }

    public float getMagnesium() {
        return magnesium;
    }

    public void setMagnesium(float magnesium) {
        this.magnesium = magnesium;
    }

    public float getZinc() {
        return zinc;
    }

    public void setZinc(float zinc) {
        this.zinc = zinc;
    }

    public float getCopper() {
        return copper;
    }

    public void setCopper(float copper) {
        this.copper = copper;
    }

    public float getBiotin() {
        return biotin;
    }

    public void setBiotin(float biotin) {
        this.biotin = biotin;
    }

    public float getPantothenicAcid() {
        return pantothenicAcid;
    }

    public void setPantothenicAcid(float pantothenicAcid) {
        this.pantothenicAcid = pantothenicAcid;
    }
}
