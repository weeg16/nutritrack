package com.mobicom.s18.nutritrack_mco;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageLogsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManageMealAdapter adapter;
    private List<MealLog> mealLogs;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_logs);

        recyclerView = findViewById(R.id.mealLogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        String email = sessionManager.getUserEmail();

        loadMealLogs(email);
    }

    private void loadMealLogs(String email) {
        mealLogs = new ArrayList<>();
        Cursor cursor = dbHelper.getAllMealLogs(email);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("meal_name"));
                double cal = cursor.getDouble(cursor.getColumnIndexOrThrow("calories"));
                double protein = cursor.getDouble(cursor.getColumnIndexOrThrow("protein"));
                double carbs = cursor.getDouble(cursor.getColumnIndexOrThrow("carbs"));
                double fats = cursor.getDouble(cursor.getColumnIndexOrThrow("fats"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("log_date"));

                mealLogs.add(new MealLog(id, name, cal, protein, carbs, fats, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new ManageMealAdapter(this, mealLogs);
        recyclerView.setAdapter(adapter);
    }
}
