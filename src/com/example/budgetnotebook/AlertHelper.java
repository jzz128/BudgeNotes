package com.example.budgetnotebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class AlertHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "BudgeNotebook6.db";
	private static final int SCHEMA_VERSION = 1;

	public AlertHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	

	public void insert(String notes) {
		ContentValues cv = new ContentValues();
		cv.put("al_notes", notes);
		getWritableDatabase().insert("alerts", "al_notes", cv);
	}

	public void update(String id, String notes) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		cv.put("al_notes", notes);
		getWritableDatabase().update("alerts", cv, "_ID=?", args);
	}

	public Cursor getAll() {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, al_notes  FROM alerts ORDER BY _id",
						null));
	}

	public String getNotes(Cursor c) {
		return (c.getString(1));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("RECOMMENDS", "alerts table created");
		db.execSQL("CREATE TABLE alerts (_id INTEGER PRIMARY KEY AUTOINCREMENT, al_notes TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public Cursor getById() {
		String[] args = { "1" };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, al_notes FROM alerts WHERE _ID=?",
						args));
	}

}



