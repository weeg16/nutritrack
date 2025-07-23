package com.mobicom.s18.nutritrack_mco;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogMealFragment extends Fragment {

    private EditText mealNameEt, caloriesEt, proteinEt, carbsEt, fatsEt;
    private Button saveLogBtn, viewMealLogBtn;
    private DatabaseHelper dbHelper;

    public LogMealFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_meal, container, false);

        // Bind views
        mealNameEt = view.findViewById(R.id.mealNameEt);
        caloriesEt = view.findViewById(R.id.caloriesEt);
        proteinEt = view.findViewById(R.id.proteinEt);
        carbsEt = view.findViewById(R.id.carbsEt);
        fatsEt = view.findViewById(R.id.fatsEt);
        saveLogBtn = view.findViewById(R.id.saveLogBtn);
        viewMealLogBtn = view.findViewById(R.id.viewMealLogBtn);

        dbHelper = new DatabaseHelper(requireContext());

        saveLogBtn.setOnClickListener(v -> {
            String mealName = mealNameEt.getText().toString().trim();
            String calStr = caloriesEt.getText().toString().trim();
            String proteinStr = proteinEt.getText().toString().trim();
            String carbsStr = carbsEt.getText().toString().trim();
            String fatsStr = fatsEt.getText().toString().trim();

            if (mealName.isEmpty() || calStr.isEmpty() || proteinStr.isEmpty() ||
                    carbsStr.isEmpty() || fatsStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double calories = Double.parseDouble(calStr);
            double protein = Double.parseDouble(proteinStr);
            double carbs = Double.parseDouble(carbsStr);
            double fats = Double.parseDouble(fatsStr);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            SessionManager sessionManager = new SessionManager(requireContext());
            String userEmail = sessionManager.getUserEmail();

            boolean inserted = dbHelper.insertMealLog(userEmail, mealName, calories, protein, carbs, fats, date);

            if (inserted) {
                Toast.makeText(getContext(), "Meal logged successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(getContext(), "Failed to log meal", Toast.LENGTH_SHORT).show();
            }
        });

        viewMealLogBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LogMealActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void clearFields() {
        mealNameEt.setText("");
        caloriesEt.setText("");
        proteinEt.setText("");
        carbsEt.setText("");
        fatsEt.setText("");
    }
}