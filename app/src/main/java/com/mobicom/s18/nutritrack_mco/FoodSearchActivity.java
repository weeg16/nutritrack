package com.mobicom.s18.nutritrack_mco;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FoodSearchActivity extends AppCompatActivity {

    private EditText foodSearchInput;
    private ImageButton searchBtn;
    private RecyclerView foodSearchResultsRecycler;
    private FoodSearchAdapter adapter;

    private RequestQueue requestQueue;
    private static final String API_KEY = "4JHkzKAEAcOWMDCJHHjivLR5Heo0TC1un5yVQpaf";

    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        foodSearchInput = findViewById(R.id.foodSearchInput);
        searchBtn = findViewById(R.id.searchBtn);
        foodSearchResultsRecycler = findViewById(R.id.foodSearchResultsRecycler);

        sessionManager = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);
        requestQueue = Volley.newRequestQueue(this);

        adapter = new FoodSearchAdapter(this, new ArrayList<>());
        foodSearchResultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        foodSearchResultsRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(this::onFoodSelected);

        searchBtn.setOnClickListener(v -> {
            String query = foodSearchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                searchFood(query);
            }
        });
    }

    private void searchFood(String query) {
        String url = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=" + API_KEY + "&query=" + query;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray foods = response.getJSONArray("foods");
                        ArrayList<FoodItem> results = new ArrayList<>();

                        for (int i = 0; i < foods.length(); i++) {
                            JSONObject item = foods.getJSONObject(i);
                            String description = item.getString("description");
                            JSONArray nutrients = item.getJSONArray("foodNutrients");

                            double calories = 0, protein = 0, carbs = 0, fats = 0;
                            for (int j = 0; j < nutrients.length(); j++) {
                                JSONObject nutrient = nutrients.getJSONObject(j);
                                String name = nutrient.getString("nutrientName");
                                double amount = nutrient.optDouble("value", 0);

                                if (name.contains("Energy")) calories = amount;
                                else if (name.contains("Protein")) protein = amount;
                                else if (name.contains("Carbohydrate")) carbs = amount;
                                else if (name.contains("Total lipid")) fats = amount;
                            }

                            results.add(new FoodItem(description, calories, protein, carbs, fats));
                        }

                        adapter.updateList(results);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse results", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "API error", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }

    private void onFoodSelected(FoodItem food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter grams consumed for " + food.getName());

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("e.g. 150");
        builder.setView(input);

        builder.setPositiveButton("Log", (dialog, which) -> {
            try {
                double grams = Double.parseDouble(input.getText().toString());
                double factor = grams / 100.0;

                String email = sessionManager.getUserEmail();
                if (email == null) {
                    Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show();
                    return;
                }

                String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                boolean inserted = dbHelper.insertMealLog(
                        email,
                        food.getName(),
                        food.getCalories() * factor,
                        food.getProtein() * factor,
                        food.getCarbs() * factor,
                        food.getFats() * factor,
                        today
                );

                if (inserted) {
                    Toast.makeText(this, "Food logged!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to log food.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
