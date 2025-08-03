package com.mobicom.s18.nutritrack_mco;

public class UserGoal {
    private int calorieGoal;
    private int proteinGoal;
    private int carbsGoal;
    private int fatsGoal;
    private double weight;

    public UserGoal(int calorieGoal, int proteinGoal, int carbsGoal, int fatsGoal, double weight) {
        this.calorieGoal = calorieGoal;
        this.proteinGoal = proteinGoal;
        this.carbsGoal = carbsGoal;
        this.fatsGoal = fatsGoal;
        this.weight = weight;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public int getProteinGoal() {
        return proteinGoal;
    }

    public int getCarbsGoal() {
        return carbsGoal;
    }

    public int getFatsGoal() {
        return fatsGoal;
    }

    public double getWeight() {
        return weight;
    }
}
