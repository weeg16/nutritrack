package com.mobicom.s18.nutritrack_mco;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MacroResultActivity extends AppCompatActivity {

    private TextView caloriesText, proteinText, carbsText, fatsText;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_result);

        caloriesText = findViewById(R.id.caloriesText);
        proteinText = findViewById(R.id.proteinText);
        backButton = findViewById(R.id.backButton);
        carbsText = findViewById(R.id.carbsText);
        fatsText = findViewById(R.id.fatsText);

        TextView goalTypeText = findViewById(R.id.goalTypeText);
        TextView activityLevelText = findViewById(R.id.activityLevelText);

        SessionManager sessionManager = new SessionManager(this);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        String email = sessionManager.getUserEmail();
        if (email != null) {
            UserGoal goal = dbHelper.getUserGoal(email);
            if (goal != null) {
                caloriesText.setText("Calories: " + goal.getCalorieGoal() + " kcal");
                proteinText.setText("Protein: " + goal.getProteinGoal() + "g");
                carbsText.setText("Carbohydrates: " + goal.getCarbsGoal() + "g");
                fatsText.setText("Fats: " + goal.getFatsGoal() + "g");
                goalTypeText.setText("Goal: " + goal.getGoalType());
                activityLevelText.setText("Activity Level: " + goal.getActivityLevel());
            }
        }
        backButton.setOnClickListener(v -> onBackPressed());
    }
}