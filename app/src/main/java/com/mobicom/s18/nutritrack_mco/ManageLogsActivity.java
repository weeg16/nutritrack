package com.mobicom.s18.nutritrack_mco;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class ManageLogsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView backButton;
    private ManageMealLogAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private List<Object> groupedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_logs);

        backButton = findViewById(R.id.backButton);

        recyclerView = findViewById(R.id.mealLogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        loadData();
        backButton.setOnClickListener(v -> onBackPressed());

    }

    private void loadData() {
        groupedItems = new ArrayList<>();
        String email = session.getUserEmail();
        Cursor cursor = dbHelper.getAllMealLogs(email);

        Map<String, List<MealLog>> groupedByDate = new LinkedHashMap<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("meal_name"));
                double cal = cursor.getDouble(cursor.getColumnIndexOrThrow("calories"));
                double protein = cursor.getDouble(cursor.getColumnIndexOrThrow("protein"));
                double carbs = cursor.getDouble(cursor.getColumnIndexOrThrow("carbs"));
                double fats = cursor.getDouble(cursor.getColumnIndexOrThrow("fats"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("log_date"));

                MealLog log = new MealLog(id, name, cal, protein, carbs, fats, date);
                if (!groupedByDate.containsKey(date)) {
                    groupedByDate.put(date, new ArrayList<>());
                }
                groupedByDate.get(date).add(log);
            } while (cursor.moveToNext());
            cursor.close();
        }

        for (String date : groupedByDate.keySet()) {
            groupedItems.add(date);
            groupedItems.addAll(groupedByDate.get(date));
        }

        adapter = new ManageMealLogAdapter(this, groupedItems, this::loadData);
        recyclerView.setAdapter(adapter);
    }
}
