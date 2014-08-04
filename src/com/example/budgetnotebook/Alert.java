/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * Alert.java
 * 
 *DBHelper Adapter for modifying data associated with the alert table.
 * 
 **/

package com.example.budgetnotebook;

public class Alert {
	private int _id;
	private int at_id;
	private String alert_name;
	private String alert_description;
	private String alert_due_date;
	
	public Alert(){}
	
	public Alert(int a_id, String name, String description, String due_date) {
		super();
		this.at_id = a_id;
		this.alert_name = name;
		this.alert_description = description;
		this.alert_due_date = due_date;
	}
	
	@Override
	public String toString() {
		return "Alert [id=" + _id + ", at_id=" + at_id + ", name=" + alert_name + ", description=" + alert_description + ", due_date=" + alert_due_date +"]";
	}
	
	//Getters --------------------------------------------------------------------
	public int getId(){
		return _id;
	}
	
	public int getAtId(){
		return at_id;
	}
	
	public String getName(){
		return alert_name;
	}
	
	public String getDescription(){
		return alert_description;
	}
	
	
	public String getDueDate(){
		return alert_due_date;
	}
	
	//Setters --------------------------------------------------------------------
	public void setId(int id){
		this._id = id;
	}
	
	public void setAId(int at_id){
		this.at_id = at_id;
	}
	
	public void setName(String name){
		this.alert_name = name;
	}
	
	public void setDescription(String description){
		this.alert_description = description;
	}
	
	public void setDueDate(String due_date){
		this.alert_due_date = due_date;
	}
}