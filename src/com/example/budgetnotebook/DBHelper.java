package com.example.budgetnotebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	//Name of the database storing the tables for the Budget Notebook application.
	public static final String DATABASE_NAME = "budget_notebook";
	
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
	
	//SQL Statement for creating the application database.
	public final String createDB = createProfile + " "
			+ createAccount + " "
			+ createTransaction;
	
	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, version);
	}

	//Creates the database using the SQL statement in createDB string.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createDB);
	}

	//Tells the system to do nothing when the DB is updated.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		//Do Nothing.
	}

}