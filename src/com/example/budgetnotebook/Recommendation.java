package com.example.budgetnotebook;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Recommendation extends Activity {
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
	
	// This method uses the Cursor getAllGoals and populates the ListView on the view_goals layout with a list of template_list_goal (layouts)
		@SuppressWarnings("deprecation")
		private void populateRecTable() {
			Recommendation rec;
			String query;
			String recQuery;
			
			Cursor reportCursor;
			Cursor recCursor;
			Cursor cCursor;
			List<Recommendation> recs;
			
			int count;
			int catCount;
			int recCount;
			int i;
			// Get the count of total transactions.
			cCursor = db.getAllTransactions(A_ID);
			count = cCursor.getCount();
			
			// Set the query to get stats on the transactions for the current account.
			query = "SELECT *, COUNT(*),(CAST (COUNT(*) AS FLOAT) / " + count + ") * 100 AS PERCENTAGE FROM " + db.TRANSACTION_TABLE + " WHERE " + db.T_A_ID + " = " + A_ID + " GROUP BY " + db.TRANSACTION_CATEGORY;
			
			// Set the recQuery to get the true recommendations when needed.
			recQuery = "SELECT * FROM " + db.REC_TABLE + " WHERE " + db.R_IS_VALID + " = 1";
			
			// Fill cursor for the transaction stats using the set query.
			reportCursor = db.dbQuery(query);
			
			// Fill cursor with all recommendations
			recCursor = db.getRcommendations();
			
			// Get the total unique categories and recommendations
			catCount = reportCursor.getCount();
			recCount = recCursor.getCount();
			
			recs = db.getListAllRecs();
			i = 0;
			
			for (int j = 0; j < recs.size(); j++) {
				recs.get(j).setIV(false);
				db.updateRec(recs.get(j));
			}
			if (reportCursor.moveToFirst()) {
				do {
					if (recCursor.moveToFirst()) {
						do {
							if (recCursor.getString(1).equals(reportCursor.getString(5)) && Float.parseFloat(recCursor.getString(2)) <= Float.parseFloat(reportCursor.getString(10))) {
								// Set the Recommendation to True.
								//Toast.makeText(this, "Recommendation found; " + i, Toast.LENGTH_LONG).show();
								recs.get(i).setIV(true);
								db.updateRec(recs.get(i));
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
			
			// Map the GOAL_TABLE fields to the TextViews on the template_list_goal layout.
			String[] transFieldNames = new String[] {db.R_CRITERIA_1};
			int[] toViewIDs = new int[] {R.id.recMessage};
		
			// Fills the ListView with all the Goals in the Table.
			SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
					this,
					R.layout.template_row_recommendation,
					recCursor,
					transFieldNames,
					toViewIDs
					);
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
	
	private int _id;
	private String c1;
	private String c2;
	private String c3;
	private String c4;
	private String c5;
	private boolean iv;
	
	public Recommendation() {}
	
	public Recommendation (String c1, String c2, String c3, String c4, String c5, boolean iv) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.c5 = c5;
		this.iv = iv;
	}
	//Getters --------------------------------------------------------------------
		public int getId(){
			return _id;
		}
		
		public String getC1(){
			return c1;
		}
		
		public String getC2(){
			return c2;
		}
		
		public String getC3(){
			return c3;
		}
		
		public String getC4(){
			return c4;
		}
		
		public String getC5(){
			return c5;
		}
		
		public boolean getIV(){
			return iv;
		}
		
		//Setters --------------------------------------------------------------------
		public void setId(int id){
			this._id = id;
		}
		
		public void setC1(String c1) {
			this.c1 = c1;
		}

		public void setC2(String c2) {
			this.c2 = c2;
		}

		public void setC3(String c3) {
			this.c3 = c3;
		}

		public void setC4(String c4) {
			this.c4 = c4;
		}

		public void setC5(String c5) {
			this.c5 = c5;
		}

		public void setIV(boolean iv) {
			this.iv = iv;
		}
}
