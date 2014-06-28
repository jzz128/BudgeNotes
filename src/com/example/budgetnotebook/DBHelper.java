package com.example.budgetnotebook;

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
	
	//Fields associated with the Account Table.
	public static final String ACCOUNT_TABLE = "account_table";
	public static final String A_ID = "_id";
	public static final String ACCOUNT_NAME = "account_name";
	public static final String ACCOUNT_NUMBER = "account_number";
	public static final String ACCOUNT_TYPE = "account_type";
	public static final String BALANCE = "balance";

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

	/**
	//SQL Statement for creating the Profile Table.
	private final String createProfile = "create table if not exists " + PROFILE_TABLE + " ( "
			+ P_ID + "integer primary key autoincrement, "
			+ FIRST_NAME + "text, "
			+ LAST_NAME + "text, "
			+ GENDER + "text, "
			+ BIRTHDAY + "text, "
			+ CITY + "text, "
			+ EMAIL + "text);";
	
	//SQL Statement for creating the Account Table.
	private final String createAccount = "create table if not exists " + ACCOUNT_TABLE + " ( "
			+ A_ID + "integer primary key autoincrement, "
			+ ACCOUNT_NAME + "text, "
			+ ACCOUNT_NUMBER + "text, "
			+ ACCOUNT_TYPE + "text, "
			+ BALANCE + "text);";
	
	//SQL Statement for creating the Transaction Table.
	private final String createTransaction = "create table if not exists " + TRANSACTION_TABLE + " ( "
			+ T_ID + "integer primary key autoincrement, "
			+ T_A_ID + "integer references account_table(_id)"
			+ TRANSACTION_NAME + "text, "
			+ TRANSACTION_DATE + "text, "
			+ TRANSACTION_AMOUNT + "text, "
			+ TRANSACTION_CATEGORY + "text, "
			+ TRANSACTION_TYPE + "text, "
			+ TRANSACTION_INTERVAL + "text, "
			+ TRANSACTION_DESCRIPTION + "text);";
	**/
	
	//SQL Statement for creating the Goal Table.
	private final String createGoal = "CREATE TABLE IF NOT EXISTS " + GOAL_TABLE + " ( " + G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GOAL_NAME + " TEXT, " + GOAL_DESCRIPTION + " TEXT, " + GOAL_TYPE + " TEXT, " + GOAL_START_AMOUNT + " TEXT, " + GOAL_DELTA_AMOUNT + " TEXT, " + GOAL_END_DATE + " TEXT);";
	
	//SQL Statement for creating the application database. REMOVED ALL TABLES BUT GOAL FOR TESTING!!! -----------------------------------------------
	public final String createDB = createGoal;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	//Creates the database using the SQL statement in createDB string.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createDB);
	}

	//Tells the system what to do when the DB is updated.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		// Drop older goal_table table if existed
        db.execSQL("DROP TABLE IF EXISTS goal_table");
 
        // create fresh goal_table table
        this.onCreate(db);
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Goal methods ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	// Add a single Goal.
	public void addGoal(Goal goal){
		Log.d("addGoal", goal.toString());
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
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
				db.query(GOAL_TABLE, new String[] {GOAL_NAME, GOAL_DESCRIPTION, GOAL_TYPE, GOAL_START_AMOUNT, GOAL_DELTA_AMOUNT, GOAL_END_DATE}, " id = ?", new String[] {String.valueOf(id) }, null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToFirst();
		
		Goal goal = new Goal();
		 goal.setId(Integer.parseInt(cursor.getString(0)));
		 goal.setName(cursor.getString(1));
		 goal.setDescription(cursor.getString(2));
		 goal.setType(cursor.getString(3));
		 goal.setStartAmount(cursor.getString(4));
		 goal.setDeltaAmount(cursor.getString(5));
		 goal.setEndDate(cursor.getString(6));
		 
		 Log.d("getGoal("+id+")", goal.toString());
		 
		 return goal;
	}
	
	// Get a list of all Goals.
	public List<Goal> getAllGoals() {
		List<Goal> goals = new LinkedList<Goal>();
		
		String query = "SELECT * FROM " + GOAL_TABLE;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Goal goal = null;
		if (cursor.moveToFirst()) {
			do {
				goal = new Goal();
				goal.setId(Integer.parseInt(cursor.getString(0)));
				//goal.setAId(Integer.parseInt(cursor.getString(1)));
				goal.setName(cursor.getString(1));
				goal.setDescription(cursor.getString(2));
				goal.setType(cursor.getString(3));
				goal.setStartAmount(cursor.getString(4));
				goal.setDeltaAmount(cursor.getString(5));
				goal.setEndDate(cursor.getString(6));
				
				goals.add(goal);
			} while (cursor.moveToNext());
		}
		
		Log.d("getAllGoals()", goals.toString());
		
		return goals;
	}
	
	// Toast all Goals
	public void toastGoal(Context context){
		String query = "SELECT * FROM " + GOAL_TABLE;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(context, cursor.getString(1), Toast.LENGTH_SHORT).show();		
				
			} while (cursor.moveToNext());
		}
		else {
			Toast.makeText(context, "No records yet!", Toast.LENGTH_SHORT).show();
		}
	}
	// Update a single Goal.
	public int updateGoal(Goal goal) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
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
	// Other methods ----------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
}
