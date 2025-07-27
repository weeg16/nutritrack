package com.mobicom.s18.nutritrack_mco;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class WeeklySummaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeeklySummaryAdapter adapter;
    private List<DailySummary> summaryList;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_summary);

        recyclerView = findViewById(R.id.summaryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        String email = sessionManager.getUserEmail();

        summaryList = getDailySummaries(email);
        adapter = new WeeklySummaryAdapter(summaryList);
        recyclerView.setAdapter(adapter);
    }

    private List<DailySummary> getDailySummaries(String email) {
        List<DailySummary> summaries = new ArrayList<>();

        Cursor cursor = dbHelper.getMealLogsForLast7Days(email);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("log_date"));
                double calories = cursor.getDouble(cursor.getColumnIndexOrThrow("total_calories"));
                double protein = cursor.getDouble(cursor.getColumnIndexOrThrow("total_protein"));
                double carbs = cursor.getDouble(cursor.getColumnIndexOrThrow("total_carbs"));
                double fats = cursor.getDouble(cursor.getColumnIndexOrThrow("total_fats"));

                DailySummary summary = new DailySummary(date, calories, protein, carbs, fats);
                summaries.add(summary);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return summaries;
    }
}
