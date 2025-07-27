package com.mobicom.s18.nutritrack_mco;

public class FoodItem {
    private final String name;
    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fats;

    public FoodItem(String name, double calories, double protein, double carbs, double fats) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFats() {
        return fats;
    }

    public String getDescription() {
        return String.format("%s\n%.1f kcal, %.1fg P / %.1fg C / %.1fg F",
                name, calories, protein, carbs, fats);
    }
}
