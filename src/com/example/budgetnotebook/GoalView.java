package com.example.budgetnotebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GoalView extends Activity{
	// Setup global variables
	Button addGoal;
	DBHelper db;
	
	RelativeLayout vwParentRow;
	ListView goalList;
	Goal goal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set to view goals layout
		setContentView(R.layout.view_goals);

		//Create Database instance
		db = new DBHelper(getBaseContext());
					
		// Populate the ListView
		populateListViewGoals();	

		// Set the ADD GOAL button to display the ADD Goal form when clicked
		addGoal = (Button) findViewById(R.id.addGoal);
		addGoal.setOnClickListener(new View.OnClickListener() {		
		
		// Display Goal Form when ADD GOAL
		@Override
			public void onClick(View v) {
				try{
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.GoalForm");
					Intent newIntent = new Intent(GoalView.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});
	};
	

	// This method uses the Cursor getAllGoals and populates the ListView on the view_goals layout with a list of template_list_goal (layouts)
	@SuppressWarnings("deprecation")
	private void populateListViewGoals() {
	
		// Set a cursor with all the Goals
		Cursor cursor = db.getAllGoals();
		
		// Map the GOAL_TABLE fields to the TextViews on the template_list_goal layout.
		String[] goalFieldNames = new String[] {DBHelper.G_ID,DBHelper.G_A_ID, DBHelper.GOAL_NAME, DBHelper.GOAL_DESCRIPTION, DBHelper.GOAL_TYPE, DBHelper.GOAL_END_DATE};
		int[] toViewIDs = new int[] {R.id.goalID,R.id.goalAccount, R.id.goalName, R.id.goalDescription, R.id.goalType, R.id.goalEnd};
	
		// Fills the ListView with all the Goals in the Table.
		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.template_list_goal,
				cursor,
				goalFieldNames,
				toViewIDs
				);
		goalList = (ListView) findViewById(R.id.listViewGoals);
		goalList.setAdapter(myCursorAdapter);

	}

	// Functionality performed when edit goal is clicked	
	public void editGoalClickHandler(View v) {
		int g_id;
		int a_id;
		
		// Get the row the clicked button is in
        vwParentRow = (RelativeLayout)v.getParent();
        
        // Get the object that the goal ID are stored in
        TextView child = (TextView)vwParentRow.getChildAt(1);
        TextView child2 = (TextView)vwParentRow.getChildAt(2);
        
        // Store the goal id in the variable integers.
        a_id = Integer.parseInt((child.getText().toString().trim())); // The value stored in the this child is the 
        g_id = Integer.parseInt((child2.getText().toString().trim()));
        
        // Display Goal Form
        try {
        	Class<?> clickedClass = Class.forName("com.example.budgetnotebook.GoalForm");
        	Intent newIntent = new Intent(GoalView.this,clickedClass);

			// Brings application back to the root activity, where exit functions properly.
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Pass the extras to the intent on GoalForm.
			newIntent.putExtra("G_ID", g_id);
        	newIntent.putExtra("A_ID", a_id);
        	newIntent.putExtra("G_EDIT", true);
        	startActivity(newIntent);
        	
        } catch(ClassNotFoundException e) {
        	e.printStackTrace();
        }
	}
	
	// Functionality performed when delete goal is clicked	
	public void deleteGoalClickHandler(View v) {
		int g_id;
		
		// Get the row the clicked button is in
        vwParentRow = (RelativeLayout)v.getParent();
        
        // Get the object that the transaction and account ID are stored in
        TextView child = (TextView)vwParentRow.getChildAt(2); // Updated to the 2 index, to correspond with the correct ID from the template_list. - DJM
        
        // Store the goal id in the variable integers.
        g_id = Integer.parseInt((child.getText().toString().trim()));
		
        // Initialize the objects.
        goal = db.getGoal(g_id);
        
		// Alert dialog to confirm delete.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                	// YES Clicked
	                case DialogInterface.BUTTON_POSITIVE:
	                	db.deleteGoal(goal);
	                	populateListViewGoals();
	                    break;
	                 // No Clicked
	                case DialogInterface.BUTTON_NEGATIVE:
	                    //Do Nothing.
	                    break;
                }
            }
        };
        // Alert Dialog Popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete goal?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
	}
}
