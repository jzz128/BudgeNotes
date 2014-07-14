package com.example.budgetnotebook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TransactionForm extends Activity {

	Button saveTransaction;
	ImageButton calendar;
	DBHelper db;
	
	Spinner transAccount;
	String[] seperatedAccount;
	
	Spinner transCategory;
	String[] seperatedCategory;
	
	Spinner transInterval;
	
	RadioGroup transType;
	RadioButton transTypeSelection;
	
	EditText transName;
	EditText transDate;
	EditText transAmount;
	EditText transDescription;
	
	private Calendar cal;
	private int day, month, year;
	
	private int transAccountI;
	private String transCategoryS;
	private String transCategoryVal;
	private String transIntervalS;
	private String transTypeS;
	private String transNameS;
	private String transDateS;
	private String transAmountS;
	private String transDescriptionS;
	
	int A_ID;
	int S_A_ID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_transaction);
		
		// Access the database.
		db = new DBHelper(getBaseContext());

		// Get the id of the account from the spinner on the transaction view.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",0);
        int lowestID;
        lowestID = db.lowestAccountID();
        S_A_ID = A_ID - lowestID + 1 ;
		
		// Initialize date field.
		transDate = (EditText) findViewById(R.id.transEditDate);
		
		// Initialize the Spinners.
		transAccount = (Spinner) findViewById(R.id.transSpinnerAccount);
		transCategory = (Spinner) findViewById(R.id.transSpinnerCategory);
		transInterval = (Spinner) findViewById(R.id.transSpinnerInterval);
		
		// Load the spinner data.
		loadAccountSpinnerData();
		loadCategorySpinnerData();
		loadIntervalSpinnerData();
		
		//Set listener for Account Spinner selection.
		transAccount.setOnItemSelectedListener(new OnItemSelectedListener() {
							
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				///Get the Account spinner data and put it in a string array.
				seperatedAccount = transAccount.getSelectedItem().toString().split(" ");											
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
		});
		
		//Set listener for Category Spinner selection.
		transCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
					
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				///Get the Category spinner data and put it in a string array.
				seperatedCategory = transCategory.getSelectedItem().toString().split(" - ");										
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
		});
		
		//Set listener for Interval Spinner selection.
		transInterval.setOnItemSelectedListener(new OnItemSelectedListener() {
							
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub													
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
		});		
		
		// Initialize the calendar.
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		
		calendar = (ImageButton) findViewById(R.id.transButtonCalendar);
		calendar.setOnClickListener(onDate);
		
		// Set the value of the account spinner using what was passed from the transaction view
		transAccount.setSelection(S_A_ID-1);
		
		// Set the ADD ACCOUNT button to display the ADD Account form when clicked
		saveTransaction = (Button) findViewById(R.id.transButtonSave);
		saveTransaction.setOnClickListener(new View.OnClickListener() {		
								
			@Override
			public void onClick(View v) {
				try{
					
					//Save the values entered in the Transaction form (form_transaction.xml).
					transName = (EditText)findViewById(R.id.transEditName);
					transAmount = (EditText) findViewById(R.id.transEditAmount);
					transDescription = (EditText) findViewById(R.id.transEditDescription);
					transType = (RadioGroup)findViewById(R.id.transEditType);
					
					transTypeSelection = (RadioButton)findViewById(transType.getCheckedRadioButtonId());
					
					// Transfer edit text to GOAL_TABLE attribute types.
					transAccountI = Integer.parseInt(seperatedAccount[0]);
					transNameS = transName.getText().toString().trim();
					transDateS = transDate.getText().toString().trim();
					transAmountS = transAmount.getText().toString().trim();
					transCategoryS = seperatedCategory[1].toString().trim();
					transCategoryVal = seperatedCategory[0].toString().trim();
					transTypeS = transTypeSelection.getText().toString().trim();
					transIntervalS = transInterval.toString().trim();
					transDescriptionS = transDescription.getText().toString().trim();
					
					if (transTypeS.equals("CR")) {
						transTypeS = String.valueOf(R.drawable.credit1);
					} else if (transTypeS.equals("DE")) {
						transTypeS = String.valueOf(R.drawable.debit1);
					} else {
						transTypeS = String.valueOf(R.drawable.other1);
					}
					
					// Call the add transaction method to add the transaction to the database!
					addTransaction();
					updateAccount();
					
					Class clickedClass = Class.forName("com.example.budgetnotebook.Transaction");
					Intent newIntent = new Intent(TransactionForm.this, clickedClass);

					// Brings us back to the root activity, where exit functions properly.
					newIntent.setFlags(newIntent.FLAG_ACTIVITY_CLEAR_TOP);
					newIntent.putExtra("A_ID", transAccountI);
					startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}				
		});
	}
	
	private void loadAccountSpinnerData() {
		List<String> list = db.getAllStringAccounts();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                     (this, android.R.layout.simple_spinner_item,list);
                      
        dataAdapter.setDropDownViewResource
                     (android.R.layout.simple_spinner_dropdown_item);
		 transAccount.setAdapter(dataAdapter);
	}
	
	private void loadCategorySpinnerData() {
		ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.transCatArray, android.R.layout.simple_spinner_item);       
		transCategory.setAdapter(dataAdapter);
	}
	
	private void loadIntervalSpinnerData() {
		ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.transIntArray, android.R.layout.simple_spinner_item);       
		transInterval.setAdapter(dataAdapter);
	}
	
	private View.OnClickListener onDate = new View.OnClickListener() {
		public void onClick(View v) {
			showDialog(0);
		}
	};
	
	// Close the Database on destroy.
		@Override
		protected void onDestroy() {
			super.onDestroy();
			db.close();
			finish();
		};
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
					transDate.setText((selectedMonth + 1) + " / " + selectedDay + " / " + selectedYear);
}
};
	
	private void addTransaction() {
		db.addTransaction(new Transaction(transAccountI, transNameS, transDateS, transAmountS, transCategoryS, transTypeS, transIntervalS, transDescriptionS));
	}

	private void updateAccount() {
		int accountId = transAccountI;
		Account account;
		int oldBalance;
		int changeAmount;
		int newBalance;
		String accountType;
		
		changeAmount = Integer.parseInt(transAmountS);
		
		//Toast.makeText(this, "changeAmount="+changeAmount, Toast.LENGTH_LONG).show();
		//Toast.makeText(this, "transAmountS="+transAmountS, Toast.LENGTH_LONG).show();
		
		switch (transType.getCheckedRadioButtonId()) {
		case R.id.transTypeCredit:
			// Type is a Credit to the account
			changeAmount = changeAmount;
			//Toast.makeText(this, "changeAmount="+changeAmount, Toast.LENGTH_LONG).show();
			break;
			
		case R.id.transTypeDebit:
			// Type is a Credit to the account
			changeAmount = -changeAmount;
			//Toast.makeText(this, "changeAmount="+changeAmount, Toast.LENGTH_LONG).show();
			break;
		}
		
		// Update the value of the Account.
		account = db.getAccount(accountId);
				
		// Reverses amount if this is a credit card.
		accountType = account.getType();
		
		if (new String("CR").equals(accountType)) {
			changeAmount = -changeAmount;
		}
		
		oldBalance = Integer.parseInt(account.getBalance());
		newBalance = oldBalance + changeAmount;
		account.setBalance(String.valueOf(newBalance));
		db.updateAccount(account);
	}

}
