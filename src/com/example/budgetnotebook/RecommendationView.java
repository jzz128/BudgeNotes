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

public class RecommendationView extends Activity {
	// UI Components
	ListView recTable;
	Spinner recAccount;
	String[] seperatedAccount;
	int A_ID;
	DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_recommendations);
		db = new DBHelper(getBaseContext());
		
		int lowestID;
        lowestID = db.lowestAccountID();
        
		// Get the extras from previous activity.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",lowestID);
		
		// Initialize the Spinners.
		recAccount = (Spinner) findViewById(R.id.recAccountSpinner);
		
		// Load the spinner data.
		loadAccountSpinnerData();
		
		populateRecTable();
		
		//Set listener for Account Spinner selection.
		recAccount.setOnItemSelectedListener(new OnItemSelectedListener() {
							
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				///Get the Account spinner data and put it in a string array.
				seperatedAccount = recAccount.getSelectedItem().toString().split(" ");
				A_ID = Integer.parseInt(seperatedAccount[0]);
				
				populateRecTable();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
		});
	}
	
	// 
		@SuppressWarnings("deprecation")
		private void populateRecTable() {
			String query;
			String recQuery;
			String transTypeS, sumQuery;
			
			Cursor reportCursor;
			Cursor recCursor;
			List<Recommendation> recs;
			
			float count;
			int catCount;
			int recCount;
			int i;
			
			transTypeS = String.valueOf(R.drawable.debit1);
			
			// Get the count of total transactions.
			sumQuery = "SELECT * FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + A_ID + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1 + " AND " + DBHelper.TRANSACTION_TYPE + " = " + transTypeS;
			count = db.querySum(sumQuery);
						
			// Set the query to get stats on the transactions for the current account.
			query = "SELECT *, SUM(CAST (" + DBHelper.TRANSACTION_AMOUNT + " AS FLOAT)), (SUM(CAST (" + DBHelper.TRANSACTION_AMOUNT + " AS FLOAT)) / " + count + ") * 100 AS PERCENTAGE FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + A_ID + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1 + " AND " + DBHelper.TRANSACTION_TYPE + " = " + transTypeS + " GROUP BY " + DBHelper.TRANSACTION_CATEGORY;
			
			// Set the recQuery to get the true recommendations when needed.
			recQuery = "SELECT * FROM " + DBHelper.REC_TABLE + " WHERE " + DBHelper.R_IS_VALID + " = 1";
			
			// Fill cursor for the transaction stats using the set query.
			reportCursor = db.dbQuery(query);
			
			// Fill cursor with all recommendations
			recCursor = db.getRecommendations();
			
			// Get the total unique categories and recommendations
			catCount = reportCursor.getCount();
			Log.d("catCount", Integer.toString(catCount));
			recCount = recCursor.getCount();
			Log.d("recCount", Integer.toString(recCount));
			
			recs = db.getAllRecommendations();
			i = 0;
			
			for (int j = 0; j < recs.size(); j++) {
				recs.get(j).setIsValid(false);
				db.updateRecommendation(recs.get(j));
			}
			
			if (reportCursor.moveToFirst()) {
				do {
					if (recCursor.moveToFirst()) {
						do {
							if (recCursor.getString(1).equals(reportCursor.getString(5)) && Float.parseFloat(recCursor.getString(2)) <= Float.parseFloat(reportCursor.getString(13))) { // Updated to incorporate new transaction method.
								// Set the Recommendation to True.
								//Toast.makeText(this, "Recommendation found; " + i, Toast.LENGTH_LONG).show();
								recs.get(i).setIsValid(true);
								db.updateRecommendation(recs.get(i));
							}
							
							i++;
						} while (recCursor.moveToNext());
					} else {
						Toast.makeText(this, "No Recommendation criteria established.", Toast.LENGTH_LONG).show();
					}
					
					i = 0;
				} while (reportCursor.moveToNext());
			} else {
				Toast.makeText(this, "No Recommendations at this time.", Toast.LENGTH_LONG).show();
			}
			
			recCursor = db.dbQuery(recQuery);
			//startManagingCursor(cursor);
			
			// Map the recommendation fields to the TextViews on the template_row_recommendation layout.
			String[] transFieldNames = new String[] {DBHelper.R_CRITERIA_1};
			int[] toViewIDs = new int[] {R.id.recMessage};
		
			// Fills the ListView with all the Goals in the Table.
			SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
					this,
					R.layout.template_row_recommendations,
					recCursor,
					transFieldNames,
					toViewIDs);
			
			recTable = (ListView) findViewById(R.id.listViewRecommendations);
			recTable.setAdapter(myCursorAdapter); 
	}
		
	private void loadAccountSpinnerData() {
		List<String> list = db.getAllStringAccounts();
	       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
	                    (this, android.R.layout.simple_spinner_item,list);
	                      
	       dataAdapter.setDropDownViewResource
	                    (android.R.layout.simple_spinner_dropdown_item);
		 recAccount.setAdapter(dataAdapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		finish();
	}
}