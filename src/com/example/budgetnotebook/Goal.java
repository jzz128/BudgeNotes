package com.example.budgetnotebook;

import android.app.Activity;
import android.os.Bundle;

public class Goal extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_goals);
		
		// DATABASE OPEN AND TEST USING TOAST ---------------------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
		
		//Create Database instance
		DBHelper db = new DBHelper(getBaseContext());
				
		// Testing Goal with Toast
		db.toastGoal(getBaseContext());
				
		// Close db
		db.close();
		
		// --------------------------------------------------------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
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
