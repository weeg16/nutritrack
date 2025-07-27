package com.mobicom.s18.nutritrack_mco;

public class MealLog {
    public int id;
    public String mealName, date;
    public double calories, protein, carbs, fats;

    public MealLog(int id, String mealName, double calories, double protein, double carbs, double fats, String date) {
        this.id = id;
        this.mealName = mealName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.date = date;
    }
}

