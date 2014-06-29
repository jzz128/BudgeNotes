package com.example.budgetnotebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class ProfileHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "BudgeNotebook4.db";
	private static final int SCHEMA_VERSION = 1;

	public ProfileHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	

	public void insert(String firstname, String lastname, String gender, String city,String email, String birthday) {
		ContentValues cv = new ContentValues();
		cv.put("p_firstname", firstname);
		cv.put("p_lastname", lastname);
		cv.put("p_gender", gender);
		cv.put("p_city", city);
		cv.put("p_email", email);
		cv.put("p_birthday", birthday);	
		getWritableDatabase().insert("profiles", "p_firstname", cv);
	}

	public void update(String id, String firstname, String lastname, String gender, String city,String email, String birthday) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		cv.put("p_firstname", firstname);
		cv.put("p_lastname", lastname);
		cv.put("p_gender", gender);
		cv.put("p_city", city);
		cv.put("p_email", email);
		cv.put("p_birthday", birthday);	
		getWritableDatabase().update("profiles", cv, "_ID=?", args);
	}

	public Cursor getAll() {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, p_firstname, p_lastname, p_gender, p_city,p_email,p_birthday  FROM profiles ORDER BY _id",
						null));
	}

	public String getFirstName(Cursor c) {
		return (c.getString(1));
	}

	public String getLastName(Cursor c) {
		return (c.getString(2));
	}

	public String getGender(Cursor c) {
		return (c.getString(3));
	}

	public String getCity(Cursor c) {
		return (c.getString(4));
	}
	
	public String getEmail(Cursor c) {
		return (c.getString(5));
	}
	
	public String getBirthday(Cursor c) {
		return (c.getString(6));
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("PROFILE", "profiles table created");
		db.execSQL("CREATE TABLE profiles (_id INTEGER PRIMARY KEY AUTOINCREMENT, p_firstname TEXT, p_lastname TEXT, p_gender TEXT, p_city TEXT, p_email TEXT,p_birthday TEXT);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public Cursor getById() {
		String[] args = { "1" };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, p_firstname,p_lastname, p_gender, p_city, p_email, p_birthday FROM profiles WHERE _ID=?",
						args));
	}

}


