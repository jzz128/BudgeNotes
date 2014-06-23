package com.example.budgetnotebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class GoalHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "BudgeNotebook2.db";
	private static final int SCHEMA_VERSION = 1;

	public GoalHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	public void insert(String goal_name, String goal_amount, String goal_date, String notes) {
		ContentValues cv = new ContentValues();
		cv.put("g_name", goal_name);
		cv.put("g_amount", goal_amount);
		cv.put("g_date", goal_date);
		cv.put("g_notes", notes);
		getWritableDatabase().insert("goals", "g_name", cv);
	}

	public void update(String id, String goal_name, String goal_amount, String goal_date,
			String notes) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		cv.put("g_name", goal_name);
		cv.put("g_amount", goal_amount);
		cv.put("g_date", goal_date);
		cv.put("g_notes", notes);
		getWritableDatabase().update("goals", cv, "_ID=?", args);
	}

	public Cursor getAll() {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, g_name, g_amount, g_date, g_notes FROM goals ORDER BY _id",
						null));
	}

	public String getGoalName(Cursor c) {
		return (c.getString(1));
	}

	public String getGoalAmount(Cursor c) {
		return (c.getString(2));
	}

	public String getGoalDate(Cursor c) {
		return (c.getString(3));
	}

	public String getGoalNotes(Cursor c) {
		return (c.getString(4));
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("GOAL", "goals table created");
		db.execSQL("CREATE TABLE goals (_id INTEGER PRIMARY KEY AUTOINCREMENT, g_name TEXT, g_amount TEXT, g_date TEXT, g_notes TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public Cursor getById(String id) {
		String[] args = { id };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, g_name,g_amount, g_date, g_notes FROM goals WHERE _ID=?",
						args));
	}

}

