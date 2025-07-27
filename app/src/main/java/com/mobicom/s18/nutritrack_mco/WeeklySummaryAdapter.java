package com.mobicom.s18.nutritrack_mco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeeklySummaryAdapter extends RecyclerView.Adapter<WeeklySummaryAdapter.ViewHolder> {
    private final List<DailySummary> summaryList;

    public WeeklySummaryAdapter(List<DailySummary> summaryList) {
        this.summaryList = summaryList;
    }

    @NonNull
    @Override
    public WeeklySummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklySummaryAdapter.ViewHolder holder, int position) {
        DailySummary summary = summaryList.get(position);
        holder.dateTv.setText("Date: " + summary.date);
        holder.caloriesTv.setText("Calories: " + summary.totalCalories + " kcal");
        holder.macrosTv.setText("Protein: " + summary.totalProtein + "g | Carbs: " + summary.totalCarbs + "g | Fats: " + summary.totalFats + "g");
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv, caloriesTv, macrosTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.dateTv);
            caloriesTv = itemView.findViewById(R.id.caloriesTv);
            macrosTv = itemView.findViewById(R.id.macrosTv);
        }
    }
}
