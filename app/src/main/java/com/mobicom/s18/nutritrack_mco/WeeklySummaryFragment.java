package com.mobicom.s18.nutritrack_mco;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class WeeklySummaryFragment extends Fragment {

    public WeeklySummaryFragment() {
        super(R.layout.fragment_weekly_summary);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView totalCaloriesText = view.findViewById(R.id.totalCaloriesText);
        TextView totalProteinText = view.findViewById(R.id.totalProteinText);
        TextView totalCarbsText = view.findViewById(R.id.totalCarbsText);
        TextView totalFatsText = view.findViewById(R.id.totalFatsText);
        Button detailedStatsBtn = view.findViewById(R.id.detailedStatsBtn);

        SessionManager sessionManager = new SessionManager(requireContext());
        String email = sessionManager.getUserEmail();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        Cursor cursor = dbHelper.getMealLogsForLast7Days(email);

        double totalCalories = 0;
        double totalProtein = 0;
        double totalCarbs = 0;
        double totalFats = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                totalCalories += cursor.getDouble(cursor.getColumnIndexOrThrow("total_calories"));
                totalProtein += cursor.getDouble(cursor.getColumnIndexOrThrow("total_protein"));
                totalCarbs += cursor.getDouble(cursor.getColumnIndexOrThrow("total_carbs"));
                totalFats += cursor.getDouble(cursor.getColumnIndexOrThrow("total_fats"));
            } while (cursor.moveToNext());
            cursor.close();
        }

        totalCaloriesText.setText(String.format(Locale.getDefault(), "%.0f kcal", totalCalories));
        totalProteinText.setText(String.format(Locale.getDefault(), "%.0f g", totalProtein));
        totalCarbsText.setText(String.format(Locale.getDefault(), "%.0f g", totalCarbs));
        totalFatsText.setText(String.format(Locale.getDefault(), "%.0f g", totalFats));

        detailedStatsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WeeklySummaryActivity.class);
            startActivity(intent);
        });
    }
}
