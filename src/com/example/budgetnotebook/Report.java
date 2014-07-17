package com.example.budgetnotebook;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

public class Report extends Activity {
	ListView reportTable;
	DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_report);
		db = new DBHelper(getBaseContext());
		populateReportTable();
	}
	
	// This method uses the Cursor getAllGoals and populates the ListView on the view_goals layout with a list of template_list_goal (layouts)
		@SuppressWarnings("deprecation")
		private void populateReportTable() {

			String query;
			query = "SELECT " + db.TRANSACTION_CATEGORY + ", COUNT(*) AS CATCOUNT FROM " + db.TRANSACTION_TABLE + " WHERE " + db.T_A_ID + " = 1 GROUP BY " + db.TRANSACTION_CATEGORY;
			
			Cursor cursor = db.dbQuery(query);
			if (cursor != null) {
		        cursor.moveToFirst();
				Toast.makeText(this, cursor.getString(0), Toast.LENGTH_LONG).show();
				Toast.makeText(this, cursor.getString(1), Toast.LENGTH_LONG).show();
				Toast.makeText(this, cursor.getColumnName(0), Toast.LENGTH_LONG).show();
				Toast.makeText(this, cursor.getColumnName(1), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "No Transactions!", Toast.LENGTH_LONG).show();
			}
			//startManagingCursor(cursor);
			/*
			// Map the GOAL_TABLE fields to the TextViews on the template_list_goal layout.
			String[] goalFieldNames = new String[] {db.TRANSACTION_CATEGORY, "CATCOUNT"};
			int[] toViewIDs = new int[] {R.id.catName, R.id.catCount};
		
			// Fills the ListView with all the Goals in the Table.
			SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
					this,
					R.layout.template_row_report,
					cursor,
					goalFieldNames,
					toViewIDs
					);
			reportTable = (ListView) findViewById(R.id.reportTable);
			reportTable.setAdapter(myCursorAdapter); 
			*/

	}
}
