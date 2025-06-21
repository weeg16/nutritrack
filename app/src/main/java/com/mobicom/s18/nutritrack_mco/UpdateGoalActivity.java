package com.mobicom.s18.nutritrack_mco;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateGoalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goal);

        Button saveUpdatedGoalBtn = findViewById(R.id.saveUpdatedGoalBtn);
        saveUpdatedGoalBtn.setOnClickListener(v -> {

            Intent intent = new Intent(UpdateGoalActivity.this, MacroResultActivity.class);
            startActivity(intent);
        });

    }
}