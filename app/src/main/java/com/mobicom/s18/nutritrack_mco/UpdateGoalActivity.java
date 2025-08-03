package com.mobicom.s18.nutritrack_mco;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateGoalActivity extends AppCompatActivity {

    private EditText updateWeightEt, updateCaloriesEt, updateProteinEt, updateCarbsEt, updateFatsEt;
    private Button saveUpdatedGoalBtn, viewGoalBtn;
    private ImageView backButton;
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

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        saveUpdatedGoalBtn.setOnClickListener(v -> {
            String email = sessionManager.getUserEmail();
            if (email == null) {
                Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double weight = Double.parseDouble(updateWeightEt.getText().toString().trim());
                int calorieGoal = Integer.parseInt(updateCaloriesEt.getText().toString().trim());
                int proteinGoal = Integer.parseInt(updateProteinEt.getText().toString().trim());
                int carbGoal = Integer.parseInt(updateCarbsEt.getText().toString().trim());
                int fatGoal = Integer.parseInt(updateFatsEt.getText().toString().trim());

                boolean saved = dbHelper.setUserGoal(email, calorieGoal, proteinGoal, carbGoal, fatGoal, weight);

                if (saved) {
                    Toast.makeText(this, "Goals updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update goals.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show();
            }
        });

        viewGoalBtn.setOnClickListener(v ->
                startActivity(new Intent(this, MacroResultActivity.class))
        );
        backButton.setOnClickListener(v -> onBackPressed());

    }
}
