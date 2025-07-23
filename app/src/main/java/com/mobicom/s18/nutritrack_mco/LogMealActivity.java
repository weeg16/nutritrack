package com.mobicom.s18.nutritrack_mco;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LogMealActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    SessionManager session;
    private List<MealLog> mealLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_meal);

        recyclerView = findViewById(R.id.mealLogRecyclerView);
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);
        mealLogs = new ArrayList<>();

        String email = session.getUserEmail();
        Cursor cursor = dbHelper.getAllMealLogs(email);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("meal_name"));
                double cal = cursor.getDouble(cursor.getColumnIndexOrThrow("calories"));
                double protein = cursor.getDouble(cursor.getColumnIndexOrThrow("protein"));
                double carbs = cursor.getDouble(cursor.getColumnIndexOrThrow("carbs"));
                double fats = cursor.getDouble(cursor.getColumnIndexOrThrow("fats"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("log_date"));

                mealLogs.add(new MealLog(name, cal, protein, carbs, fats, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MealLogAdapter(mealLogs));
    }
}
