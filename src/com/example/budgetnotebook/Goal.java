package com.example.budgetnotebook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Goal extends Activity{
	
	Button addGoal;
	DBHelper db;
	
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
	
	/*
	// Close the Database on destroy.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		finish();
	};
	*/
	
	// This method uses the Cursor getAllGoals and populates the ListView on the view_goals layout with a list of template_list_goal (layouts)
	@SuppressWarnings("deprecation")
	private void populateListViewGoals() {
	
		// Set a cursor with all the Goals
		Cursor cursor = db.getAllGoals();
		
		//startManagingCursor(cursor);
		
		// Map the GOAL_TABLE fields to the TextViews on the template_list_goal layout.
		String[] goalFieldNames = new String[] {db.G_A_ID, db.GOAL_NAME, db.GOAL_DESCRIPTION, db.GOAL_TYPE, db.GOAL_END_DATE};
		int[] toViewIDs = new int[] {R.id.goalAccount, R.id.goalName, R.id.goalDescription, R.id.goalType, R.id.goalEnd};
	
		// Fills the ListView with all the Goals in the Table.
		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.template_list_goal,
				cursor,
				goalFieldNames,
				toViewIDs
				);
		ListView goalList = (ListView) findViewById(R.id.listViewGoals);
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
}
