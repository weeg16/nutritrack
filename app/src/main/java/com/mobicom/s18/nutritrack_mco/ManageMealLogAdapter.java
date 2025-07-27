package com.mobicom.s18.nutritrack_mco;

import android.app.AlertDialog;
import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageMealLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Object> items;
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final Runnable refreshCallback;

    public ManageMealLogAdapter(Context context, List<Object> items, Runnable refreshCallback) {
        this.context = context;
        this.items = items;
        this.dbHelper = new DatabaseHelper(context);
        this.refreshCallback = refreshCallback;
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof String) ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_date_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_manage_meal, parent, false);
            return new MealViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).dateText.setText((String) items.get(position));
        } else {
            MealLog log = (MealLog) items.get(position);
            MealViewHolder vh = (MealViewHolder) holder;
            vh.mealName.setText(log.mealName);
            vh.macros.setText(String.format("C: %.0f | P: %.0f | F: %.0f", log.carbs, log.protein, log.fats));
            vh.calories.setText(String.format("Calories: %.0f", log.calories));

            vh.editBtn.setOnClickListener(v -> showEditDialog(log));
            vh.deleteBtn.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Meal")
                        .setMessage("Are you sure you want to delete this meal?")
                        .setPositiveButton("Yes", (d, w) -> {
                            dbHelper.deleteMealLog(log.id);
                            refreshCallback.run();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }
    }

    private void showEditDialog(MealLog log) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_meal, null);
        EditText name = dialogView.findViewById(R.id.editMealNameEt);
        EditText cal = dialogView.findViewById(R.id.editCaloriesEt);
        EditText protein = dialogView.findViewById(R.id.editProteinEt);
        EditText carbs = dialogView.findViewById(R.id.editCarbsEt);
        EditText fats = dialogView.findViewById(R.id.editFatsEt);

        name.setText(log.mealName);
        cal.setText(String.valueOf(log.calories));
        protein.setText(String.valueOf(log.protein));
        carbs.setText(String.valueOf(log.carbs));
        fats.setText(String.valueOf(log.fats));

        new AlertDialog.Builder(context)
                .setTitle("Edit Meal")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    dbHelper.updateMealLog(
                            log.id,
                            name.getText().toString(),
                            Double.parseDouble(cal.getText().toString()),
                            Double.parseDouble(protein.getText().toString()),
                            Double.parseDouble(carbs.getText().toString()),
                            Double.parseDouble(fats.getText().toString())
                    );
                    refreshCallback.run();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.headerDateTv);
        }
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName, calories, macros;
        Button editBtn, deleteBtn;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealNameTv);
            calories = itemView.findViewById(R.id.caloriesTv);
            macros = itemView.findViewById(R.id.macrosTv);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
