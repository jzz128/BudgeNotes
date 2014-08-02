/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * Goal.java
 * 
 * DBHelper adapter for modifying DB data associated with the Goal Table.
 **/

package com.example.budgetnotebook;

public class Goal {
	//Class variable definitions.
	private int _id;
	private int g_id;
	private String goal_name;
	private String goal_description;
	private String goal_type;
	private String goal_start_amount;
	private String goal_delta_amount;
	private String goal_end_date;
	private String goal_status;

	//Initializers --------------------------------------------------------------
	public Goal(){}
	
	public Goal(int g_id, String name, String description, String type, String start_amount, String delta_amount, String end_date, String goal_status) {
		super();
		this.g_id = g_id;
		this.goal_name = name;
		this.goal_description = description;
		this.goal_type = type;
		this.goal_start_amount = start_amount;
		this.goal_delta_amount = delta_amount;
		this.goal_end_date = end_date;
		this.goal_status = goal_status;
	}
	
	// Return Goal object as a string
	@Override
	public String toString() {
		return "Goal [id=" + _id + ", g_id=" + g_id + ", name=" + goal_name + ", description=" + goal_description + ", type=" + goal_type + ", start_amount=" + goal_start_amount + ", delta_amount=" + goal_delta_amount + ", end_date=" + goal_end_date +"goal_status=" + goal_status + "]";
	}
	
	//Getters --------------------------------------------------------------------
	public int getId(){
		return _id;
	}
	
	public int getAId(){
		return g_id;
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
	
	public String getStatus() {
		return goal_status;
	}
	
	//Setters --------------------------------------------------------------------
	public void setId(int id){
		this._id = id;
	}
	
	public void setAId(int g_id){
		this.g_id = g_id;
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
	
	public void setStatus(String goal_status) {
		this.goal_status = goal_status;
	}
	
}
