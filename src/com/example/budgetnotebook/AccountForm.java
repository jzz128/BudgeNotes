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
	
	int A_ID;
	boolean A_EDIT;
	Account account;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_account);
		
		// Access the database.
		db = new DBHelper(getBaseContext());	
		
		// Get the extras from previous activity.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",0);
		A_EDIT = intent.getBooleanExtra("A_EDIT", false);
		
		//Save the values entered in the Account form (form_account.xml).
		accountName = (EditText)findViewById(R.id.accountEditName);
		accountNumber = (EditText)findViewById(R.id.accountEditNumber);
		accountType = (RadioGroup)findViewById(R.id.accountEditType);
		accountBalance = (EditText)findViewById(R.id.accountEditBalance);
		
		// Set checking to be selected by default
		accountTypeSelection = (RadioButton)findViewById(R.id.accountTypeChecking);
		accountTypeSelection.setChecked(true);
		
		// Auto fill the form if this is an edit.
		if (A_EDIT) {
			account = db.getAccount(A_ID);
			populateForm();
		} else {
			//Do Nothing.
		}
		
		// Set the ADD ACCOUNT button to display the ADD Account form when clicked
		saveAccount = (Button) findViewById(R.id.accountButtonSave);
		saveAccount.setOnClickListener(new View.OnClickListener() {		
			
			@Override
			public void onClick(View v) {
				try{
					
					accountTypeSelection = (RadioButton)findViewById(accountType.getCheckedRadioButtonId());
										
					// Transfer edit text to ACCOUNT_TABLE attribute types.
					accountNameS = accountName.getText().toString().trim();
					accountNumberS = accountNumber.getText().toString().trim();
					accountTypeS = accountTypeSelection.getText().toString().trim();
					accountBalanceS = accountBalance.getText().toString().trim();
					
					if(inputsValid()){
						
						if (A_EDIT) {
							fillAccountObject();
							db.updateAccount(account);
						} else {
							// Call the add account method to add the account to the database!
							addAccount();
						}
							Class<?> clickedClass = Class.forName("com.example.budgetnotebook.Account");
							Intent newIntent = new Intent(AccountForm.this, clickedClass);

							// Brings us back to the root activity, where exit functions properly.
							newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

	private void fillAccountObject() {
		account.setName(accountNameS);
		account.setNumber(accountNumberS);
		account.setType(accountTypeS);
		account.setBalance(accountBalanceS);
	}
	
	private void populateForm() {
		accountName.setText(account.getName());
		accountNumber.setText(account.getNumber());
		
		if (account.getType().equals("CHK")) {
			accountTypeSelection = (RadioButton)findViewById(R.id.accountTypeChecking);
			accountTypeSelection.setChecked(true);
		} else if (account.getType().equals("SAV")) {
			accountTypeSelection = (RadioButton)findViewById(R.id.accountTypeSavings);
			accountTypeSelection.setChecked(true);
		} else if (account.getType().equals("CR")) {
			accountTypeSelection = (RadioButton)findViewById(R.id.accountTypeCredit);
			accountTypeSelection.setChecked(true);
		} else {
			accountTypeSelection = (RadioButton)findViewById(R.id.accountTypeChecking);
			accountTypeSelection.setChecked(true);
		}
		
		accountBalance.setText(account.getBalance());
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
	
	// Close the Database on destroy.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		finish();
	};

}
