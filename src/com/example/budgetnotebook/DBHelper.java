/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * DBHelper.java
 * 
 * Database helper file for creating / adding / updating / querying DB data.
 * Implemented by Form and View activities.
 * 
 **/

package com.example.budgetnotebook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
	//Name of the database storing the tables for the Budget Notebook application.
	public static final String DATABASE_NAME = "BudgetNotebook.db";
	public static final int VERSION = 6; // Updated Goal Table. 1 Aug 2014 - DJM
	
	//Fields associated with the Profile Table.
	public static final String PROFILE_TABLE = "profile_table";
	public static final String P_ID = "_id";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String GENDER = "gender";
	public static final String BIRTHDAY = "birthday";
	public static final String CITY = "city";
	public static final String EMAIL = "email";
	// String for all PROFIL_TABLE field names.
	public static final String[] PROFILE_FIELDS= new String[] {P_ID, FIRST_NAME, LAST_NAME, GENDER, BIRTHDAY, CITY, EMAIL};
	
	//Fields associated with the Account Table.
	public static final String ACCOUNT_TABLE = "account_table";
	public static final String A_ID = "_id";
	public static final String ACCOUNT_NAME = "account_name";
	public static final String ACCOUNT_NUMBER = "account_number";
	public static final String ACCOUNT_TYPE = "account_type";
	public static final String BALANCE = "balance";
	public static final String DELETE_ACCOUNT_TRIGGER = "delete_account";
	// String for all ACCOUNT_TABLE field names.
	public static final String[] ACCOUNT_FIELDS = new String[] {A_ID, ACCOUNT_NAME, ACCOUNT_NUMBER, ACCOUNT_TYPE, BALANCE};

	//Fields associated with the Transaction Table.
	public static final String TRANSACTION_TABLE = "transaction_table";
	public static final String T_ID = "_id";
	public static final String T_A_ID = "t_a_id";
	public static final String TRANSACTION_NAME = "transaction_name";
	public static final String TRANSACTION_DATE = "transaction_date";
	public static final String TRANSACTION_AMOUNT = "transaction_amount";
	public static final String TRANSACTION_CATEGORY = "transaction_category";
	public static final String TRANSACTION_TYPE = "transaction_type";
	public static final String TRANSACTION_INTERVAL = "transaction_interval";
	public static final String TRANSACTION_DESCRIPTION = "transaction_description";
	public static final String TRANSACTION_ACCOUNTED = "transaction_accounted";
	public static final String TRANSACTION_CHANGE = "transaction_change";
	public static final String T_CHANGE_COLOR = "t_change_color";
	// String for all TRANSACTION_TABLE field names.
	public static final String[] TRANSACTION_FIELDS = new String[] {T_ID, T_A_ID, TRANSACTION_NAME, TRANSACTION_DATE, TRANSACTION_AMOUNT, TRANSACTION_CATEGORY, TRANSACTION_TYPE, TRANSACTION_INTERVAL, TRANSACTION_DESCRIPTION, TRANSACTION_ACCOUNTED, TRANSACTION_CHANGE, T_CHANGE_COLOR};
	
	//Fields associated with the Goal Table.
	public static final String GOAL_TABLE = "goal_table";
	public static final String G_ID = "_id";
	public static final String G_A_ID = "g_a_id";
	public static final String GOAL_NAME = "goal_name";
	public static final String GOAL_DESCRIPTION = "goal_description";
	public static final String GOAL_TYPE = "goal_type";
	public static final String GOAL_START_AMOUNT = "goal_start_amount";
	public static final String GOAL_DELTA_AMOUNT = "goal_delta_amount";
	public static final String GOAL_END_DATE = "goal_end_date";
	public static final String GOAL_STATUS = "goal_status";
	public static final String DELETE_GOAL_TRIGGER = "delete_goal";
	// String for all GOAL_TABLE field names.
	public static final String[] GOAL_FIELDS = new String[] {G_ID, G_A_ID, GOAL_NAME, GOAL_DESCRIPTION, GOAL_TYPE, GOAL_START_AMOUNT, GOAL_DELTA_AMOUNT, GOAL_END_DATE, GOAL_STATUS};
	
	//Fields associated with the Rec Table.
	public static final String REC_TABLE = "recommendation_table";
	public static final String R_ID = "_id";
	public static final String R_CRITERIA_1 = "criteria_one";
	public static final String R_CRITERIA_2 = "criteria_two";
	public static final String R_CRITERIA_3 = "criteria_three";
	public static final String R_CRITERIA_4 = "criteria_four";
	public static final String R_CRITERIA_5 = "criteria_five";
	public static final String R_IS_VALID = "is_valid";
	// String for all REC_TABLE field names.
	public static final String[] REC_FIELDS = new String[] {R_ID, R_CRITERIA_1, R_CRITERIA_2, R_CRITERIA_3, R_CRITERIA_4, R_CRITERIA_5, R_IS_VALID};	
	
	//Values to use for the Recommendation table.
	
	private String[] recommendationCategories = new String[]{"3 - Home","4 - Daily Living","5 - Transportation","6 - Entertainment","7 - Health","8 - Vacation","9 - Recreation","10 - Dues / Subscriptions","11 - Personal","12 - Obligation","13 - Other"};
	private String[] recommendationThreadholds = new String[] {"30","20","10","5","5","5","5","5","5","5","5"};
	
	//Fields associated with the Alert Table.
	public static final String ALERT_TABLE = "alert_table";
	public static final String AT_ID = "_id";
	public static final String AT_A_ID = "at_a_id";
	public static final String ALERT_NAME = "alert_name";
	public static final String ALERT_DESCRIPTION = "alert_description";
	public static final String ALERT_DUE_DATE = "alert_due_date";
	
	// String for all ALERT_TABLE field names.
	public static final String[] ALERT_FIELDS = new String[] {AT_ID, AT_A_ID, ALERT_NAME, ALERT_DESCRIPTION, ALERT_DUE_DATE};
	private static final String ACCOUNT_ID = null;
		
	//SQL Statement for creating the Profile Table.
	private final String createProfile = "CREATE TABLE IF NOT EXISTS " + PROFILE_TABLE + " ( " + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIRST_NAME + " TEXT, "	+ LAST_NAME + " TEXT, " + GENDER + " TEXT, " + BIRTHDAY + " TEXT, " + CITY + " TEXT, " + EMAIL + " TEXT);";
	
	//SQL Statement for creating the Account Table.
	private final String createAccount = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " ( " + A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NAME + " TEXT, " + ACCOUNT_NUMBER + " TEXT, " + ACCOUNT_TYPE + " TEXT, " + BALANCE + " TEXT);";
	
	//SQL Statement for creating the Transaction Table.
	private final String createTransaction = "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " ( " + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T_A_ID + " INTEGER, " + TRANSACTION_NAME + " TEXT, " + TRANSACTION_DATE + " TEXT, " + TRANSACTION_AMOUNT + " TEXT, " + TRANSACTION_CATEGORY + " TEXT, " + TRANSACTION_TYPE + " TEXT, " + TRANSACTION_INTERVAL + " TEXT, " + TRANSACTION_DESCRIPTION + " TEXT, " + TRANSACTION_ACCOUNTED + " BOOLEAN, " + TRANSACTION_CHANGE + " TEXT, " + T_CHANGE_COLOR + ");";
	
	//SQL Statement for creating the Goal Table.
	private final String createGoal = "CREATE TABLE IF NOT EXISTS " + GOAL_TABLE + " ( " + G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + G_A_ID + " INTEGER, " + GOAL_NAME + " TEXT, " + GOAL_DESCRIPTION + " TEXT, " + GOAL_TYPE + " TEXT, " + GOAL_START_AMOUNT + " TEXT, " + GOAL_DELTA_AMOUNT + " TEXT, " + GOAL_END_DATE + " TEXT, " + GOAL_STATUS + " TEXT, " + "FOREIGN KEY (" + G_A_ID + ") REFERENCES " + ACCOUNT_TABLE + "(" + A_ID + "));";
	
	//SQL Statement for creating the Rec Table.
	private final String createRec = "CREATE TABLE IF NOT EXISTS " + REC_TABLE + " ( " + R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + R_CRITERIA_1 + " TEXT, " + R_CRITERIA_2 + " TEXT, " + R_CRITERIA_3 + " TEXT, " + R_CRITERIA_4 + " TEXT, " + R_CRITERIA_5 + " TEXT, " + R_IS_VALID + " BOOLEAN);";
	
	// SQL Statement for deleting an account trigger event.
	private final String deleteAccountTrigger = "CREATE TRIGGER " + DELETE_ACCOUNT_TRIGGER + " AFTER DELETE ON " + ACCOUNT_TABLE + " FOR EACH ROW BEGIN"
			+ " DELETE FROM " + GOAL_TABLE + " WHERE " + G_A_ID + " = old._id;"
			+ " DELETE FROM " + TRANSACTION_TABLE + " WHERE " + T_A_ID + " = old._id;"
			+ " DELETE FROM " + ALERT_TABLE + " WHERE " + AT_A_ID + " = old._id;"
			+ " END";
	
	/*
	private final String deleteGoalTrigger = "CREATE TRIGGER " + DELETE_GOAL_TRIGGER + " AFTER DELETE ON " + GOAL_TABLE + " FOR EACH ROW BEGIN"
			+ " DELETE FROM " + ALERT_TABLE + " WHERE " + ALERT_NAME + " LIKE 'GOAL-old." + G_ID + "';"
			+ " END";
	*/
	
	//SQL Statement for creating the Alert Table.
	private final String createAlert = "CREATE TABLE IF NOT EXISTS " + ALERT_TABLE + " ( " + AT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AT_A_ID + " INTEGER, " + ALERT_NAME + " TEXT, " + ALERT_DESCRIPTION + " TEXT,  " + ALERT_DUE_DATE + " TEXT);";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	// Creates the database using the SQL statements.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createGoal);
		db.execSQL(createProfile);
		db.execSQL(createAccount);
		db.execSQL(createTransaction);
		db.execSQL(createAlert);
		db.execSQL(deleteAccountTrigger);
		//db.execSQL(deleteGoalTrigger);
		db.execSQL(createRec);
		
        //Add Recommendations to the database table.
        for (int i = 0; i <11; i++) {
        	db.execSQL("INSERT INTO " + REC_TABLE + "(criteria_one, criteria_two, is_valid) VALUES ('" + recommendationCategories[i] + "', '" + recommendationThreadholds[i] + "', 0);");
        }
	}

	//Tells the system what to do when the DB is updated.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		// Drop older tables if they exist
		db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE); // Must be created before GOAL_TABLE for foreign key purposes.
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
        db.execSQL("DROP TRIGGER IF EXISTS " + DELETE_ACCOUNT_TRIGGER);
        db.execSQL("DROP TABLE IF EXISTS " + REC_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ALERT_TABLE);
        
        // create fresh database table
        this.onCreate(db);
	}

	// ---------------------------------------------------------------------------------------------------------------------
	//Generic Query methods.
	// ---------------------------------------------------------------------------------------------------------------------
	
	//Returns a cursor filled with the result of the passed query string.
	public Cursor dbQuery(String query){
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
				
		return cursor;
	}
	
	// Returns an integer result of the passed query string.
	public int queryCount(String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
				
		return cursor.getCount();
	}
	
	//Returns the sum of the transaction amounts of the passed query string.
	public float querySum(String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if(cursor != null)
			cursor.moveToFirst();
		float sum = 0;
		for (int i = 0; i < cursor.getCount(); i++) {
			sum += Float.parseFloat(cursor.getString(4));
			cursor.moveToNext();
		}
			
		return sum;
	}
	
	//Returns the id of the first element in the passed query string.  Used with a limit 1 query to return the ID of the last created record.
	public int lastRowID (String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		int lastId = 0;
		Cursor c = db.rawQuery(query, null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}
		return lastId;
	}
	
	//Returns the spinner an int that can be used as a spinner location for account spinners. Corrects for case when accounts have been deleted.
	public int correctSpinID(int a_id) {
	    int correct_id = -1;
		Cursor accounts = getAllAccounts();
	    if(accounts.moveToFirst()) {
	        for(int i = 0; i < accounts.getCount(); i++) {
	        	correct_id = accounts.getInt(0);
	        	if (correct_id == a_id) {correct_id = i; break;}
	        	accounts.moveToNext();
	        }
	    }
	    return correct_id;
	}
	
	// ---------------------------------------------------------------------------------------------------------------------
	// Recommendation methods ----------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------
	
	// Add a single Recommendation. 							<-- ADDED TO ASIST IN TESTING -->
	public void addRecommendation(Recommendation recommend){
		Log.d("addRecommendation", recommend.toString());
				
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues values = new ContentValues();
		values.put(R_ID, recommend.getId());
		values.put(R_CRITERIA_1, recommend.getCategory1());
		values.put(R_CRITERIA_2, recommend.getCategory2());
		values.put(R_CRITERIA_3, recommend.getCategory3());
		values.put(R_CRITERIA_4, recommend.getCategory4());
		values.put(R_CRITERIA_5, recommend.getCategory5());
		values.put(R_IS_VALID, recommend.getIsValid());
				
		db.insert(REC_TABLE, null, values);
				
		db.close();
	}
	
	// Returns a cursor filled with all the recommendations.
	public Cursor getRecommendations() {
		SQLiteDatabase db = this.getWritableDatabase();
			
		String query = "SELECT * FROM " + REC_TABLE;
			
		Cursor cursor = db.rawQuery(query, null);
			
		return cursor;
	}
	
	//Returns a Recommendation object with the passed id.
	public Recommendation getRecommendation(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
			
		Cursor cursor =
				db.query(REC_TABLE, REC_FIELDS, R_ID + " = ?", new String[] {String.valueOf(id) }, null, null, null, null);
			
		if (cursor != null)
	        cursor.moveToFirst();
			
		Recommendation rec = new Recommendation();
		rec.setId(Integer.parseInt(cursor.getString(0)));
		rec.setCategory1(cursor.getString(1));
		rec.setCategory2(cursor.getString(2));
		rec.setCategory3(cursor.getString(3));
		rec.setCategory4(cursor.getString(4));
		rec.setCategory5(cursor.getString(5));
			 
		 Log.d("getRec("+id+")", rec.toString());
		 
		 return rec;
	}
	
	//Updates the recommendation with the id of the passed object with the attributes of the passed object.
	public int updateRecommendation(Recommendation rec) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(R_CRITERIA_1,  rec.getCategory1());
		values.put(R_CRITERIA_2, rec.getCategory2());
		values.put(R_CRITERIA_3, rec.getCategory3());
		values.put(R_CRITERIA_4, rec.getCategory4());
		values.put(R_CRITERIA_5, rec.getCategory5());
		values.put(R_IS_VALID, rec.getIsValid());
			
		int i = db.update(REC_TABLE, values, R_ID + " = ?", new String[] { String.valueOf(rec.getId()) });
			
		db.close();
			
		return i;
	}
	
	//Returns a list of Recommendation objects for all records in the Recommendation table.
	public List<Recommendation> getAllRecommendations() {
		List<Recommendation> recs = new LinkedList<Recommendation>();
			
		String query = "SELECT * FROM " + REC_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		Recommendation rec = null;
		if (cursor.moveToFirst()) {
			do {
				rec = new Recommendation();
				rec.setId(Integer.parseInt(cursor.getString(0)));
				rec.setCategory1(cursor.getString(1));
				rec.setCategory2(cursor.getString(2));
				rec.setCategory3(cursor.getString(3));
				rec.setCategory4(cursor.getString(4));
				rec.setCategory5(cursor.getString(5));
				rec.setIsValid(Boolean.parseBoolean(cursor.getString(6)));
					
				recs.add(rec);
			} while (cursor.moveToNext());
		}
			
		return recs;
	}
		
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Goal methods ---------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	// Checking the status of the goals, setting their icon color and generating the alert message.
	@SuppressLint("SimpleDateFormat")
	public void checkGoalStatus() {
		List<Goal> goalList = getListAllGoals();	
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Set your date format
		java.util.Date today = null;
		java.util.Date date = null;
		
		String currentDate;
		String newStatus = null, newDescription = null;
		String[] goalType;
		String typeVal;
		String query, alt_id;
		Cursor cursor;
		
		int count = goalList.size();
		long todayMilli, goalMilli, diffInDays;
		
		Goal goal;
		Account account;
		Alert alert = new Alert();
		
		java.util.Date d = Calendar.getInstance().getTime(); // Current time
		currentDate = sdf.format(d); // Get Date String according to date format
		
		float start = 0, delta = 0, end = 0, check = 0;
		
		Calendar todayCal = Calendar.getInstance();
		Calendar goalCal = Calendar.getInstance();
		
		try {
	        today = sdf.parse(currentDate);
	        todayCal.setTime(today);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		
		todayMilli = todayCal.getTimeInMillis();
		
		for(int i = 0; i < count; i++) {
			goal = goalList.get(i);
			goalType = goal.getType().split(" ");
			typeVal = goalType[0];
			
			alt_id = String.valueOf(goal.getId());
			
			query = "SELECT * FROM " + ALERT_TABLE + " WHERE " + ALERT_NAME + " LIKE 'GOAL-" + alt_id + "'";
			cursor = dbQuery(query);
			if(cursor.moveToFirst())
				alert = getAlert(cursor.getInt(0));
			account = getAccount(goal.getAId());
			
			start = Float.parseFloat(goal.getStartAmount());
			delta = Float.parseFloat(goal.getDeltaAmount());
						
			//=============================== Check is set based on the Goal Type ======================================
			
			if (typeVal.equals("Save")) {check = Float.parseFloat(account.getBalance()); end = start + delta;} else
			if (typeVal.equals("Pay")) {end = Float.parseFloat(account.getBalance()); check = start - delta;} else
			if (typeVal.equals("Do")) {check = Float.parseFloat(account.getBalance()); end = start - delta;}
			
			//==========================================================================================================
						
			try {
		        date = sdf.parse(goal.getEndDate());
		        goalCal.setTime(date);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
			
			goalMilli = goalCal.getTimeInMillis();
			
			diffInDays = (goalMilli- todayMilli)/(24*60*60*1000);
			
			if(diffInDays > 3) {Log.d("OPTION 1: ", "-"); newStatus =String.valueOf(R.drawable.goal_prog); newDescription = "Goal '" + goal.getName() + "' for account '" + account.getName() + "' is approaching on: " + goal.getEndDate();} else
			if(diffInDays <= 3 && diffInDays > 0) {Log.d("OPTION 2: ", "-"); newStatus = String.valueOf(R.drawable.goal_jep); newDescription = "Goal '" + goal.getName() + "' for account '" + account.getName() + "' is approaching on: " + goal.getEndDate();} else
			if(diffInDays <= 0) {	
				if(check >= end) {Log.d("OPTION 3.1: ", "-"); newStatus = String.valueOf(R.drawable.goal_success); newDescription = "Goal '" + goal.getName() + "' for account '" + account.getName() + "' was achieved!";} else
				if(check < end) {Log.d("OPTION 3.2: ", "-"); newStatus = String.valueOf(R.drawable.goal_fail); newDescription = "Goal '" + goal.getName() + "' for account '" + account.getName() + "' failed!";}
			}
			
			Log.d("the status is: ", newStatus);
			
			goal.setStatus(newStatus);
			alert.setDescription(newDescription);
			updateGoal(goal);
			updateAlert(alert);
			
			Log.d("the goal was updated!: ", "-");
			Log.d("the difference is: ", String.valueOf(diffInDays));
			Log.d("the end is: ", String.valueOf(end));
			Log.d("the check is: ", String.valueOf(check));
			Log.d("the status is: ", goal.getStatus());
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public void toastAlerts(Context context, String alertType){
		String query = "SELECT * FROM " + ALERT_TABLE + " WHERE " + ALERT_DESCRIPTION + " IS NOT NULL AND substr(" + ALERT_NAME + ",1,4) LIKE '" + alertType + "'";
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Set your date format
		java.util.Date today = null;
		java.util.Date date = null;
		String currentDate;
		
		long todayMilli, alertMilli, diffInDays;
		Calendar todayCal = Calendar.getInstance();
		Calendar tranCal = Calendar.getInstance();

		java.util.Date d = Calendar.getInstance().getTime(); // Current time
		currentDate = sdf.format(d); // Get Date String according to date format
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			do {
				try {
					date = sdf.parse(cursor.getString(4));
					tranCal.setTime(date);
					today = sdf.parse(currentDate);
					todayCal.setTime(today);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				todayMilli = todayCal.getTimeInMillis();
				alertMilli = tranCal.getTimeInMillis();
				diffInDays = (alertMilli - todayMilli)/(24*60*60*1000);
				
				if(-3 <= diffInDays && diffInDays <= 3)Toast.makeText(context, cursor.getString(3), Toast.LENGTH_LONG).show();		
					
			} while (cursor.moveToNext());
		}
		else {
			//Toast.makeText(context, "No Goals yet!", Toast.LENGTH_LONG).show();
		}
	} 
	
	// Add a single Goal.
	public void addGoal(Goal goal){
		Log.d("addGoal", goal.toString());
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(G_A_ID, goal.getAId());
		values.put(GOAL_NAME, goal.getName());
		values.put(GOAL_DESCRIPTION, goal.getDescription());
		values.put(GOAL_TYPE, goal.getType());
		values.put(GOAL_START_AMOUNT, goal.getStartAmount());
		values.put(GOAL_DELTA_AMOUNT, goal.getDeltaAmount());
		values.put(GOAL_END_DATE, goal.getEndDate());
		values.put(GOAL_STATUS, goal.getStatus());
			
		db.insert(GOAL_TABLE, null, values);
			
		db.close();
	}
		
	// Get a single Goal. Returns Goal object.
	public Goal getGoal(int id){
		SQLiteDatabase db = this.getReadableDatabase();
			
		Cursor cursor =
				db.query(GOAL_TABLE, GOAL_FIELDS, G_ID + " = ?", new String[] {String.valueOf(id) }, null, null, null, null);
			
		if (cursor != null)
	        cursor.moveToFirst();
			
		Goal goal = new Goal();
		 goal.setId(Integer.parseInt(cursor.getString(0)));
		 goal.setAId(Integer.parseInt(cursor.getString(1)));
		 goal.setName(cursor.getString(2));
		 goal.setDescription(cursor.getString(3));
		 goal.setType(cursor.getString(4));
		 goal.setStartAmount(cursor.getString(5));
		 goal.setDeltaAmount(cursor.getString(6));
		 goal.setEndDate(cursor.getString(7));
		 goal.setStatus(cursor.getString(8));
		 
		 Log.d("getGoal("+id+")", goal.toString());
		 
		 return goal;
	}
		
	// Get a list of all Goals. Returns a list of Goal objects for each record in the Goal table.
	public List<Goal> getListAllGoals() {
		List<Goal> goals = new LinkedList<Goal>();
			
		String query = "SELECT * FROM " + GOAL_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Goal goal = null;
		if (cursor.moveToFirst()) {
			do {
				goal = new Goal();
				goal.setId(Integer.parseInt(cursor.getString(0)));
				goal.setAId(Integer.parseInt(cursor.getString(1)));
				goal.setName(cursor.getString(2));
				goal.setDescription(cursor.getString(3));
				goal.setType(cursor.getString(4));
				goal.setStartAmount(cursor.getString(5));
				goal.setDeltaAmount(cursor.getString(6));
				goal.setEndDate(cursor.getString(7));
				goal.setStatus(cursor.getString(8));
					
				goals.add(goal);
			} while (cursor.moveToNext());
		}
			
		Log.d("getAllGoals()", goals.toString());
			
		return goals;
	}
		
	// For List population of Goal. Returns cursor for Goal Table.
	public Cursor getAllGoals() {
		SQLiteDatabase db = this.getWritableDatabase();
			
		String where = null;
		Cursor cursor = db.query(true, GOAL_TABLE, GOAL_FIELDS,  where,  null, null,  null, null, null);
			
		if (cursor != null) {
			cursor.moveToFirst();
		}
			
		return cursor;
	}
		
	/*
	public void toastGoal(Context context){
		String query = "SELECT * FROM " + GOAL_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(context, cursor.getString(2), Toast.LENGTH_LONG).show();		
					
			} while (cursor.moveToNext());
		}
		else {
			Toast.makeText(context, "No Goals yet!", Toast.LENGTH_LONG).show();
		}
	}
	*/
	
	// Update a single Goal.
	public int updateGoal(Goal goal) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(G_A_ID,  goal.getAId());
		values.put(GOAL_NAME, goal.getName());
		values.put(GOAL_DESCRIPTION, goal.getDescription());
		values.put(GOAL_TYPE, goal.getType());
		values.put(GOAL_START_AMOUNT, goal.getStartAmount());
		values.put(GOAL_DELTA_AMOUNT, goal.getDeltaAmount());
		values.put(GOAL_END_DATE, goal.getEndDate());
		values.put(GOAL_STATUS, goal.getStatus());
			
		int i = db.update(GOAL_TABLE, values, G_ID + " = ?", new String[] { String.valueOf(goal.getId()) });
			
		db.close();
			
		return i;
	}
		
	// Delete a single Goal.
	public void deleteGoal(Goal goal) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		db.delete(GOAL_TABLE, G_ID+"= ?", new String[] { String.valueOf(goal.getId()) });
			
		db.close();
			
	}
		
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Profile methods ------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	// Add the Profile.
	public void addProfile(Profile profile){
		Log.d("addProfile", profile.toString());
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(FIRST_NAME, profile.getFirstName());
		values.put(LAST_NAME, profile.getLastName());
		values.put(GENDER, profile.getGender());
		values.put(BIRTHDAY, profile.getBirthday());
		values.put(CITY, profile.getCity());
		values.put(EMAIL, profile.getEmail());
		
		db.insert(PROFILE_TABLE, null, values);
			
		db.close();
	}
		
	// Get the Profile.
	public Profile getProfile(int id){
		SQLiteDatabase db = this.getReadableDatabase();
			
		Cursor cursor =
				db.query(PROFILE_TABLE, PROFILE_FIELDS, P_ID + " = ?", new String[] {String.valueOf(id) }, null, null, null, null);
			
		if (cursor != null) {
	        cursor.moveToFirst();
			
		Profile profile = new Profile();
		profile.setId(Integer.parseInt(cursor.getString(0)));
		profile.setFirstName(cursor.getString(1));
		profile.setLastName(cursor.getString(2));
		profile.setGender(cursor.getString(3));
		profile.setBirthday(cursor.getString(4));
		profile.setCity(cursor.getString(5));
		profile.setEmail(cursor.getString(6));
			 
		 Log.d("getProfile("+id+")", profile.toString());
			 
		 return profile;
		} else {
			return null;
		}
	}
		
	//Check if a Profile record is created yet.
	public int checkProfileExists() {
		String query = "SELECT * FROM " + PROFILE_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		return cursor.getCount();
	}
		
	/*
	public void toastProfile(Context context){
		String query = "SELECT * FROM " + PROFILE_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(context, cursor.getString(1), Toast.LENGTH_LONG).show();		
					
			} while (cursor.moveToNext());
		}
		else {
			Toast.makeText(context, "No Profile yet!", Toast.LENGTH_LONG).show();
		}
	}
	*/
	
	// Update the Profile.
	public int updateProfile(Profile profile) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(FIRST_NAME, profile.getFirstName());
		values.put(LAST_NAME, profile.getLastName());
		values.put(GENDER, profile.getGender());
		values.put(BIRTHDAY, profile.getBirthday());
		values.put(CITY, profile.getCity());
		values.put(EMAIL, profile.getEmail());
		
		int i = db.update(PROFILE_TABLE, values, P_ID + " = ?", new String[] { String.valueOf(profile.getId()) });
			
		db.close();
			
		return i;
	}
		
	/*
	public void deleteProfile(Profile profile) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		db.delete(PROFILE_TABLE, P_ID+"= ?", new String[] { String.valueOf(profile.getId()) });
			
		db.close();		
	}
	*/
	
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Account methods ------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
	// Check if any Accounts exist.
	public boolean checkAccountExists() {
		String query = "SELECT * FROM " + ACCOUNT_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		if (cursor.getCount() < 1) {
			return false;
		} else {
			return true;
		}
	}
		
	// Add a single Account.
	public void addAccount(Account account){
		Log.d("addAccount", account.toString());
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(ACCOUNT_NAME, account.getName());
		values.put(ACCOUNT_NUMBER, account.getNumber());
		values.put(ACCOUNT_TYPE, account.getType());
		values.put(BALANCE, account.getBalance());
			
		db.insert(ACCOUNT_TABLE, null, values);
			
		db.close();
	}
		
	// Get a single Account.
	public Account getAccount(int id){
		SQLiteDatabase db = this.getReadableDatabase();
			
		Cursor cursor =
				db.query(ACCOUNT_TABLE, ACCOUNT_FIELDS, A_ID + " = ?", new String[] {String.valueOf(id) }, null, null, null, null);
			
		if (cursor != null)
	        cursor.moveToFirst();
			
		Account account = new Account();
		account.setId(Integer.parseInt(cursor.getString(0)));
		account.setName(cursor.getString(1));
		account.setNumber(cursor.getString(2));
		account.setType(cursor.getString(3));
		account.setBalance(cursor.getString(4));
		 
		 Log.d("getAccount("+id+")", account.toString());
			 
		 return account;
	}
		
	// Get a list of all Accounts as Account Objects.
	public List<Account> getListAllAccounts() {
		List<Account> accounts = new LinkedList<Account>();
			
		String query = "SELECT * FROM " + ACCOUNT_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		Account account = null;
		if (cursor.moveToFirst()) {
			do {
				account = new Account();
				account.setId(Integer.parseInt(cursor.getString(0)));
				account.setName(cursor.getString(1));
				account.setNumber(cursor.getString(2));
				account.setType(cursor.getString(3));
				account.setBalance(cursor.getString(4));
					
				accounts.add(account);
			} while (cursor.moveToNext());
		}
			
		Log.d("getListAllAccounts()", accounts.toString());
			
		return accounts;
	}
		
	// Get a list of all Accounts as Strings.
	public List<String> getAllStringAccounts(){
		List<String> accounts = new ArrayList<String>();
	         
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + ACCOUNT_TABLE;
	      
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	      
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	         	accounts.add(cursor.getString(0) + " " + cursor.getString(1) + " $" + cursor.getString(4));
	        } while (cursor.moveToNext());
	    }
	         
	    // closing connection
	    cursor.close();
	    db.close();
	         
	    // returning accounts
	    return accounts;
	}
		
	// For List population of Account ListView. Returns a cursor for the Accounts Table.
	public Cursor getAllAccounts() {
		SQLiteDatabase db = this.getWritableDatabase();
			
		String where = null;
		Cursor cursor = db.query(true, ACCOUNT_TABLE, ACCOUNT_FIELDS,  where,  null, null,  null, ACCOUNT_ID + " ASC", null);
			
		if (cursor != null) {
			cursor.moveToFirst();
		}
			
		return cursor;
	}
		
	/*
	public void toastAccount(Context context){
		String query = "SELECT * FROM " + ACCOUNT_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(context, cursor.getString(2), Toast.LENGTH_LONG).show();		
					
			} while (cursor.moveToNext());
		}
		else {
			Toast.makeText(context, "No Accounts yet!", Toast.LENGTH_LONG).show();
		}
	}
	*/
	
	// Update a single Account.
	public int updateAccount(Account account) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(ACCOUNT_NAME, account.getName());
		values.put(ACCOUNT_NUMBER, account.getNumber());
		values.put(ACCOUNT_TYPE, account.getType());
		values.put(BALANCE, account.getBalance());
			
		int i = db.update(ACCOUNT_TABLE, values, A_ID + " = ?", new String[] { String.valueOf(account.getId()) });
			
		db.close();
			
		return i;
	}
		
	// Delete a single Account.
	public void deleteAccount(Account account) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		db.delete(ACCOUNT_TABLE, A_ID+"= ?", new String[] { String.valueOf(account.getId()) });
			
		db.close();
			
	}
		
	// Return the lowest id in the table
	public int lowestAccountID () {
		String query = "SELECT * FROM " + ACCOUNT_TABLE + " ORDER BY " + A_ID + " ASC LIMIT 1";
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		if (cursor.moveToFirst()) {
			//Toast.makeText(context, cursor.getString(0), Toast.LENGTH_LONG).show();
			return Integer.parseInt(cursor.getString(0).trim());
		}
		else {
			return 0;
		}
	}
		
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Transaction methods ---------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
	// Check if any Transactions exist.
	public boolean checkTransactionExists() {
		String query = "SELECT * FROM " + TRANSACTION_TABLE;
					
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
					
		if (cursor.getCount() < 1) {
			return false;
		} else {
			return true;
		}
	}
		
	// Check if a transaction date is on or before the date stored ed in the passed string 'against'.
	@SuppressLint("SimpleDateFormat")
	public boolean checkAccountedDate(String transDate, String against) {
			
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Set your date format
		java.util.Date today = null;
		java.util.Date date = null;
		String currentDate;
			
		if (against.equals("now")) {
			java.util.Date d = Calendar.getInstance().getTime(); // Current time
			currentDate = sdf.format(d); // Get Date String according to date format
		} else {
			currentDate = against;
		}
							
		try {
	        date = sdf.parse(transDate);
	        today = sdf.parse(currentDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		
		Log.d("TRANSACTION DATE: ", String.valueOf(date));	
		Log.d("TODAY DATE: ", String.valueOf(today));
		
		if (date.before(today) || date.equals(today)) {
			Log.d("ACCOUNTED: ", "TRUE");
			return true;
		} else {
			Log.d("ACCOUNTED: ", "FALSE");
			return false;
		}
		
	}
		
	// Add a single Transaction.
	public void addTransaction(Transaction transaction){
		Log.d("addTransaction", transaction.toString());
					
		SQLiteDatabase db = this.getWritableDatabase();
					
		ContentValues values = new ContentValues();
		values.put(T_A_ID, transaction.getAID());
		values.put(TRANSACTION_NAME, transaction.getName());
		values.put(TRANSACTION_DATE, transaction.getDate());
		values.put(TRANSACTION_AMOUNT, transaction.getAmount());
		values.put(TRANSACTION_CATEGORY, transaction.getCategory());
		values.put(TRANSACTION_TYPE, transaction.getType());
		values.put(TRANSACTION_INTERVAL, transaction.getInterval());
		values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
		values.put(TRANSACTION_ACCOUNTED, transaction.getAccounted());
		values.put(TRANSACTION_CHANGE, transaction.getChange());
		values.put(T_CHANGE_COLOR, transaction.getChangeColor());
			
		db.insert(TRANSACTION_TABLE, null, values);
					
		db.close();
	}
				
	// Get a single Transaction.
	public Transaction getTransaction(int id){
		SQLiteDatabase db = this.getReadableDatabase();
					
		Cursor cursor =
				db.query(TRANSACTION_TABLE, TRANSACTION_FIELDS, T_ID + " = ?", new String[] {String.valueOf(id) }, null, null, null, null);
					
		if (cursor != null)
	        cursor.moveToFirst();
			
		// Set the Boolean
		boolean accounted = false;
		int x = cursor.getInt(9);
		if (x == 1) accounted = true;
			
		Transaction transaction = new Transaction();
		transaction.setId(Integer.parseInt(cursor.getString(0)));
		transaction.setAId(Integer.parseInt(cursor.getString(1)));
		transaction.setName(cursor.getString(2));
		transaction.setDate(cursor.getString(3));
		transaction.setAmount(cursor.getString(4));
		transaction.setCategory(cursor.getString(5));
		transaction.setType(cursor.getString(6));
		transaction.setInterval(cursor.getString(7));
		transaction.setDescription(cursor.getString(8));
		transaction.setAccounted(accounted); // Updated to incorporate new transaction method.
		transaction.setChange(cursor.getString(10));
		transaction.setChangeColor(cursor.getString(11));
				 
		 Log.d("getTransaction("+id+")", transaction.toString());
				 
		 return transaction;
	}
				
	// Get a list of all Transactions.
	public List<Transaction> getAllListTransactions(int a_id) {
		List<Transaction> transactions = new LinkedList<Transaction>();
					
		String where = null;
		String having = null;
		String order = null;
			
		if(a_id != 0) {
			where = T_A_ID+"="+a_id;
		}
		
		order = "substr(" + TRANSACTION_DATE + ",7) || substr(" + TRANSACTION_DATE + ",1,2) || substr(" + TRANSACTION_DATE + ",4,2)";
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(true, TRANSACTION_TABLE, TRANSACTION_FIELDS,  where,  null, null,  having, order, null);
					
		Transaction transaction = null;
		boolean accounted;
		int x;
			
		if (cursor.moveToFirst()) {
				
			do {
				// Set the Boolean
				accounted = false;
				x = cursor.getInt(9);
				if (x == 1) accounted = true;
					
				transaction = new Transaction();
				transaction.setId(Integer.parseInt(cursor.getString(0)));
				transaction.setAId(Integer.parseInt(cursor.getString(1)));
				transaction.setName(cursor.getString(2));
				transaction.setDate(cursor.getString(3));
				transaction.setAmount(cursor.getString(4));
				transaction.setCategory(cursor.getString(5));
				transaction.setType(cursor.getString(6));
				transaction.setInterval(cursor.getString(7));
				transaction.setDescription(cursor.getString(8));
				transaction.setAccounted(accounted); // Updated to incorporate new transaction method.
				transaction.setChange(cursor.getString(10));
				transaction.setChangeColor(cursor.getString(11));
						
				transactions.add(transaction);
			} while (cursor.moveToNext());
		}
					
		Log.d("getAllTransactions()", transactions.toString());
				
		return transactions;
	}
		
	// For List population of Account ListView
	public Cursor getAllTransactions(int a_id) {
		SQLiteDatabase db = this.getWritableDatabase();
					
		String where = null;
		String having = null;
		String order = null;
			
		order = "substr(" + TRANSACTION_DATE + ",7) || substr(" + TRANSACTION_DATE + ",1,2) || substr(" + TRANSACTION_DATE + ",4,2)";
						
		if(a_id != 0) {
			where = T_A_ID + " = " + a_id + " AND " + TRANSACTION_ACCOUNTED + " = " + 1;
		}
		Cursor cursor = db.query(true, TRANSACTION_TABLE, TRANSACTION_FIELDS,  where,  null, null,  having, order, null);
	
		if (cursor != null) {
			cursor.moveToFirst();
		}
					
		return cursor;
	}
	
	// For List population of Account ListView
		@SuppressLint("SimpleDateFormat")
		public Cursor getAllTransactionsInRange(int a_id, String start, String end) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Set your date format
			java.util.Date endDate = null;
			java.util.Date startDate = null;
			
			String currentEnd;
			String currentStart;
			
			Calendar rngCal;
			
			int iDay, iMonth, iYear;
			
			String nDay, nMonth;
			String rngDateEnd, rngDateStart;
			
			//**************START DATE*********************
			// Convert date string to date format
			if (start.equals("now")) {
				java.util.Date d = Calendar.getInstance().getTime(); // Current time
				currentStart = sdf.format(d); // Get Date String according to date format
			} else {
				currentStart = start;
			}

			try {
				startDate = sdf.parse(currentStart);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			rngCal = Calendar.getInstance();
			rngCal.setTime(startDate);
			
			
			iDay = rngCal.get(Calendar.DAY_OF_MONTH);
			iMonth = rngCal.get(Calendar.MONTH);
			iYear = rngCal.get(Calendar.YEAR);
			
			if (iDay < 10) {
				nDay = "0" + String.valueOf(iDay);
			} else {
				nDay = String.valueOf(iDay);
			}
			if (iMonth < 10) {
				nMonth = "0" + String.valueOf(iMonth+1);
			} else {
				nMonth =String.valueOf(iMonth+1);
			}
			
			rngDateStart = String.valueOf(iYear) + nMonth + nDay;
			//**************START DATE*********************
			
			//**************END DATE*********************
			if (end.equals("now")) {
				java.util.Date d = Calendar.getInstance().getTime(); // Current time
				currentEnd = sdf.format(d); // Get Date String according to date format
			} else {
				currentEnd = end;
			}

			try {
				endDate = sdf.parse(currentEnd);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			rngCal = Calendar.getInstance();
			rngCal.setTime(endDate);
			
			
			iDay = rngCal.get(Calendar.DAY_OF_MONTH);
			iMonth = rngCal.get(Calendar.MONTH);
			iYear = rngCal.get(Calendar.YEAR);
			
			if (iDay < 10) {
				nDay = "0" + String.valueOf(iDay);
			} else {
				nDay = String.valueOf(iDay);
			}
			if (iMonth < 10) {
				nMonth = "0" + String.valueOf(iMonth+1);
			} else {
				nMonth =String.valueOf(iMonth+1);
			}
			
			rngDateEnd = String.valueOf(iYear) + nMonth + nDay;
			//**************END DATE*********************

			SQLiteDatabase db = this.getWritableDatabase();
						
			String where = null;
			String having = null;
			String order = null;
				
			order = "substr(" + TRANSACTION_DATE + ",7) || substr(" + TRANSACTION_DATE + ",1,2) || substr(" + TRANSACTION_DATE + ",4,2)";
			
			Log.d("Start=",rngDateStart);
			Log.d("End=",rngDateEnd);
			
			if(a_id != 0) {
				where = T_A_ID + " = " + a_id + " AND " + TRANSACTION_ACCOUNTED + " = " + 1 + " AND CAST(" + order + " AS INTEGER) <= " + rngDateEnd + " AND CAST(" + order + " AS INTEGER) >= " +  rngDateStart;
			}
			Cursor cursor = db.query(true, TRANSACTION_TABLE, TRANSACTION_FIELDS,  where,  null, null,  having, order, null);
		
			if (cursor != null) {
				cursor.moveToFirst();
			}
			db.close();			
			return cursor;
			
		}
	
	/*
	public void toastTransactionDates(Context context) {
		SQLiteDatabase db = this.getWritableDatabase();
			
		String where = null;
		String having = null;
		String order = null;
			
		order = "substr(" + TRANSACTION_DATE + ",7) || substr(" + TRANSACTION_DATE + ",4,2) || substr(" + TRANSACTION_DATE + ",1,2) ASC";

		Cursor cursor = db.query(true, TRANSACTION_TABLE, TRANSACTION_FIELDS,  where,  null, null,  having, order, null);
			
		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(context, cursor.getString(3), Toast.LENGTH_LONG).show();		
							
			} while (cursor.moveToNext());
		}
		else {
			Toast.makeText(context, "No Transactions yet!", Toast.LENGTH_LONG).show();
		}
	}
	*/
		
	/*
	public void toastTransaction(Context context){
		String query = "SELECT * FROM " + TRANSACTION_TABLE;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
					
		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(context, cursor.getString(2), Toast.LENGTH_LONG).show();		
							
			} while (cursor.moveToNext());
		}
		else {
			Toast.makeText(context, "No Transactions yet!", Toast.LENGTH_LONG).show();
		}
	}
	*/
		
	// Update a single Transaction.
	public int updateTransaction(Transaction transaction) {
					
		SQLiteDatabase db = this.getWritableDatabase();
					
		ContentValues values = new ContentValues();
		values.put(T_A_ID, transaction.getAID());
		values.put(TRANSACTION_NAME, transaction.getName());
		values.put(TRANSACTION_DATE, transaction.getDate());
		values.put(TRANSACTION_AMOUNT, transaction.getAmount());
		values.put(TRANSACTION_CATEGORY, transaction.getCategory());
		values.put(TRANSACTION_TYPE, transaction.getType());
		values.put(TRANSACTION_INTERVAL, transaction.getInterval());
		values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
		values.put(TRANSACTION_ACCOUNTED, transaction.getAccounted()); // Updated to incorporate new transaction method.
		values.put(TRANSACTION_CHANGE, transaction.getChange());
		values.put(T_CHANGE_COLOR, transaction.getChangeColor());
					
		int i = db.update(TRANSACTION_TABLE, values, T_ID + " = ?", new String[] { String.valueOf(transaction.getId()) });
					
		db.close();
					
		return i;
	}
				
	// Delete a single Transaction.
	public void deleteTransaction(Transaction transaction) {
					
		SQLiteDatabase db = this.getWritableDatabase();
					
		db.delete(TRANSACTION_TABLE, T_ID+"= ?", new String[] { String.valueOf(transaction.getId()) });
					
		db.close();
					
	}
				
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Alert methods ---------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
	// Add a single Alert.
	public void addAlert(Alert alert){
		Log.d("addAlert", alert.toString());
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(AT_A_ID, alert.getAtId());
		values.put(ALERT_NAME, alert.getName());
		values.put(ALERT_DESCRIPTION, alert.getDescription());
		values.put(ALERT_DUE_DATE, alert.getDueDate());
			
		db.insert(ALERT_TABLE, null, values);
		
		db.close();
	}
		
	// Get a single Alert.
	public Alert getAlert(int id){
		SQLiteDatabase db = this.getReadableDatabase();
			
		Cursor cursor =
				db.query(ALERT_TABLE, ALERT_FIELDS, AT_ID + " = ?", new String[] {String.valueOf(id) }, null, null, null, null);
			
		if (cursor != null)
	        cursor.moveToFirst();
			
		Alert alert = new Alert();
		alert.setId(Integer.parseInt(cursor.getString(0)));
		alert.setAId(Integer.parseInt(cursor.getString(1)));
		alert.setName(cursor.getString(2));
		alert.setDescription(cursor.getString(3));
		alert.setDueDate(cursor.getString(4));
			 
		 Log.d("getAlert("+id+")", alert.toString());
			 
		 return alert;
	}
		
	// Get a list of all Alerts. 
	public List<Alert> getListAllAlerts() {
		List<Alert> alerts = new LinkedList<Alert>();
			
		String query = "SELECT * FROM " + ALERT_TABLE;
			
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
			
		Alert alert = null;
		if (cursor.moveToFirst()) {
			do {
				alert = new Alert();
				alert.setId(Integer.parseInt(cursor.getString(0)));
				alert.setAId(Integer.parseInt(cursor.getString(1)));
				alert.setName(cursor.getString(2));
				alert.setDescription(cursor.getString(3));
				alert.setDueDate(cursor.getString(4));
					
				alerts.add(alert);
			} while (cursor.moveToNext());
		}
			
		Log.d("getAllAlerts()", alerts.toString());
		
		return alerts;
	}
		
	// For List population of Alert
	public Cursor getAllAlerts() {
		SQLiteDatabase db = this.getReadableDatabase();
			
		String where = null;
		Cursor cursor = db.query(true, ALERT_TABLE, ALERT_FIELDS,  where,  null, null,  null, null, null);
			
		if (cursor != null) {
			cursor.moveToFirst();
		}
			
		return cursor;
	}
			
	// Update a single Alert.
	public int updateAlert(Alert alert) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(AT_A_ID,  alert.getAtId());
		values.put(ALERT_NAME, alert.getName());
		values.put(ALERT_DESCRIPTION, alert.getDescription());
		values.put(ALERT_DUE_DATE, alert.getDueDate());
			
		int i = db.update(ALERT_TABLE, values, AT_ID + " = ?", new String[] { String.valueOf(alert.getId()) });
			
		db.close();
			
		return i;
	}
		
	// Delete a single Alert.
	public void deleteAlert(Alert alert) {
			
		SQLiteDatabase db = this.getWritableDatabase();
			
		db.delete(ALERT_TABLE, AT_ID+"= ?", new String[] { String.valueOf(alert.getId()) });
			
		db.close();
			
	}	

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Other methods --------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	// Accounts for transactions that are on or before the spinner end date
	public void cleanTransactions(Context context, String endDate) {
			
		int numAccounts;
		int numTran = 0;
		float tranSum = 0;
		float accBase = 0;
		int a_id;
		String query;
			
		// Check if any accounts exist. Could be replaced with comparison based on a queryCount() call.
		if (checkAccountExists()) {
			// Get a list off all the accounts and a count of how many there are.
			List<Account> accList = getListAllAccounts();
			numAccounts = accList.size();				
			
			// Check if any transactions exist.
			if(checkTransactionExists()) {
				// Iterate through all accounts.
				for (int i = 0; i < numAccounts; i++) {
					// Get the _id of the current account.
					a_id = accList.get(i).getId();
					//Establish a base query returning all transactions associated with an account.
					query = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE " + T_A_ID + " = " + a_id;
					//Returns the total count of transactions associated with the current account.
					numTran = queryCount(query);
					Log.d("numTran", Integer.toString(numTran));
					//Returns a sum of all Transactions that are currently accounted for.
					tranSum = querySum(query + " AND " + TRANSACTION_ACCOUNTED + " = " + 1);
					//Returns the base (beginning) balance of the current account.
					accBase = Float.parseFloat((accList.get(i).getBalance())) - tranSum;
					
					// Reset the current account base balance.
					accList.get(i).setBalance(String.format("%.2f",accBase));
					updateAccount(accList.get(i));
					
				}
				// Get a list of all the transactions and a count of how many there are.
				List<Transaction> tranList = getAllListTransactions(0);
				int count = tranList.size();
				
				Account account;
				String currBalance;
				String changeAmount;
				float newBalance;
				// Iterate through all the transactions
				for (int i = 0; i < count; i++) {
					//Get the current transaction Account and balance.
					account = getAccount(tranList.get(i).getAID());
					currBalance = account.getBalance();
					// Set the accounted flag against today's date and update the transaction
					//tranList.get(i).setAccounted(checkAccountedDate(tranList.get(i).getDate(), "now"));
					tranList.get(i).setAccounted(checkAccountedDate(tranList.get(i).getDate(), endDate));
					updateTransaction(tranList.get(i));
					//Get the transaction amount.
					changeAmount = tranList.get(i).getAmount();
					// If the transaction is accounted update the account balance.
					if(tranList.get(i).getAccounted()) {
						if(account.getType().equals("CR")) {newBalance = Float.parseFloat(currBalance) - Float.parseFloat(changeAmount);} else
							{newBalance = Float.parseFloat(currBalance) + Float.parseFloat(changeAmount);}
						account.setBalance(String.format("%.2f",newBalance));
						updateAccount(account);
						tranList.get(i).setChange(account.getBalance());
						
						if (newBalance > 100) {tranList.get(i).setChangeColor("#006400");} else
						if (0 <= newBalance  && newBalance <= 100) {tranList.get(i).setChangeColor("#DAA520");} else
						if (newBalance < 0) {tranList.get(i).setChangeColor("#FF0000");}
						
						updateTransaction(tranList.get(i));
					} else {
						tranList.get(i).setChange(null);
						tranList.get(i).setChangeColor(null);
						updateTransaction(tranList.get(i));
					}
				}
			} else {
				//Toast.makeText(context, "No Transactions Exist to update!", Toast.LENGTH_LONG).show();
			}
			
		} else {
			//Toast.makeText(context, "No Accounts Exist to update!", Toast.LENGTH_LONG).show();
		}
		
	}
	
	@SuppressLint("SimpleDateFormat")
	public void checkTransactionStatus() {
		Log.d("Checking Transaction Status","-");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Set your date format
		java.util.Date today = null;
		java.util.Date date = null;
		String currentDate;
		String newAlertDate;
		
		java.util.Date d = Calendar.getInstance().getTime(); // Current time
		currentDate = sdf.format(d); // Get Date String according to date format
									
		Transaction transaction;
		Alert alert;
		Account account;
		
		String query = "SELECT * FROM " + ALERT_TABLE + " WHERE substr(" + ALERT_NAME + ",1,4) LIKE 'TRAN'";
		String[] nameSplit;
		String query2;

		String tranAmount = "error";
		
		String cal_for_month, cal_for_year, cal_for_day;
		
		Cursor alertCursor = dbQuery(query);
		Cursor transCursor;
		
		//long todayMilli, tranMilli, diffInDays;
		Calendar todayCal = Calendar.getInstance();
		Calendar tranCal = Calendar.getInstance();
		Log.d("THERE ARE THIS MANY ALERTS: ", String.valueOf(alertCursor.getCount()));
		if (alertCursor.moveToFirst()) {
			
			alert = new Alert();
			account = new Account();
			
			do {
				alert = getAlert(alertCursor.getInt(0));
				alert.setDueDate("1/1/1990");
				account = getAccount(alertCursor.getInt(1));
				
				nameSplit = alertCursor.getString(2).split("-");
				transaction = getTransaction(Integer.parseInt(nameSplit[1]));	
				
				try {
			        date = sdf.parse(alert.getDueDate());
			        tranCal.setTime(date);
			        today = sdf.parse(currentDate);
			        todayCal.setTime(today);
			    } catch (ParseException e) {
			        e.printStackTrace();
			    }
				
				cal_for_month = Integer.toString(tranCal.get(Calendar.MONTH)+1);
		        if (tranCal.get(Calendar.MONTH)+1 < 10 ) cal_for_month = "0" + cal_for_month;
		            
		        cal_for_year = Integer.toString(tranCal.get(Calendar.YEAR));
		            
		        //Sets the day value to two digit format for sorting.
		        cal_for_day = Integer.toString(tranCal.get(Calendar.DAY_OF_MONTH));
		           
		        if (tranCal.get(Calendar.DAY_OF_MONTH) < 10 ) cal_for_day = "0" + cal_for_day;
				newAlertDate = cal_for_month + "/" + cal_for_day + "/" + cal_for_year;
				
				if (date.before(today) || date.equals(today)) {
					query2 = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE " + TRANSACTION_NAME + " LIKE '" + transaction.getName() + "-" + transaction.getId() + "'";
					transCursor = dbQuery(query2);
					Log.d("THERE ARE THIS MANY TRANSACTIONS: ", String.valueOf(transCursor.getCount()));
					if (transCursor.moveToFirst()) {
						do {
							try {
								date = sdf.parse(transCursor.getString(3));
								tranCal.setTime(date);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							
							Log.d("THE TRANSACTION DATE IS: ", String.valueOf(date));

							if (date.after(today) || date.equals(today)) {
								cal_for_month = Integer.toString(tranCal.get(Calendar.MONTH)+1);
						        if (tranCal.get(Calendar.MONTH)+1 < 10 ) cal_for_month = "0" + cal_for_month;
						            
						        cal_for_year = Integer.toString(tranCal.get(Calendar.YEAR));
						            
						        //Sets the day value to two digit format for sorting.
						        cal_for_day = Integer.toString(tranCal.get(Calendar.DAY_OF_MONTH));
						           
						        if (tranCal.get(Calendar.DAY_OF_MONTH) < 10 ) cal_for_day = "0" + cal_for_day;
								newAlertDate = cal_for_month + "/" + cal_for_day + "/" + cal_for_year;
								tranAmount = transCursor.getString(4);
								break;
							}
						} while (transCursor.moveToNext());
					}
				}
				
				alert.setDueDate(newAlertDate);
				//todayMilli = todayCal.getTimeInMillis();
				//tranMilli = tranCal.getTimeInMillis();
				//diffInDays = (tranMilli- todayMilli)/(24*60*60*1000);
				Log.d("THE DIFF IN DAYS IS: ", String.valueOf(date));
				//if(diffInDays <= 3 && diffInDays > 0) {
					alert.setDescription("Transaction for " + tranAmount + " on Account " + account.getName() + " will occur on " + alert.getDueDate() + ".");
				//} else {
				//	alert.setDescription(null);
				//}
				updateAlert(alert);
				
			} while (alertCursor.moveToNext());
		}
		
	}
	
	// Set transactions to be accounted according to the date passed in the date String in the account passed by A_ID.
	public void seeFuture (Context context, String date, int A_ID) {
		
		Log.d ("SEEING THE FUTURE: ", "-");
		Log.d("USING THE DATE: ", String.valueOf(date));
		
		List<Transaction> tranList = getAllListTransactions(A_ID);
		int numTrans = tranList.size();
		Account account;
		String currBalance;
		String changeAmount;
		float newBalance;
		boolean pAccounted;
					
		for (int i = 0; i < numTrans; i++) {
			account = getAccount(tranList.get(i).getAID());
			currBalance = account.getBalance();
			pAccounted = tranList.get(i).getAccounted();
							
			// Set the threshold date here -----------------------------------------------
			tranList.get(i).setAccounted(checkAccountedDate(tranList.get(i).getDate(), date));
			// --------------------------------------------------------------------------
				
			updateTransaction(tranList.get(i));
			//Toast.makeText(this, String.valueOf(pAccounted), Toast.LENGTH_LONG).show();
			//Toast.makeText(this, String.valueOf(tranList.get(i).getAccounted()), Toast.LENGTH_LONG).show();
				
			if(!pAccounted && tranList.get(i).getAccounted()) {
				changeAmount = tranList.get(i).getAmount();
				newBalance = Float.parseFloat(currBalance) + Float.parseFloat(changeAmount);
				account.setBalance(String.format("%.2f",newBalance));				
				updateAccount(account);
				
				if (newBalance > 100) {tranList.get(i).setChangeColor("#006400");} else
				if (0 <= newBalance  && newBalance <= 100) {tranList.get(i).setChangeColor("#DAA520");} else
				if (newBalance < 0) {tranList.get(i).setChangeColor("#FF0000");}
				
				tranList.get(i).setChange(account.getBalance());
				updateTransaction(tranList.get(i));
			} else if (pAccounted && !tranList.get(i).getAccounted()) {
				changeAmount = tranList.get(i).getAmount();
				newBalance = Float.parseFloat(currBalance) - Float.parseFloat(changeAmount);
				account.setBalance(String.format("%.2f",newBalance));				
				updateAccount(account);
				tranList.get(i).setChange(null);
				tranList.get(i).setChangeColor(null);
				updateTransaction(tranList.get(i));
			} else if(pAccounted == tranList.get(i).getAccounted()) {
				//Do Nothing.
			} else {
				Toast.makeText(context, "An Error has Occured!", Toast.LENGTH_LONG).show();
			}
		}		
	}
		
	// Creates a years worth of recurring transactions according to the interval passed by interval.
	@SuppressLint("SimpleDateFormat")
	public void createRecurringTransactions (int interval, String transDateS, int T_ID) {
		Transaction transaction = getTransaction(T_ID);
		int recDayInt = 0;
		int recMonInt = 0;
		int recYerInt = 0;
		int recCount = 0;
			
		Calendar intCal;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Set your date format
		java.util.Date date = null;
		String intDate;
		int iDay, iMonth, iYear;
		String nDay, nMonth;
		Alert alert;
		
		//Handling the recurring transactions.
		if (interval != 0) {
				
			switch (interval) {
				case 1:
					//Every two weeks transaction. Adds 26 extra transactions.
					recDayInt = 14;
					recMonInt = 0;
					recYerInt = 0;
					recCount = 26;
					break;
				case 2:
					//Every month transaction. Adds 12 additional transactions.
					recDayInt = 0;
					recMonInt = 1;
					recYerInt = 0;
					recCount = 12;
					break;
				case 3:
					//Every year transaction. Adds one additional transaction.
					recDayInt = 0;
					recMonInt = 0;
					recYerInt = 1;
					recCount = 1;
					break;
			}
			
			try {
				date = sdf.parse(transDateS);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
			
			intCal = Calendar.getInstance();
			intCal.setTime(date);
			intCal.add(Calendar.DATE, recDayInt);
			intCal.add(Calendar.MONTH, recMonInt);
			intCal.add(Calendar.YEAR, recYerInt);
			
			iDay = intCal.get(Calendar.DAY_OF_MONTH);
			iMonth = intCal.get(Calendar.MONTH);
			iYear = intCal.get(Calendar.YEAR);
			
			if (iDay < 10) {
				nDay = "0" + String.valueOf(iDay);
			} else {
				nDay = String.valueOf(iDay);
			}
			if (iMonth < 9) {
				nMonth = "0" + String.valueOf(iMonth+1);
			} else {
				nMonth =String.valueOf(iMonth+1);
			}
			
			intDate = nMonth + "/" + nDay + "/" + String.valueOf(iYear);
			//Toast.makeText(getBaseContext(), String.valueOf(intDate), Toast.LENGTH_LONG).show();
			
			String tName = transaction.getName();
			int tID = transaction.getId();
			
			alert = new Alert (transaction.getAID(), "TRAN-" + String.valueOf(tID), null, intDate);
			addAlert(alert);
			
			for (int i = 0; i < recCount; i++) {
				transaction.setName(tName + "-" + String.valueOf(tID));
				transaction.setDate(intDate);
				transaction.setAccounted(false);
				transaction.setChange(null);
				transaction.setChangeColor(null);
				addTransaction(transaction);
				
				try {
					date = sdf.parse(intDate);
		        } catch (ParseException e) {
		            e.printStackTrace();
		        }
				intCal.setTime(date);
				if (interval == 1) intCal.add(Calendar.DATE, recDayInt);
				intCal.add(Calendar.MONTH, recMonInt);
				intCal.add(Calendar.YEAR, recYerInt);
				
				iDay = intCal.get(Calendar.DAY_OF_MONTH);
				iMonth = intCal.get(Calendar.MONTH);
				iYear = intCal.get(Calendar.YEAR);
				
				if (iDay < 10) {
					nDay = "0" + String.valueOf(iDay);
				} else {
					nDay = String.valueOf(iDay);
				}
				if (iMonth < 9) {
					nMonth = "0" + String.valueOf(iMonth+1);
				} else {
					nMonth =String.valueOf(iMonth+1);
				}
				
				intDate = nMonth + "/" + nDay + "/" + String.valueOf(iYear);
				//Toast.makeText(getBaseContext(), String.valueOf(intDate), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	//Delete recurring transactions spawned from passed transaction object and original transaction object.
	public void deleteRecommendationTransactions(Transaction transaction) {
		int nLen = transaction.getName().length() + 2;
		String query = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE substr(" + TRANSACTION_NAME + "," + nLen + ") = '" + transaction.getId() + "'";
		
		Transaction recTran;
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
				
		float changeAmount = 0;
		if (transaction.getAccounted()) changeAmount = 1;
		
		
		Account account;
		account = getAccount(transaction.getAID());
		
		if (cursor.moveToFirst()) {
			do {
				recTran = getTransaction(cursor.getInt(0));
				if (recTran.getAccounted()) changeAmount = changeAmount + 1;
				deleteTransaction(recTran);
			} while (cursor.moveToNext());
		}
		changeAmount = changeAmount * Float.parseFloat(transaction.getAmount());
		deleteTransaction(getTransaction(transaction.getId()));
		changeAmount = Float.parseFloat(account.getBalance()) - changeAmount;
		account.setBalance(String.format("%.2f",changeAmount));
		updateAccount(account);
	}
	
	public void recalcAlert(Transaction transaction) {
		
		int nLen = transaction.getName().length() + 2;
		String query = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE substr(" + TRANSACTION_NAME + "," + nLen + ") = '" + transaction.getId() + "'";
		String alertQuery ="SELECT * FROM " + ALERT_TABLE + " WHERE " + AT_A_ID + " = " + transaction.getAID() + " AND " + ALERT_NAME + " = 'TRAN-" + transaction.getId() + "'"; 
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		Cursor alertCursor = db.rawQuery(alertQuery, null);
				
		Alert alert = new Alert();

		if (cursor.moveToFirst()) {
			if(alertCursor.moveToFirst()) {
				alert = getAlert(alertCursor.getInt(0));
				alert.setDueDate("01/01/1999");
				updateAlert(alert);
			}
		} else {
			if(alertCursor.moveToFirst()) {
				alert = getAlert(alertCursor.getInt(0));
				deleteAlert(alert);
			}
		}
	}
	
	//Edit recurring transactions spawned from passed transaction object and original transaction object.
	public void editRecurringTransactions(Transaction transaction, int id, boolean subsTransOnly) {
		// Create an object for the initially generated transaction.
		Transaction baseTran = getTransaction(id);
		// Set the name offset length so transactions can be queried
		int nLen = baseTran.getName().length() + 2;
		// Set the query to be every associated transaction or subsequent only transactions according to the passed boolean.
		String query = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE substr(" + TRANSACTION_NAME + "," + nLen + ") = '" + id + "'";	
		if (subsTransOnly)
			query = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE substr(" + TRANSACTION_NAME + "," + nLen + ") = '" + id + "' AND " + T_ID + " >= " + transaction.getId();
		// Place holder object for the query results	
		Transaction recTran;
		// Open the database and fill a cursor with the query results.	
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		// Place holders for the account delta amounts.	
		float changeAmount = 0;
		float changeBackAmount = 0;
		// Object for the associated account.	
		Account account;
		account = getAccount(transaction.getAID());
		//Update transactions in the query and generate the changeAmount / changeBackAmount counter	
		if (cursor.moveToFirst()) {
			do {
				recTran = getTransaction(cursor.getInt(0));
				transaction.setId(recTran.getId());
				transaction.setName(recTran.getName());
				transaction.setDate(recTran.getDate());
				transaction.setAccounted(recTran.getAccounted());
				// Stores the transaction amount if previously accounted and increments the counter.	
				if (transaction.getAccounted()) {
					changeAmount = changeAmount + Float.parseFloat(recTran.getAmount());
					changeBackAmount = changeBackAmount + 1;
				}
				updateTransaction(transaction);
			} while (cursor.moveToNext());
		}

		// Handles the base transaction like above if the scope is all associated transactions.
		if(!subsTransOnly) {
			transaction.setId(baseTran.getId());
			transaction.setName(baseTran.getName());
			transaction.setDate(baseTran.getDate());
			transaction.setAccounted(baseTran.getAccounted());
			if (transaction.getAccounted()) {
				changeAmount = changeAmount + Float.parseFloat(baseTran.getAmount());
				changeBackAmount = changeBackAmount + 1;
			}
			updateTransaction(transaction);
		}
		
		//Reverses all previously accounted transactions and stores the new amount
		changeAmount = Float.parseFloat(account.getBalance()) - changeAmount;
		// Sets the new delta amount using the counter and passed transaction amount
		changeBackAmount = changeBackAmount * Float.parseFloat(transaction.getAmount());
		// Set teh new account balance
		changeAmount = changeAmount + changeBackAmount;
		account.setBalance(String.format("%.2f",changeAmount));
		updateAccount(account);
	}
		
}
