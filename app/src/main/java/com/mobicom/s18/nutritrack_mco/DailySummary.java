package com.mobicom.s18.nutritrack_mco;

public class DailySummary {
    private String date;
    private double totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFats;

    public DailySummary(String date, double totalCalories, double totalProtein, double totalCarbs, double totalFats) {
        this.date = date;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFats = totalFats;
    }

    public String getDate() {
        return date;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public double getTotalCarbs() {
        return totalCarbs;
    }

    public double getTotalFats() {
        return totalFats;
    }
}
