package com.example.budgetnotebook;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Report extends Activity {
	ListView reportTableCount;
	ListView reportTableAmount;
	
	Spinner transAccount;
	String[] seperatedAccount;
	int A_ID;
	DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_report);
		db = new DBHelper(getBaseContext());
		
		int lowestID;
        lowestID = db.lowestAccountID();
        
		// Get the extras from previous activity.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",lowestID);
		
		// Initialize the Spinners.
		transAccount = (Spinner) findViewById(R.id.reportAccountSpinner);
		
		// Load the spinner data.
		loadAccountSpinnerData();
		
		populateReportCountTable();
		populateReportAmountCTable();
		populateReportAmountDTable();
		
		//Set listener for Account Spinner selection.
				transAccount.setOnItemSelectedListener(new OnItemSelectedListener() {
									
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						///Get the Account spinner data and put it in a string array.
						seperatedAccount = transAccount.getSelectedItem().toString().split(" ");
						A_ID = Integer.parseInt(seperatedAccount[0]);
						populateReportCountTable();
						populateReportAmountCTable();
						populateReportAmountDTable();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub		
					}
				});
	}
	
	// 
	@SuppressWarnings("deprecation")
	private void populateReportCountTable() {

		String query;
		Cursor cursor;
		Cursor cCursor;
			
		int count;
			
		cCursor = db.getAllTransactions(A_ID);
		count = cCursor.getCount();
			
		query = "SELECT *, COUNT(*),(CAST (COUNT(*) AS FLOAT) / " + count + ") * 100 AS PERCENTAGE FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + A_ID + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1 + " GROUP BY " + DBHelper.TRANSACTION_CATEGORY;
			
		cursor = db.dbQuery(query);
		cursor.moveToFirst();
			
		//startManagingCursor(cursor);
			
		// Map the GOAL_TABLE fields to the TextViews on the template_list_goal layout.
		// Updated for new transaction method.
		String[] transFieldNames = new String[] {DBHelper.TRANSACTION_CATEGORY, cursor.getColumnName(11), cursor.getColumnName(12)};
		int[] toViewIDs = new int[] {R.id.catName, R.id.catStat, R.id.catPercent};
		
		// 
		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.template_row_report,
				cursor,
				transFieldNames,
				toViewIDs
				);
		reportTableCount = (ListView) findViewById(R.id.reportTableCount);
		reportTableCount.setAdapter(myCursorAdapter); 
	}
	
	// 
		@SuppressWarnings("deprecation")
		private void populateReportAmountCTable() {

			String query, sumQuery, transTypeS;
			Cursor cursor;
				
			float count;

			transTypeS = String.valueOf(R.drawable.credit1);
			//transTypeS = String.valueOf(R.drawable.debit1);
			
			sumQuery = "SELECT * FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + A_ID + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1 + " AND " + DBHelper.TRANSACTION_TYPE + " = " + transTypeS;
			count = db.querySum(sumQuery);
			
			query = "SELECT *, SUM(CAST (" + DBHelper.TRANSACTION_AMOUNT + " AS FLOAT)), (SUM(CAST (" + DBHelper.TRANSACTION_AMOUNT + " AS FLOAT)) / " + count + ") * 100 AS PERCENTAGE FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + A_ID + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1 + " AND " + DBHelper.TRANSACTION_TYPE + " = " + transTypeS + " GROUP BY " + DBHelper.TRANSACTION_CATEGORY;
						
			cursor = db.dbQuery(query);
			
			cursor.moveToFirst();
				
			// Updated for new transaction method.
			String[] transFieldNames = new String[] {DBHelper.TRANSACTION_CATEGORY, cursor.getColumnName(11), cursor.getColumnName(12)};
			int[] toViewIDs = new int[] {R.id.catName, R.id.catStat, R.id.catPercent};
			
			// Fills the ListView with all the Goals in the Table.
			SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
					this,
					R.layout.template_row_report,
					cursor,
					transFieldNames,
					toViewIDs
					);
			reportTableAmount = (ListView) findViewById(R.id.reportTableAmountC);
			reportTableAmount.setAdapter(myCursorAdapter); 
		}

		// 
		@SuppressWarnings("deprecation")
		private void populateReportAmountDTable() {

			String query, sumQuery, transTypeS;
			Cursor cursor;
				
			float count;
					
			// transTypeS = String.valueOf(R.drawable.credit1);
			transTypeS = String.valueOf(R.drawable.debit1);
					
			sumQuery = "SELECT * FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + A_ID + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1 + " AND " + DBHelper.TRANSACTION_TYPE + " = " + transTypeS;
			count = db.querySum(sumQuery);
					
			query = "SELECT *, SUM(CAST (" + DBHelper.TRANSACTION_AMOUNT + " AS FLOAT)), (SUM(CAST (" + DBHelper.TRANSACTION_AMOUNT + " AS FLOAT)) / " + count + ") * 100 AS PERCENTAGE FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + A_ID + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1 + " AND " + DBHelper.TRANSACTION_TYPE + " = " + transTypeS + " GROUP BY " + DBHelper.TRANSACTION_CATEGORY;
								
			cursor = db.dbQuery(query);
					
			cursor.moveToFirst();
						
			// Updated for new transaction method.
			String[] transFieldNames = new String[] {DBHelper.TRANSACTION_CATEGORY, cursor.getColumnName(11), cursor.getColumnName(12)};
			int[] toViewIDs = new int[] {R.id.catName, R.id.catStat, R.id.catPercent};
					
			// Fills the ListView with all the Goals in the Table.
			SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
					this,
					R.layout.template_row_report,
					cursor,
					transFieldNames,
					toViewIDs
					);
			reportTableAmount = (ListView) findViewById(R.id.reportTableAmountD);
			reportTableAmount.setAdapter(myCursorAdapter); 
		}
		
	private void loadAccountSpinnerData() {
		List<String> list = db.getAllStringAccounts();
	       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
	                    (this, android.R.layout.simple_spinner_item,list);
	                      
	       dataAdapter.setDropDownViewResource
	                    (android.R.layout.simple_spinner_dropdown_item);
		 transAccount.setAdapter(dataAdapter);
	}
}
