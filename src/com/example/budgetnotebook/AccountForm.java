package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AccountForm extends Activity implements InputValidator{
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
					
					// Transfer edit text to ACCOUNT_TABLE attribute types.
					accountNameS = accountName.getText().toString().trim();
					accountNumberS = accountNumber.getText().toString().trim();
					accountTypeS = accountTypeSelection.getText().toString().trim();
					accountBalanceS = accountBalance.getText().toString().trim();
					
					if(inputsValid()){
						// Call the add account method to add the account to the database!
						addAccount();
					
						Class clickedClass = Class.forName("com.example.budgetnotebook.Account");
						Intent newIntent = new Intent(AccountForm.this, clickedClass);
						startActivity(newIntent);
					}
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}				
		});
		
	};
	
	private void addAccount() {
		db.addAccount(new Account(accountNameS, accountNumberS, accountTypeS, accountBalanceS));
	}

	@Override
	public boolean inputsValid() {
		boolean valid = true;
		
		// Account name is not empty
		if(accountNameS.length() == 0){
			accountName.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Account number is not empty
		if(accountNumberS.length() == 0){
			accountNumber.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Account type has been selected
		if(accountTypeS.length() == 0){
			accountTypeSelection.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Beginning balance is not empty
		if(accountBalanceS.length() == 0){
			accountBalance.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		return valid;
	};	

}
