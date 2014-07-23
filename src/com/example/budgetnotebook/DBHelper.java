package com.example.budgetnotebook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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
	public static final int VERSION = 3; // Updated Transaction Table. 22 July 2014 - DJM
	
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
	// String for all TRANSACTION_TABLE field names.
	public static final String[] TRANSACTION_FIELDS = new String[] {T_ID, T_A_ID, TRANSACTION_NAME, TRANSACTION_DATE, TRANSACTION_AMOUNT, TRANSACTION_CATEGORY, TRANSACTION_TYPE, TRANSACTION_INTERVAL, TRANSACTION_DESCRIPTION, TRANSACTION_ACCOUNTED};
	
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
	// String for all GOAL_TABLE field names.
	public static final String[] GOAL_FIELDS = new String[] {G_ID, G_A_ID, GOAL_NAME, GOAL_DESCRIPTION, GOAL_TYPE, GOAL_START_AMOUNT, GOAL_DELTA_AMOUNT, GOAL_END_DATE};
	
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
	private String[] cats = new String[]{"3 - Home","4 - Daily Living","5 - Transportation","6 - Entertainment","7 - Health","8 - Vacation","9 - Recreation","10 - Dues / Subscriptions","11 - Personal","12 - Obligation","13 - Other"};

	private String[] thresh = new String[] {"30","20","10","5","5","5","5","5","5","5","5"};
	
	//Fields associated with the Alert Table.
	public static final String ALERT_TABLE = "alert_table";
	public static final String AT_ID = "_id";
	public static final String AT_A_ID = "at_a_id";
	public static final String ALERT_NAME = "alert_name";
	public static final String ALERT_DESCRIPTION = "alert_description";
	public static final String ALERT_DUE_DATE = "alert_due_date";
	
	// String for all ALERT_TABLE field names.
	public static final String[] ALERT_FIELDS = new String[] {AT_ID, AT_A_ID, ALERT_NAME, ALERT_DESCRIPTION, ALERT_DUE_DATE};
		
	//SQL Statement for creating the Profile Table.
	private final String createProfile = "CREATE TABLE IF NOT EXISTS " + PROFILE_TABLE + " ( " + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIRST_NAME + " TEXT, "	+ LAST_NAME + " TEXT, " + GENDER + " TEXT, " + BIRTHDAY + " TEXT, " + CITY + " TEXT, " + EMAIL + " TEXT);";
	
	//SQL Statement for creating the Account Table.
	private final String createAccount = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " ( " + A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NAME + " TEXT, " + ACCOUNT_NUMBER + " TEXT, " + ACCOUNT_TYPE + " TEXT, " + BALANCE + " TEXT);";
	
	//SQL Statement for creating the Transaction Table.
	private final String createTransaction = "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " ( " + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T_A_ID + " INTEGER, " + TRANSACTION_NAME + " TEXT, " + TRANSACTION_DATE + " TEXT, " + TRANSACTION_AMOUNT + " TEXT, " + TRANSACTION_CATEGORY + " TEXT, " + TRANSACTION_TYPE + " TEXT, " + TRANSACTION_INTERVAL + " TEXT, " + TRANSACTION_DESCRIPTION + " TEXT, " + TRANSACTION_ACCOUNTED + " BOOLEAN);";
	
	//SQL Statement for creating the Goal Table.
	private final String createGoal = "CREATE TABLE IF NOT EXISTS " + GOAL_TABLE + " ( " + G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + G_A_ID + " INTEGER, " + GOAL_NAME + " TEXT, " + GOAL_DESCRIPTION + " TEXT, " + GOAL_TYPE + " TEXT, " + GOAL_START_AMOUNT + " TEXT, " + GOAL_DELTA_AMOUNT + " TEXT, " + GOAL_END_DATE + " TEXT, " + "FOREIGN KEY (" + G_A_ID + ") REFERENCES " + ACCOUNT_TABLE + "(" + A_ID + "));";
	
	//SQL Statement for creating the Rec Table.
	private final String createRec = "CREATE TABLE IF NOT EXISTS " + REC_TABLE + " ( " + R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + R_CRITERIA_1 + " TEXT, " + R_CRITERIA_2 + " TEXT, " + R_CRITERIA_3 + " TEXT, " + R_CRITERIA_4 + " TEXT, " + R_CRITERIA_5 + " TEXT, " + R_IS_VALID + " BOOLEAN);";
	
	// SQL Statement for deleting an account trigger event.
	private final String deleteAccountTrigger = "CREATE TRIGGER " + DELETE_ACCOUNT_TRIGGER + " AFTER DELETE ON " + ACCOUNT_TABLE + " FOR EACH ROW BEGIN"
			+ " DELETE FROM " + GOAL_TABLE + " WHERE " + G_A_ID + " = old._id;"
			+ " DELETE FROM " + TRANSACTION_TABLE + " WHERE " + T_A_ID + " = old._id;"
			+ " END";
	//SQL Statement for creating the Alert Table.
	//private final String createAlert = "CREATE TABLE IF NOT EXISTS " + ALERT_TABLE + " ( " + AT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AT_A_ID + " INTEGER, " + ALERT_NAME + " TEXT, " + ALERT_DESCRIPTION + " TEXT,  " + ALERT_DUE_DATE + " TEXT, " + "FOREIGN KEY (" + AT_A_ID + ") REFERENCES " + ACCOUNT_TABLE + "(" + AT_ID + "));";
	private final String createAlert = "CREATE TABLE IF NOT EXISTS " + ALERT_TABLE + " ( " + AT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AT_A_ID + " INTEGER, " + ALERT_NAME + " TEXT, " + ALERT_DESCRIPTION + " TEXT,  " + ALERT_DUE_DATE + " TEXT);";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	//Creates the database using the SQL statements.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createGoal);
		db.execSQL(createProfile);
		db.execSQL(createAccount);
		db.execSQL(createTransaction);
		db.execSQL(createAlert);
		db.execSQL(deleteAccountTrigger);
		db.execSQL(createRec);
		
        //Add Recommendations to the database table.
        for (int i = 0; i <11; i++) {
        	db.execSQL("INSERT INTO " + REC_TABLE + "(criteria_one, criteria_two, is_valid) VALUES ('" + cats[i] + "', '" + thresh[i] + "', 0);");
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
		//TODO Generic Query method !!! This is probably not safe and should be controlled further with predefined queries. !!!
		// ---------------------------------------------------------------------------------------------------------------------
		public Cursor dbQuery(String query){
			
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(query, null);
				
			return cursor;
		}
		
		// ---------------------------------------------------------------------------------------------------------------------
		// Recommendation Methods
		public Cursor getRcommendations() {
			SQLiteDatabase db = this.getWritableDatabase();
			
			String query = "SELECT * FROM " + REC_TABLE;
			
			Cursor cursor = db.rawQuery(query, null);
			
			return cursor;
		}
		
		public Recommendation getRec(int id) {
			SQLiteDatabase db = this.getReadableDatabase();
			
			Cursor cursor =
					db.query(REC_TABLE, REC_FIELDS, R_ID + " = ?", new String[] {String.valueOf(id) }, null, null, null, null);
			
			if (cursor != null)
		        cursor.moveToFirst();
			
			Recommendation rec = new Recommendation();
			rec.setId(Integer.parseInt(cursor.getString(0)));
			rec.setC1(cursor.getString(1));
			rec.setC2(cursor.getString(2));
			rec.setC3(cursor.getString(3));
			rec.setC4(cursor.getString(4));
			rec.setC5(cursor.getString(5));
			 
			 Log.d("getRec("+id+")", rec.toString());
			 
			 return rec;
		}
		
		public int updateRec(Recommendation rec) {
			
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(R_CRITERIA_1,  rec.getC1());
			values.put(R_CRITERIA_2, rec.getC2());
			values.put(R_CRITERIA_3, rec.getC3());
			values.put(R_CRITERIA_4, rec.getC4());
			values.put(R_CRITERIA_5, rec.getC5());
			values.put(R_IS_VALID, rec.getIV());
			
			int i = db.update(REC_TABLE, values, R_ID + " = ?", new String[] { String.valueOf(rec.getId()) });
			
			db.close();
			
			return i;
		}
		
		public List<Recommendation> getListAllRecs() {
			List<Recommendation> recs = new LinkedList<Recommendation>();
			
			String query = "SELECT * FROM " + REC_TABLE;
			
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(query, null);
			
			Recommendation rec = null;
			if (cursor.moveToFirst()) {
				do {
					rec = new Recommendation();
					rec.setId(Integer.parseInt(cursor.getString(0)));
					rec.setC1(cursor.getString(1));
					rec.setC2(cursor.getString(2));
					rec.setC3(cursor.getString(3));
					rec.setC4(cursor.getString(4));
					rec.setC5(cursor.getString(5));
					rec.setIV(Boolean.parseBoolean(cursor.getString(6)));
					
					recs.add(rec);
				} while (cursor.moveToNext());
			}
			
			return recs;
		}
		
		// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		// Goal methods ---------------------------------------------------------------------------------------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
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
			
			db.insert(GOAL_TABLE, null, values);
			
			db.close();
		}
		
		// Get a single Goal.
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
			 
			 Log.d("getGoal("+id+")", goal.toString());
			 
			 return goal;
		}
		
		// Get a list of all Goals. 
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
					
					goals.add(goal);
				} while (cursor.moveToNext());
			}
			
			Log.d("getAllGoals()", goals.toString());
			
			return goals;
		}
		
		// For List population of Goal
		public Cursor getAllGoals() {
			SQLiteDatabase db = this.getWritableDatabase();
			
			String where = null;
			Cursor cursor = db.query(true, GOAL_TABLE, GOAL_FIELDS,  where,  null, null,  null, null, null);
			
			if (cursor != null) {
				cursor.moveToFirst();
			}
			
			return cursor;
		}
		
		//TODO Toast all Goals -- REMOVE AFTER TESTING --
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
		
		public int checkProfileExists() {
			String query = "SELECT * FROM " + PROFILE_TABLE;
			
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(query, null);
			
			return cursor.getCount();
		}
		
		//TODO Toast profile  -- REMOVE AFTER TESTING --
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
		
		// Delete the Profile.
		public void deleteProfile(Profile profile) {
			
			SQLiteDatabase db = this.getWritableDatabase();
			
			db.delete(PROFILE_TABLE, P_ID+"= ?", new String[] { String.valueOf(profile.getId()) });
			
			db.close();		
		}
		
		// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		// Account methods ------------------------------------------------------------------------------------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
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
		
		// For List population of Account ListView
		public Cursor getAllAccounts() {
			SQLiteDatabase db = this.getWritableDatabase();
			
			String where = null;
			Cursor cursor = db.query(true, ACCOUNT_TABLE, ACCOUNT_FIELDS,  where,  null, null,  null, null, null);
			
			if (cursor != null) {
				cursor.moveToFirst();
			}
			
			return cursor;
		}
		
		//TODO Toast all Accounts -- REMOVE AFTER TESTING --
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
		
		// Check if a transaction date is on or before today.
		public boolean checkAccountedDate(String transDate) {
			java.util.Date d = Calendar.getInstance().getTime(); // Current time
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Set your date format
			String currentDate = sdf.format(d); // Get Date String according to date format
			
			java.util.Date date = null;
			java.util.Date today = null;
			
			try {
	            date = sdf.parse(transDate);
	            today = sdf.parse(currentDate);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
			
			if (date.before(today) || date.equals(today)) {
				return true;
			} else {
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
					 
			 Log.d("getTransaction("+id+")", transaction.toString());
					 
			 return transaction;
		}
				
		// Get a list of all Transactions.
		public List<Transaction> getAllListTransactions() {
			List<Transaction> transactions = new LinkedList<Transaction>();
					
			String query = "SELECT * FROM " + TRANSACTION_TABLE;
			
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(query, null);
					
			Transaction transaction = null;
			if (cursor.moveToFirst()) {
				do {
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
					transaction.setAccounted(Boolean.parseBoolean(cursor.getString(9))); // Updated to incorporate new transaction method.
							
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
			if(a_id != 0) {
				where = T_A_ID+"="+a_id;
			}
			Cursor cursor = db.query(true, TRANSACTION_TABLE, TRANSACTION_FIELDS,  where,  null, null,  having, null, null);
					
			if (cursor != null) {
				cursor.moveToFirst();
			}
					
			return cursor;
		}
		
		//TODO Toast all Transactions -- REMOVE AFTER TESTING --
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
		
}
