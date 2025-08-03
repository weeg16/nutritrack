package com.mobicom.s18.nutritrack_mco;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MacroResultActivity extends AppCompatActivity {

    private TextView caloriesText, proteinText, carbsText, fatsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_result);

        caloriesText = findViewById(R.id.caloriesText);
        proteinText = findViewById(R.id.proteinText);
        carbsText = findViewById(R.id.carbsText);
        fatsText = findViewById(R.id.fatsText);

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
            }
        }
    }
}
