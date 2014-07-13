package com.example.budgetnotebook;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
	//Name of the database storing the tables for the Budget Notebook application.
	public static final String DATABASE_NAME = "BudgetNotebook.db";
	public static final int VERSION = 1;
	
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
	// String for all TRANSACTION_TABLE field names.
	public static final String[] TRANSACTION_FIELDS = new String[] {T_ID, T_A_ID, TRANSACTION_NAME, TRANSACTION_DATE, TRANSACTION_AMOUNT, TRANSACTION_CATEGORY, TRANSACTION_TYPE, TRANSACTION_INTERVAL, TRANSACTION_DESCRIPTION};
	
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
	
	//SQL Statement for creating the Profile Table.
	private final String createProfile = "CREATE TABLE IF NOT EXISTS " + PROFILE_TABLE + " ( " + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIRST_NAME + " TEXT, "	+ LAST_NAME + " TEXT, " + GENDER + " TEXT, " + BIRTHDAY + " TEXT, " + CITY + " TEXT, " + EMAIL + " TEXT);";
	
	//SQL Statement for creating the Account Table.
	private final String createAccount = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " ( " + A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NAME + " TEXT, " + ACCOUNT_NUMBER + " TEXT, " + ACCOUNT_TYPE + " TEXT, " + BALANCE + " TEXT);";
	
	//SQL Statement for creating the Transaction Table.
	private final String createTransaction = "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " ( " + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T_A_ID + " INTEGER, " + TRANSACTION_NAME + " TEXT, " + TRANSACTION_DATE + " TEXT, " + TRANSACTION_AMOUNT + " TEXT, " + TRANSACTION_CATEGORY + " TEXT, " + TRANSACTION_TYPE + " TEXT, " + TRANSACTION_INTERVAL + " TEXT, " + TRANSACTION_DESCRIPTION + " TEXT);";
	
	//SQL Statement for creating the Goal Table.
	private final String createGoal = "CREATE TABLE IF NOT EXISTS " + GOAL_TABLE + " ( " + G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + G_A_ID + " INTEGER, " + GOAL_NAME + " TEXT, " + GOAL_DESCRIPTION + " TEXT, " + GOAL_TYPE + " TEXT, " + GOAL_START_AMOUNT + " TEXT, " + GOAL_DELTA_AMOUNT + " TEXT, " + GOAL_END_DATE + " TEXT, " + "FOREIGN KEY (" + G_A_ID + ") REFERENCES " + ACCOUNT_TABLE + "(" + A_ID + "));";
		
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
	}

	//Tells the system what to do when the DB is updated.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		// Drop older tables if they exist
		db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE); // Must be created before GOAL_TABLE for foreign key purposes.
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
        
        // create fresh database table
        this.onCreate(db);
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
		
		// Toast all Goals -- REMOVE AFTER TESTING --
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
		
		// Toast profile  -- REMOVE AFTER TESTING --
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
		
		// Get a list of all Accounts.
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
		
		// 
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
		
		// Toast all Accounts
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
		
		// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		// Transaction methods ---------------------------------------------------------------------------------------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
				
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
					
			Transaction transaction = new Transaction();
			transaction.setId(Integer.parseInt(cursor.getString(0)));
			transaction.setAId(Integer.parseInt(cursor.getString(1)));
			transaction.setName(cursor.getString(2));
			transaction.setDate(cursor.getString(3));
			transaction.setAmount(cursor.getString(4));
			transaction.setCategory(cursor.getString(5));
			transaction.setType(cursor.getString(6));
			transaction.setInterval(cursor.getString(7));
			transaction.setDescription(cursor.getString(7));
					 
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
		
		// Toast all Transactions
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
		// Other methods --------------------------------------------------------------------------------------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
}
