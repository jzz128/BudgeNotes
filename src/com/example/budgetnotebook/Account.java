/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * Account.java
 * 
 * DBHelper adapter for modifying DB data associated with the Account Table.
 **/
	
package com.example.budgetnotebook;

public class Account {
	// Class variable definitions.
	private int _id;
	private String account_name;
	private String account_number;
	private String account_type;
	private String balance;
	
	// Constructors --------------------------------------------------------------	
	public Account(){}
	
	public Account(String account_name, String account_number, String account_type, String balance) {
		super();
		this.account_name = account_name;
		this.account_number = account_number;
		this.account_type = account_type;
		this.balance = balance;
	}
	
	// Log reporting string -------------------------------------------------------
	@Override
	public String toString() {
		return "Account [id=" + _id + ", account_name=" + account_name + ", account_number=" + account_number + ", account_type=" + account_type + ", balance=" + balance +"]";
	}
	
	// Getters --------------------------------------------------------------------
	public int getId(){
		return _id;
	}
	
	public String getName() {
		return account_name;
	}
	
	public String getNumber() {
		return account_number;
	}
	
	public String getType() {
		return account_type;
	}
	
	public String getBalance() {
		return balance;
	}
	
	//Setters --------------------------------------------------------------------
	public void setId(int id){
		this._id = id;
	}
	
	public void setName(String account_name){
		this.account_name = account_name;
	}
	
	public void setNumber(String account_number) {
		this.account_number = account_number;
	}
	
	public void setType(String account_type) {
		this.account_type = account_type;
	}
	
	public void setBalance(String balance) {
		this.balance = balance;
	}
}