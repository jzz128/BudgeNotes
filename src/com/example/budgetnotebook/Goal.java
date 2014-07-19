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

public class Goal extends Activity{
	
	Button addGoal;
	DBHelper db;
	
	RelativeLayout vwParentRow;
	ListView goalList; // moved to higher scope. - DJM
	Goal goal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_goals);

		//Create Database instance
		db = new DBHelper(getBaseContext());
					
		// Populate the ListView
		populateListViewGoals();	

		// Set the ADD GOAL button to display the ADD Goal form when clicked
		addGoal = (Button) findViewById(R.id.addGoal);
		addGoal.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
				try{
					Class clickedClass = Class.forName("com.example.budgetnotebook.GoalForm");
					Intent newIntent = new Intent(Goal.this, clickedClass);
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
		String[] goalFieldNames = new String[] {db.G_ID,db.G_A_ID, db.GOAL_NAME, db.GOAL_DESCRIPTION, db.GOAL_TYPE, db.GOAL_END_DATE};
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
		
	private int _id;
	private int a_id;
	private String goal_name;
	private String goal_description;
	private String goal_type;
	private String goal_start_amount;
	private String goal_delta_amount;
	private String goal_end_date;
	
	public Goal(){}
	
	public Goal(int a_id, String name, String description, String type, String start_amount, String delta_amount, String end_date) {
		super();
		this.a_id = a_id;
		this.goal_name = name;
		this.goal_description = description;
		this.goal_type = type;
		this.goal_start_amount = start_amount;
		this.goal_delta_amount = delta_amount;
		this.goal_end_date = end_date;
	}
	
	@Override
	public String toString() {
		return "Goal [id=" + _id + ", a_id=" + a_id + ", name=" + goal_name + ", description=" + goal_description + ", type=" + goal_type + ", start_amount=" + goal_start_amount + ", delta_amount=" + goal_delta_amount + ", end_date=" + goal_end_date +"]";
	}
	
	//Getters --------------------------------------------------------------------
	public int getId(){
		return _id;
	}
	
	public int getAId(){
		return a_id;
	}
	
	public String getName(){
		return goal_name;
	}
	
	public String getDescription(){
		return goal_description;
	}
	
	public String getType(){
		return goal_type;
	}
	
	public String getStartAmount(){
		return goal_start_amount;
	}
	
	public String getDeltaAmount(){
		return goal_delta_amount;
	}
	
	public String getEndDate(){
		return goal_end_date;
	}
	
	//Setters --------------------------------------------------------------------
	public void setId(int id){
		this._id = id;
	}
	
	public void setAId(int a_id){
		this.a_id = a_id;
	}
	
	public void setName(String name){
		this.goal_name = name;
	}
	
	public void setDescription(String description){
		this.goal_description = description;
	}
	
	public void setType(String type){
		this.goal_type = type;
	}
	
	public void setStartAmount(String start_amount){
		this.goal_start_amount = start_amount;
	}
	
	public void setDeltaAmount(String delta_amount){
		this.goal_delta_amount = delta_amount;
	}
	
	public void setEndDate(String end_date){
		this.goal_end_date = end_date;
	}
	
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
        
        try {
        	Class clickedClass = Class.forName("com.example.budgetnotebook.GoalForm");
        	Intent newIntent = new Intent(Goal.this,clickedClass);

			// Brings us back to the root activity, where exit functions properly.
			newIntent.setFlags(newIntent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Pass the extras to the intent on GoalForm.
			newIntent.putExtra("G_ID", g_id);
        	newIntent.putExtra("A_ID", a_id);
        	newIntent.putExtra("G_EDIT", true);
        	startActivity(newIntent);
        	
        } catch(ClassNotFoundException e) {
        	e.printStackTrace();
        }
	}
	
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
        
		// Alert dialog to affirm delete.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                	db.deleteGoal(goal);
                	populateListViewGoals();
                	//S_A_ID = A_ID - lowestID + 1 ;
            		//transAccount.setSelection(S_A_ID-1);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Do Nothing.
                    break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete goal?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
	}
}
