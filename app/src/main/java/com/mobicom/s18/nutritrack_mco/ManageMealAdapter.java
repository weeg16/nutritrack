package com.mobicom.s18.nutritrack_mco;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageMealAdapter extends RecyclerView.Adapter<ManageMealAdapter.MealViewHolder> {

    private List<MealLog> mealLogs;
    private Context context;
    private DatabaseHelper dbHelper;

    public ManageMealAdapter(Context context, List<MealLog> mealLogs) {
        this.context = context;
        this.mealLogs = mealLogs;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal_log, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealLog log = mealLogs.get(position);
        holder.mealNameTv.setText("Meal: " + log.mealName);
        holder.caloriesTv.setText("Calories: " + log.calories);
        holder.macrosTv.setText("Protein: " + log.protein + "g, Carbs: " + log.carbs + "g, Fats: " + log.fats + "g");
        holder.dateTv.setText("Date: " + log.date);

        holder.itemView.setOnLongClickListener(v -> {
            showOptionsDialog(position, log);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mealLogs.size();
    }

    private void showOptionsDialog(int position, MealLog log) {
        new AlertDialog.Builder(context)
                .setTitle("Manage Meal Log")
                .setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        showEditDialog(position, log);
                    } else {
                        deleteMeal(position, log);
                    }
                })
                .show();
    }

    private void showEditDialog(int position, MealLog log) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_meal, null);
        EditText mealNameEt = dialogView.findViewById(R.id.editMealNameEt);
        EditText calEt = dialogView.findViewById(R.id.editCaloriesEt);
        EditText proteinEt = dialogView.findViewById(R.id.editProteinEt);
        EditText carbsEt = dialogView.findViewById(R.id.editCarbsEt);
        EditText fatsEt = dialogView.findViewById(R.id.editFatsEt);

        // Pre-fill current values
        mealNameEt.setText(log.mealName);
        calEt.setText(String.valueOf(log.calories));
        proteinEt.setText(String.valueOf(log.protein));
        carbsEt.setText(String.valueOf(log.carbs));
        fatsEt.setText(String.valueOf(log.fats));

        new AlertDialog.Builder(context)
                .setTitle("Edit Meal Log")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String updatedName = mealNameEt.getText().toString().trim();
                    double updatedCal = Double.parseDouble(calEt.getText().toString());
                    double updatedProtein = Double.parseDouble(proteinEt.getText().toString());
                    double updatedCarbs = Double.parseDouble(carbsEt.getText().toString());
                    double updatedFats = Double.parseDouble(fatsEt.getText().toString());

                    boolean success = dbHelper.updateMealLog(
                            log.id, updatedName, updatedCal, updatedProtein, updatedCarbs, updatedFats);

                    if (success) {
                        log.mealName = updatedName;
                        log.calories = updatedCal;
                        log.protein = updatedProtein;
                        log.carbs = updatedCarbs;
                        log.fats = updatedFats;
                        notifyItemChanged(position);
                        Toast.makeText(context, "Meal updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteMeal(int position, MealLog log) {
        boolean deleted = dbHelper.deleteMealLog(log.id);
        if (deleted) {
            mealLogs.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Meal deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTv, caloriesTv, macrosTv, dateTv;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealNameTv = itemView.findViewById(R.id.mealNameTv);
            caloriesTv = itemView.findViewById(R.id.caloriesTv);
            macrosTv = itemView.findViewById(R.id.macrosTv);
            dateTv = itemView.findViewById(R.id.dateTv);
        }
    }
}
