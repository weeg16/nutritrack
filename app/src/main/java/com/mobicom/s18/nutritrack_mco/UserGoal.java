package com.mobicom.s18.nutritrack_mco;

public class UserGoal {
    private int calorieGoal;
    private int proteinGoal;
    private int carbsGoal;
    private int fatsGoal;
    private double weight;
    String goalType;
    String activityLevel;

    public UserGoal(int calorieGoal, int proteinGoal, int carbsGoal, int fatsGoal, double weight,  String goalType, String activityLevel) {
        this.calorieGoal = calorieGoal;
        this.proteinGoal = proteinGoal;
        this.carbsGoal = carbsGoal;
        this.fatsGoal = fatsGoal;
        this.weight = weight;
        this.goalType = goalType;
        this.activityLevel = activityLevel;
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

    public String getGoalType() {
        return goalType;
    }

    public String getActivityLevel() {
        return activityLevel;
    }


    public double getWeight() {
        return weight;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public void setProteinGoal(int proteinGoal) {
        this.proteinGoal = proteinGoal;
    }

    public void setCarbsGoal(int carbsGoal) {
        this.carbsGoal = carbsGoal;
    }

    public void setFatsGoal(int fatsGoal) {
        this.fatsGoal = fatsGoal;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}