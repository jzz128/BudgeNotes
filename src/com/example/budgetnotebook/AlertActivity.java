package com.example.budgetnotebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlertActivity extends Activity{
	// UI Components
	Button addGoal;
	DBHelper db;
	RelativeLayout vwParentRow;
	ListView alertList;
	Alert alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set to view goals layout
		setContentView(R.layout.activity_alert);

		//Create Database instance
		db = new DBHelper(getBaseContext());
				
		// Populate the ListView
		populateListViewAlerts();	

	};

	// This method uses the Cursor getAllAlerts and populates the ListView on the Alert_Activity layout with a list of template_list_alert (layouts)
	@SuppressWarnings("deprecation")
	private void populateListViewAlerts() {
	
		// Set a cursor with all the Goals
		Cursor cursor = db.getAllAlerts();
		
		// Map the GOAL_TABLE fields to the TextViews on the template_list_goal layout.
		String[] goalFieldNames = new String[] {DBHelper.AT_ID, DBHelper.AT_A_ID, DBHelper.ALERT_DUE_DATE,DBHelper.ALERT_NAME, DBHelper.ALERT_DESCRIPTION};
		int[] toViewIDs = new int[] {R.id.alertID,R.id.alertAccountID, R.id.alertEndDate, R.id.alertName, R.id.alertDescrip};
	
		// Fills the ListView with all the Goals in the Table.
		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.template_list_alert,
				cursor,
				goalFieldNames,
				toViewIDs
				);
		alertList = (ListView) findViewById(R.id.listViewAlerts);
		alertList.setAdapter(myCursorAdapter);

	}
	
	public void deleteAlertClickHandler (View v) {
		int at_id;
		
		// Get the row the clicked button is in
        vwParentRow = (RelativeLayout)v.getParent();
        
        // Get the object that the transaction and account ID are stored in
        TextView child = (TextView)vwParentRow.getChildAt(1); // Updated to the 2 index, to correspond with the correct ID from the template_list. - DJM
        
        // Store the goal id in the variable integers.
        at_id = Integer.parseInt((child.getText().toString().trim()));
		
        // Initialize the objects.
        alert = db.getAlert(at_id);
        
		// Alert dialog to confirm delete.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                	// YES Clicked
	                case DialogInterface.BUTTON_POSITIVE:
	                	db.deleteAlert(alert);
	                	populateListViewAlerts();
	                    break;
	                 // No Clicked
	                case DialogInterface.BUTTON_NEGATIVE:
	                default:
	                	//Do Nothing.
	                    break;
                }
            }
        };
        
        // Alert Dialog Popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete alert? \n ** THIS CAN NOT BE UNDONE!").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
	}
}