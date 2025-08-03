package com.mobicom.s18.nutritrack_mco;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateGoalActivity extends AppCompatActivity {

    private EditText updateWeightEt, updateCaloriesEt, updateProteinEt, updateCarbsEt, updateFatsEt;
    private Button saveUpdatedGoalBtn, viewGoalBtn;
    private ImageView backButton;
    private Spinner goalSpinner, activityLevelSpinner;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goal);

        backButton = findViewById(R.id.backButton);
        updateWeightEt = findViewById(R.id.updateWeightEt);
        updateCaloriesEt = findViewById(R.id.updateCaloriesEt);
        updateProteinEt = findViewById(R.id.updateProteinEt);
        updateCarbsEt = findViewById(R.id.updateCarbsEt);
        updateFatsEt = findViewById(R.id.updateFatsEt);
        saveUpdatedGoalBtn = findViewById(R.id.saveUpdatedGoalBtn);
        viewGoalBtn = findViewById(R.id.viewGoalBtn);
        goalSpinner = findViewById(R.id.goalSpinner);
        activityLevelSpinner = findViewById(R.id.activityLevelSpinner);

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(
                this, R.array.goal_options, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapter);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(
                this, R.array.activity_level_options, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevelSpinner.setAdapter(activityAdapter);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        saveUpdatedGoalBtn.setOnClickListener(v -> {
            String email = sessionManager.getUserEmail();

            if (email == null || email.trim().isEmpty()) {
                Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String weightStr = updateWeightEt.getText().toString().trim();
                String calorieStr = updateCaloriesEt.getText().toString().trim();
                String proteinStr = updateProteinEt.getText().toString().trim();
                String carbStr = updateCarbsEt.getText().toString().trim();
                String fatStr = updateFatsEt.getText().toString().trim();

                if (weightStr.isEmpty() || calorieStr.isEmpty() || proteinStr.isEmpty() ||
                        carbStr.isEmpty() || fatStr.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double weight = Double.parseDouble(weightStr);
                int calorieGoal = Integer.parseInt(calorieStr);
                int proteinGoal = Integer.parseInt(proteinStr);
                int carbGoal = Integer.parseInt(carbStr);
                int fatGoal = Integer.parseInt(fatStr);

                String selectedGoal = goalSpinner.getSelectedItem().toString();
                String selectedActivityLevel = activityLevelSpinner.getSelectedItem().toString();

                boolean saved = dbHelper.setUserGoal(email, calorieGoal, proteinGoal, carbGoal, fatGoal, weight, selectedGoal,selectedActivityLevel );

                if (saved) {
                    Toast.makeText(this, "Goals updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update goals. Please try again.", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number input. Please check your values.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        viewGoalBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MacroResultActivity.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(v -> onBackPressed());

    }
}