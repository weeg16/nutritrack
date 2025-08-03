package com.mobicom.s18.nutritrack_mco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {
    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGoalDisplay();
    }

    private void updateGoalDisplay() {
        TextView calorieCountTv = requireView().findViewById(R.id.calorieCounterValue);
        TextView weightValueTv = requireView().findViewById(R.id.weightValue);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SessionManager sessionManager = new SessionManager(getContext());
        String email = sessionManager.getUserEmail();

        double consumed = dbHelper.getTodaysCalories(email);
        int goal = dbHelper.getUserCalorieGoal(email);
        double weight = dbHelper.getUserWeight(email);

        if (goal > 0) {
            calorieCountTv.setText(String.format("%.0f kcal / %d kcal", consumed, goal));
        } else {
            calorieCountTv.setText(String.format("%.0f kcal", consumed));
        }

        if (weight > 0) {
            weightValueTv.setText(String.format("%.1f kg", weight));
        } else {
            weightValueTv.setText("—");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateGoalDisplay();

        Button foodSearchBtn = view.findViewById(R.id.foodSearchBtn);
        Button updateGoalBtn = view.findViewById(R.id.updateGoalBtn);
        Button manageLogsBtn = view.findViewById(R.id.manageLogsBtn);
        Button chartSummaryBtn = view.findViewById(R.id.chartSummaryBtn);

        foodSearchBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), FoodSearchActivity.class)));
        updateGoalBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), UpdateGoalActivity.class)));
        manageLogsBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ManageLogsActivity.class)));
        chartSummaryBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChartSummaryActivity.class)));
    }
}
