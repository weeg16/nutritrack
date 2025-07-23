package com.mobicom.s18.nutritrack_mco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealLogAdapter extends RecyclerView.Adapter<MealLogAdapter.MealLogViewHolder> {
    private final List<MealLog> mealLogs;

    public MealLogAdapter(List<MealLog> mealLogs) {
        this.mealLogs = mealLogs;
    }

    @NonNull
    @Override
    public MealLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_log, parent, false);
        return new MealLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealLogViewHolder holder, int position) {
        MealLog log = mealLogs.get(position);
        holder.mealNameTv.setText("Meal: " + log.mealName);
        holder.caloriesTv.setText("Calories: " + log.calories + " kcal");
        holder.macrosTv.setText("Protein: " + log.protein + "g, Carbs: " + log.carbs + "g, Fats: " + log.fats + "g");
        holder.dateTv.setText("Date: " + log.date);
    }

    @Override
    public int getItemCount() {
        return mealLogs.size();
    }

    static class MealLogViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTv, caloriesTv, macrosTv, dateTv;

        public MealLogViewHolder(@NonNull View itemView) {
            super(itemView);
            mealNameTv = itemView.findViewById(R.id.mealNameTv);
            caloriesTv = itemView.findViewById(R.id.caloriesTv);
            macrosTv = itemView.findViewById(R.id.macrosTv);
            dateTv = itemView.findViewById(R.id.dateTv);
        }
    }
}
