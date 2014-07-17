package com.example.budgetnotebook;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Report extends Activity {
	ListView reportTable;
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
		
		populateReportTable();
		
		//Set listener for Account Spinner selection.
				transAccount.setOnItemSelectedListener(new OnItemSelectedListener() {
									
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						///Get the Account spinner data and put it in a string array.
						seperatedAccount = transAccount.getSelectedItem().toString().split(" ");
						A_ID = Integer.parseInt(seperatedAccount[0]);
						populateReportTable();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub		
					}
				});
	}
	
	// This method uses the Cursor getAllGoals and populates the ListView on the view_goals layout with a list of template_list_goal (layouts)
		@SuppressWarnings("deprecation")
		private void populateReportTable() {

			String query;
			Cursor cursor;
			Cursor cCursor;
			
			int count;
			
			cCursor = db.getAllTransactions(A_ID);
			count = cCursor.getCount();
			
			query = "SELECT *, COUNT(*),(CAST (COUNT(*) AS FLOAT) / " + count + ") * 100 AS PERCENTAGE FROM " + db.TRANSACTION_TABLE + " WHERE " + db.T_A_ID + " = " + A_ID + " GROUP BY " + db.TRANSACTION_CATEGORY;
			
			cursor = db.dbQuery(query);
			cursor.moveToFirst();
			/*
			if (cursor != null) {
		        cursor.moveToFirst();
				Toast.makeText(this, cursor.getString(0), Toast.LENGTH_LONG).show();
				Toast.makeText(this, cursor.getString(1), Toast.LENGTH_LONG).show();
				Toast.makeText(this, cursor.getColumnName(0), Toast.LENGTH_LONG).show();
				Toast.makeText(this, cursor.getColumnName(1), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "No Transactions!", Toast.LENGTH_LONG).show();
			} */
			//startManagingCursor(cursor);
			
			// Map the GOAL_TABLE fields to the TextViews on the template_list_goal layout.
			String[] transFieldNames = new String[] {db.TRANSACTION_CATEGORY, cursor.getColumnName(9), cursor.getColumnName(10)};
			int[] toViewIDs = new int[] {R.id.catName, R.id.catCount, R.id.catPercent};
		
			// Fills the ListView with all the Goals in the Table.
			SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
					this,
					R.layout.template_row_report,
					cursor,
					transFieldNames,
					toViewIDs
					);
			reportTable = (ListView) findViewById(R.id.reportTable);
			reportTable.setAdapter(myCursorAdapter); 
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
