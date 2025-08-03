package com.mobicom.s18.nutritrack_mco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NutriTrack.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable =
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "email TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL)";
        db.execSQL(createUserTable);

        String createMealLogTable = "CREATE TABLE meal_logs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT NOT NULL, " +
                "meal_name TEXT NOT NULL, " +
                "calories REAL, " +
                "protein REAL, " +
                "carbs REAL, " +
                "fats REAL, " +
                "log_date TEXT NOT NULL)";
        db.execSQL(createMealLogTable);

        String createGoalTable = "CREATE TABLE user_goals (" +
                "email TEXT PRIMARY KEY, " +
                "calorie_goal INTEGER, " +
                "protein_goal INTEGER, " +
                "carbs_goal INTEGER, " +
                "fats_goal INTEGER, " +
                "weight REAL, " +
                "goal_type TEXT, " +
                "activity_level TEXT)";
        db.execSQL(createGoalTable);
    }

    public boolean setUserGoal(String email, int cal, int protein, int carbs, int fats, double weight, String goalType, String activityLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("calorie_goal", cal);
        values.put("protein_goal", protein);
        values.put("carbs_goal", carbs);
        values.put("fats_goal", fats);
        values.put("weight", weight);
        values.put("goal_type", goalType);
        values.put("activity_level", activityLevel);
        long result = db.insertWithOnConflict("user_goals", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public UserGoal getUserGoal(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT calorie_goal, protein_goal, carbs_goal, fats_goal, weight, goal_type, activity_level FROM user_goals WHERE email = ?", new String[]{email});

        UserGoal goal = null;
        if (cursor.moveToFirst()) {
            goal = new UserGoal(
                    cursor.getInt(0),    // calorie_goal
                    cursor.getInt(1),    // protein_goal
                    cursor.getInt(2),    // carbs_goal
                    cursor.getInt(3),    // fats_goal
                    cursor.getDouble(4),  // weight
                    cursor.getString(5), // goal_type
                    cursor.getString(6)  // activity_level
            );
        }
        cursor.close();
        return goal;
    }

    public boolean insertMealLog(String userEmail, String mealName, double calories, double protein, double carbs, double fats, String logDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_email", userEmail);
        values.put("meal_name", mealName);
        values.put("calories", calories);
        values.put("protein", protein);
        values.put("carbs", carbs);
        values.put("fats", fats);
        values.put("log_date", logDate);
        long result = db.insert("meal_logs", null, values);
        return result != -1;
    }

    public Cursor getAllMealLogs(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM meal_logs WHERE user_email=? ORDER BY log_date DESC", new String[]{email});
    }

    public Cursor getMealLogsForLast7Days(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT log_date, " +
                        "SUM(calories) as total_calories, " +
                        "SUM(protein) as total_protein, " +
                        "SUM(carbs) as total_carbs, " +
                        "SUM(fats) as total_fats " +
                        "FROM meal_logs " +
                        "WHERE user_email=? AND log_date >= date('now', '-6 days') " +
                        "GROUP BY log_date ORDER BY log_date DESC",
                new String[]{email}
        );
    }

    public boolean updateMealLog(int id, String mealName, double calories, double protein, double carbs, double fats) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("meal_name", mealName);
        values.put("calories", calories);
        values.put("protein", protein);
        values.put("carbs", carbs);
        values.put("fats", fats);
        int result = db.update("meal_logs", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteMealLog(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("meal_logs", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public List<DailySummary> getMealLogsBetweenDates(String email, String startDate, String endDate) {
        List<DailySummary> summaries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT log_date, SUM(calories) as total_calories, " +
                        "SUM(protein) as total_protein, SUM(carbs) as total_carbs, SUM(fats) as total_fats " +
                        "FROM meal_logs WHERE user_email=? AND log_date BETWEEN ? AND ? " +
                        "GROUP BY log_date ORDER BY log_date ASC",
                new String[]{email, startDate, endDate});
        if (cursor.moveToFirst()) {
            do {
                summaries.add(new DailySummary(
                        cursor.getString(0),
                        cursor.getDouble(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return summaries;
    }

    public double getTodaysCalories(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        Cursor cursor = db.rawQuery(
                "SELECT SUM(calories) FROM meal_logs WHERE user_email=? AND log_date=?",
                new String[]{email, today}
        );
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public boolean setUserGoal(String email, int cal, int protein, int carbs, int fats, double weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("calorie_goal", cal);
        values.put("protein_goal", protein);
        values.put("carbs_goal", carbs);
        values.put("fats_goal", fats);
        values.put("weight", weight);
        long result = db.insertWithOnConflict("user_goals", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }


    public int getUserCalorieGoal(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT calorie_goal FROM user_goals WHERE email = ?", new String[]{email});
        int goal = 0;
        if (cursor.moveToFirst()) goal = cursor.getInt(0);
        cursor.close();
        return goal;
    }

    public double getUserWeight(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT weight FROM user_goals WHERE email = ?", new String[]{email});
        double weight = 0;
        if (cursor.moveToFirst()) weight = cursor.getDouble(0);
        cursor.close();
        return weight;
    }

    public int getUserProteinGoal(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT protein_goal FROM user_goals WHERE email = ?", new String[]{email});
        int goal = 0;
        if (cursor.moveToFirst()) goal = cursor.getInt(0);
        cursor.close();
        return goal;
    }

    public int getUserCarbsGoal(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT carbs_goal FROM user_goals WHERE email = ?", new String[]{email});
        int goal = 0;
        if (cursor.moveToFirst()) goal = cursor.getInt(0);
        cursor.close();
        return goal;
    }

    public int getUserFatsGoal(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fats_goal FROM user_goals WHERE email = ?", new String[]{email});
        int goal = 0;
        if (cursor.moveToFirst()) goal = cursor.getInt(0);
        cursor.close();
        return goal;
    }

    public boolean isEmailTaken(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean registerUser(String name, String email, String password) {
        if (isEmailTaken(email)) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", hashPassword(password));
        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE email=? AND password=?",
                new String[]{email, hashPassword(password)}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS meal_logs");
        db.execSQL("DROP TABLE IF EXISTS user_goals");
        onCreate(db);
    }
}