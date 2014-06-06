package com.example.budgetnotebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class RecordHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "BudgeNotebook.db";
	private static final int SCHEMA_VERSION = 1;

	public RecordHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	public void insert(String item, String amount, String type, String notes) {
		ContentValues cv = new ContentValues();
		cv.put("item", item);
		cv.put("amount", amount);
		cv.put("type", type);
		cv.put("notes", notes);
		getWritableDatabase().insert("records", "item", cv);
	}

	public Cursor getAll() {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, item, amount, type, notes FROM records ORDER BY item",
						null));
	}

	public String getItem(Cursor c) {
		return (c.getString(1));
	}

	public String getAmount(Cursor c) {
		return (c.getString(2));
	}

	public String getType(Cursor c) {
		return (c.getString(3));
	}

	public String getNotes(Cursor c) {
		return (c.getString(4));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE records (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, amount TEXT, type TEXT, notes TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
