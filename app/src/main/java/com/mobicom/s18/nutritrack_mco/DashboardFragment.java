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

        TextView calorieCountTv = view.findViewById(R.id.calorieCounterValue);
        calorieCountTv.setText("100000 kcal");


        Button foodSearchBtn = view.findViewById(R.id.foodSearchBtn);
        Button updateGoalBtn = view.findViewById(R.id.updateGoalBtn);
        Button manageLogsBtn = view.findViewById(R.id.manageLogsBtn);

        foodSearchBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), FoodSearchActivity.class)));
        updateGoalBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), UpdateGoalActivity.class)));
        manageLogsBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ManageLogsActivity.class)));
    }
}