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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager sessionManager = new SessionManager(getContext());
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        TextView calorieCountTv = view.findViewById(R.id.calorieCounterValue);
        String email = sessionManager.getUserEmail();

        if (email != null) {
            double totalCaloriesToday = dbHelper.getTodaysCalories(email);
            calorieCountTv.setText(String.format("%.0f kcal", totalCaloriesToday));
        } else {
            calorieCountTv.setText("0 kcal");
        }

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
