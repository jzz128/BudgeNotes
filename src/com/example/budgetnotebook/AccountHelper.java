package com.example.budgetnotebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class AccountHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "BudgeNotebook1.db";
	private static final int SCHEMA_VERSION = 1;

	public AccountHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	public void insert(String account_name, String account_number, String notes) {
		ContentValues cv = new ContentValues();
		cv.put("a_name", account_name);
		cv.put("a_number", account_number);
		cv.put("a_notes", notes);
		getWritableDatabase().insert("accounts", "a_name", cv);
	}

	public void update(String id, String account_name, String account_number, 
			String notes) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		cv.put("a_name", account_name);
		cv.put("a_number", account_number);
		cv.put("a_notes", notes);
		getWritableDatabase().update("accounts", cv, "_ID=?", args);
	}

	public Cursor getAll() {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, a_name, a_number, a_notes FROM accounts ORDER BY _id",
						null));
	}

	public String getAccountName(Cursor c) {
		return (c.getString(1));
	}

	public String getAccountNumber(Cursor c) {
		return (c.getString(2));
	}

	public String getAccountNotes(Cursor c) {
		return (c.getString(3));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("ACCOUNT", "accounts table created");
		db.execSQL("CREATE TABLE accounts (_id INTEGER PRIMARY KEY AUTOINCREMENT, a_name TEXT, a_number TEXT, a_notes TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public Cursor getById(String id) {
		String[] args = { id };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, a_name, a_number, a_notes FROM accounts WHERE _ID=?",
						args));
	}

}

