package com.mobicom.s18.nutritrack_mco;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChartSummaryActivity extends AppCompatActivity {

    private TextView startDateTv, endDateTv, proteinSummaryTv, carbsSummaryTv, fatsSummaryTv;
    private Switch switchMacroMode;
    private BarChart barChart, macroBarChart;
    private PieChart pieChart;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    private String selectedStartDate, selectedEndDate;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Typeface montserrat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_summary);

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        macroBarChart = findViewById(R.id.macroBarChart);
        startDateTv = findViewById(R.id.startDateTv);
        endDateTv = findViewById(R.id.endDateTv);
        switchMacroMode = findViewById(R.id.switchMacroMode);

        proteinSummaryTv = findViewById(R.id.proteinSummaryTv);
        carbsSummaryTv = findViewById(R.id.carbsSummaryTv);
        fatsSummaryTv = findViewById(R.id.fatsSummaryTv);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        montserrat = ResourcesCompat.getFont(this, R.font.montserrat_medium);

        Calendar cal = Calendar.getInstance();
        selectedEndDate = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, -6);
        selectedStartDate = sdf.format(cal.getTime());

        startDateTv.setText(selectedStartDate);
        endDateTv.setText(selectedEndDate);

        startDateTv.setOnClickListener(v -> showDatePicker(true));
        endDateTv.setOnClickListener(v -> showDatePicker(false));

        switchMacroMode.setOnCheckedChangeListener((buttonView, isChecked) -> updateCharts());

        updateCharts();
    }

    private void showDatePicker(boolean isStart) {
        Calendar cal = Calendar.getInstance();
        try {
            Date date = sdf.parse(isStart ? selectedStartDate : selectedEndDate);
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            String pickedDate = sdf.format(selected.getTime());

            if (isStart) {
                selectedStartDate = pickedDate;
                startDateTv.setText(pickedDate);
            } else {
                selectedEndDate = pickedDate;
                endDateTv.setText(pickedDate);
            }
            updateCharts();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateCharts() {
        String email = sessionManager.getUserEmail();
        if (email == null) return;

        ArrayList<DailySummary> summaries = new ArrayList<>(dbHelper.getMealLogsBetweenDates(email, selectedStartDate, selectedEndDate));
        if (summaries.isEmpty()) {
            Toast.makeText(this, "No data found in this range", Toast.LENGTH_SHORT).show();
        }

        setupCalorieBarChart(summaries);

        if (switchMacroMode.isChecked()) {
            pieChart.setVisibility(PieChart.GONE);
            macroBarChart.setVisibility(BarChart.VISIBLE);
            setupMacronutrientBarChart(summaries);
        } else {
            pieChart.setVisibility(PieChart.VISIBLE);
            macroBarChart.setVisibility(BarChart.GONE);
            setupMacronutrientPieChart(summaries);
        }

        // Update current vs goal macros
        double totalProtein = 0, totalCarbs = 0, totalFats = 0;
        for (DailySummary s : summaries) {
            totalProtein += s.getTotalProtein();
            totalCarbs += s.getTotalCarbs();
            totalFats += s.getTotalFats();
        }

        int goalProtein = dbHelper.getUserProteinGoal(email);
        int goalCarbs = dbHelper.getUserCarbsGoal(email);
        int goalFats = dbHelper.getUserFatsGoal(email);

        proteinSummaryTv.setText(String.format("Protein: %.0f g / %d g", totalProtein, goalProtein));
        carbsSummaryTv.setText(String.format("Carbs: %.0f g / %d g", totalCarbs, goalCarbs));
        fatsSummaryTv.setText(String.format("Fats: %.0f g / %d g", totalFats, goalFats));
    }

    private void setupCalorieBarChart(ArrayList<DailySummary> summaries) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < summaries.size(); i++) {
            DailySummary s = summaries.get(i);
            entries.add(new BarEntry(i, (float) s.getTotalCalories()));
            labels.add(s.getDate());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Calories per Day");
        dataSet.setColor(getResources().getColor(R.color.teal_700, getTheme()));
        BarData barData = new BarData(dataSet);
        barData.setValueTypeface(montserrat);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setTypeface(montserrat);

        barChart.getAxisLeft().setTypeface(montserrat);
        barChart.getAxisRight().setTypeface(montserrat);
        barChart.getLegend().setTypeface(montserrat);
        barChart.invalidate();
    }

    private void setupMacronutrientPieChart(ArrayList<DailySummary> summaries) {
        float protein = 0f, carbs = 0f, fats = 0f;
        for (DailySummary s : summaries) {
            protein += s.getTotalProtein();
            carbs += s.getTotalCarbs();
            fats += s.getTotalFats();
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (protein > 0) entries.add(new PieEntry(protein, "Protein"));
        if (carbs > 0) entries.add(new PieEntry(carbs, "Carbs"));
        if (fats > 0) entries.add(new PieEntry(fats, "Fats"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                getColor(R.color.protein_color),
                getColor(R.color.carb_color),
                getColor(R.color.fat_color)
        );

        PieData pieData = new PieData(dataSet);
        pieData.setValueTypeface(montserrat);
        pieData.setValueTextSize(14f);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTypeface(montserrat);
        pieChart.setCenterText("Total Macronutrients");
        pieChart.setCenterTextTypeface(montserrat);
        pieChart.setCenterTextSize(16f);
        pieChart.getLegend().setTypeface(montserrat);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    private void setupMacronutrientBarChart(ArrayList<DailySummary> summaries) {
        ArrayList<BarEntry> proteinEntries = new ArrayList<>();
        ArrayList<BarEntry> carbsEntries = new ArrayList<>();
        ArrayList<BarEntry> fatsEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < summaries.size(); i++) {
            DailySummary s = summaries.get(i);
            proteinEntries.add(new BarEntry(i, (float) s.getTotalProtein()));
            carbsEntries.add(new BarEntry(i, (float) s.getTotalCarbs()));
            fatsEntries.add(new BarEntry(i, (float) s.getTotalFats()));
            labels.add(s.getDate());
        }

        BarDataSet proteinSet = new BarDataSet(proteinEntries, "Protein");
        BarDataSet carbsSet = new BarDataSet(carbsEntries, "Carbs");
        BarDataSet fatsSet = new BarDataSet(fatsEntries, "Fats");

        proteinSet.setColor(getColor(R.color.protein_color));
        carbsSet.setColor(getColor(R.color.carb_color));
        fatsSet.setColor(getColor(R.color.fat_color));

        BarData data = new BarData(proteinSet, carbsSet, fatsSet);
        float groupSpace = 0.2f;
        float barSpace = 0.05f;
        float barWidth = 0.25f;

        data.setBarWidth(barWidth);
        data.setValueTypeface(montserrat);

        macroBarChart.setData(data);
        macroBarChart.groupBars(0f, groupSpace, barSpace);
        macroBarChart.setFitBars(true);
        macroBarChart.getDescription().setEnabled(false);

        XAxis xAxis = macroBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setTypeface(montserrat);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(summaries.size());

        macroBarChart.getAxisLeft().setTypeface(montserrat);
        macroBarChart.getAxisRight().setTypeface(montserrat);
        macroBarChart.getLegend().setTypeface(montserrat);
        macroBarChart.invalidate();
    }
}
