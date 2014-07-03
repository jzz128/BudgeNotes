package com.example.budgetnotebook;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Added the adapter scaffolding and getters / setters.
 * 
 */

public class Account  {
		
	private EditText accountName;
	private EditText accountNumber;
	private RadioGroup accountTypes;
	private EditText beginningBalance;

	private int _id;
	private String account_name;
	private String account_number;
	private String account_type;
	private String balance;
	
	public Account(){}
	
	public Account(String account_name, String account_number, String account_type, String balance) {
		super();
		this.account_name = account_name;
		this.account_number = account_number;
		this.account_type = account_type;
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + _id + ", account_name=" + account_name + ", account_number=" + account_number + ", account_type=" + account_type + ", balance=" + balance +"]";
	}
	
	//Getters --------------------------------------------------------------------
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

