package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AccountForm extends Activity {
	Button saveAccount = null;
	DBHelper db;
	
	EditText accountName;
	EditText accountNumber;
	RadioGroup accountType;
	RadioButton accountTypeSelection;
	EditText accountBalance;
		
	String accountNameS;
	String accountNumberS;
	String accountTypeS;
	String accountBalanceS;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_account);
		
		// Access the database.
		db = new DBHelper(getBaseContext());
		
		// Set the ADD ACCOUNT button to display the ADD Account form when clicked
		saveAccount = (Button) findViewById(R.id.accountButtonSave);
		saveAccount.setOnClickListener(new View.OnClickListener() {		
								
			@Override
			public void onClick(View v) {
				try{
					
					//Save the values entered in the Account form (form_account.xml).
					accountName = (EditText)findViewById(R.id.accountEditName);
					accountNumber = (EditText)findViewById(R.id.accountEditNumber);
					accountType = (RadioGroup)findViewById(R.id.accountEditType);
					accountTypeSelection = (RadioButton)findViewById(accountType.getCheckedRadioButtonId());
					accountBalance = (EditText)findViewById(R.id.accountEditBalance);
					
					// Transfer edit text to GOAL_TABLE attribute types.
					accountNameS = accountName.getText().toString().trim();
					accountNumberS = accountNumber.getText().toString().trim();
					accountTypeS = accountTypeSelection.getText().toString().trim();
					accountBalanceS = accountBalance.getText().toString().trim();
					
					// Call the add goal method to add the goal to the database!
					addAccount();
					
					Class clickedClass = Class.forName("com.example.budgetnotebook.Account");
					Intent newIntent = new Intent(AccountForm.this, clickedClass);
					startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}				
		});
		
	};
	
	private void addAccount() {
		db.addAccount(new Account(accountNameS, accountNumberS, accountTypeS, accountBalanceS));
	};	

}
