package com.mobicom.s18.nutritrack_mco;

public class DailySummary {
    public String date;
    public double totalCalories;
    public double totalProtein;
    public double totalCarbs;
    public double totalFats;

    public DailySummary(String date, double totalCalories, double totalProtein, double totalCarbs, double totalFats) {
        this.date = date;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFats = totalFats;
    }
}
