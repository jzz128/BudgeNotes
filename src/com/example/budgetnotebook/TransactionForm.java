package com.example.budgetnotebook;

import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class TransactionForm extends Activity implements InputValidator {

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
	TextView transDate;
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
	private boolean transAccounted;
	private boolean prevAccounted;
	
	int A_ID;
	int T_ID;
	int S_A_ID;
	boolean T_EDIT;
	Transaction transaction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_transaction);
		
		// Access the database.
		db = new DBHelper(getBaseContext());
		
		// Get the id of the account from the spinner on the transaction view.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",0);
		T_ID = intent.getIntExtra("T_ID", 0);
		T_EDIT = intent.getBooleanExtra("T_EDIT", false);
		
        int lowestID;
        lowestID = db.lowestAccountID();
        S_A_ID = A_ID - lowestID + 1 ;
		
		// Initialize date field.
		transDate = (TextView) findViewById(R.id.transEditDate);
		
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
		
		//Save the values entered in the Transaction form (form_transaction.xml).
		transName = (EditText)findViewById(R.id.transEditName);
		transAmount = (EditText) findViewById(R.id.transEditAmount);
		transDescription = (EditText) findViewById(R.id.transEditDescription);
		transType = (RadioGroup)findViewById(R.id.transEditType);
		
		// Auto fill the form if this is an edit.
		if (T_EDIT) {
			transaction = db.getTransaction(T_ID); // Corrected this. Was A_ID - DJM
			prevAccounted = transaction.getAccounted();
			//Toast.makeText(this, String.valueOf(prevAccounted), Toast.LENGTH_LONG).show();
			populateForm();
		} else {
			// Set the type selection button to default to CR
			transTypeSelection = (RadioButton)findViewById(R.id.transTypeCredit);
			transTypeSelection.setChecked(true);
			// Set date to todays date
			cal= Calendar.getInstance();
            String cal_for_month = Integer.toString(cal.get(Calendar.MONTH)+1);
            String cal_for_year = Integer.toString(cal.get(Calendar.YEAR));
            String cal_for_day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
			String todayAsString = cal_for_month + "/" + cal_for_day + "/" + cal_for_year;
			transDate.setText(todayAsString);
		}
		
		// Set the ADD ACCOUNT button to display the ADD Account form when clicked
		saveTransaction = (Button) findViewById(R.id.transButtonSave);
		saveTransaction.setOnClickListener(new View.OnClickListener() {		
								
			@Override
			public void onClick(View v) {
				try{
					
					transTypeSelection = (RadioButton)findViewById(transType.getCheckedRadioButtonId());
					
					// Transfer edit text to GOAL_TABLE attribute types.
					transAccountI = Integer.parseInt(seperatedAccount[0]);
					transNameS = transName.getText().toString().trim();
					transDateS = transDate.getText().toString().trim();
					transAmountS = transAmount.getText().toString().trim();
					transCategoryS = String.valueOf(transCategory.getSelectedItemPosition()) + " - " + seperatedCategory[1].toString().trim();
					transCategoryVal = seperatedCategory[0].toString().trim();
					transTypeS = transTypeSelection.getText().toString().trim();
					transIntervalS = String.valueOf(transInterval.getSelectedItemPosition()) + " - " + transInterval.getSelectedItem().toString();
					transDescriptionS = transDescription.getText().toString().trim();
					
					transAccounted = db.checkAccountedDate(transDateS, "now"); // To incorporate the new transaction format.
					
					if (transTypeS.equals("CR")) {
						transTypeS = String.valueOf(R.drawable.credit1);
					} else if (transTypeS.equals("DE")) {
						transTypeS = String.valueOf(R.drawable.debit1);
						if (Integer.parseInt(transAmountS) > 0 ) transAmountS = String.valueOf(-Integer.parseInt(transAmountS)); //Sets the debits to negative numbers.
					} else {
						transTypeS = String.valueOf(R.drawable.other1);
					}
					
					// Validate inputs
					if(inputsValid()){
						if (T_EDIT) {
							// Update the account balance and then update the transaction record.
							fillTransObject();
							if (prevAccounted) reverseTransaction();
							db.updateTransaction(transaction);
							if (transAccounted) updateAccount();
						} else {
							// Call the add transaction method to add the transaction to the database and update the account balance!
							addTransaction();
							if (transAccounted) updateAccount();
						}
						
						Class<?> clickedClass = Class.forName("com.example.budgetnotebook.Transaction");
						Intent newIntent = new Intent(TransactionForm.this, clickedClass);
	
						// Brings us back to the root activity, where exit functions properly.
						newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						newIntent.putExtra("A_ID", transAccountI);
						startActivity(newIntent);
                        // Finish Activity
                        finish();
					}
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}				
		});
	}
	
	// Fill the transaction variable with updated information.
	private void fillTransObject() {
		transaction.setAId(transAccountI);
		transaction.setName(transNameS);
		transaction.setDate(transDateS);
		transaction.setAmount(transAmountS);
		transaction.setCategory(transCategoryS);
		transaction.setType(transTypeS);
		transaction.setInterval(transIntervalS);
		transaction.setDescription(transDescriptionS);
		transaction.setAccounted(transAccounted);
	}
	
	// Fill the form fields with database data.
	private void populateForm() {
		
		String[] tranCat;
		String[] tranInt;
		
		tranCat = transaction.getCategory().split(" - ");
		tranInt = transaction.getInterval().split(" - ");
		
		transName.setText(transaction.getName());
		transDate.setText(transaction.getDate());
		transAmount.setText(transaction.getAmount());
		transDescription.setText(transaction.getDescription());
		
		if (transaction.getType().equals(String.valueOf(R.drawable.credit1))) {
			transTypeSelection = (RadioButton)findViewById(R.id.transTypeCredit);
			transTypeSelection.setChecked(true);
		} else if (transaction.getType().equals(String.valueOf(R.drawable.debit1))) {
			transTypeSelection = (RadioButton)findViewById(R.id.transTypeDebit);
			transTypeSelection.setChecked(true);
		}else {
			//Do nothing for now.
		}
		
		transCategory.setSelection(Integer.parseInt(tranCat[0]));
		transInterval.setSelection(Integer.parseInt(tranInt[0]));
		
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
		ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.transCatArray, android.R.layout.simple_spinner_item);       
		transCategory.setAdapter(dataAdapter);
	}
	
	private void loadIntervalSpinnerData() {
		ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.transIntArray, android.R.layout.simple_spinner_item);       
		transInterval.setAdapter(dataAdapter);
	}
		
	// Close the Database on destroy.
		@Override
		protected void onDestroy() {
			super.onDestroy();
			db.close();
			finish();
		};

	private View.OnClickListener onDate = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {
			showDialog(0);
		}
	};
		
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
					transDate.setText((selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear);
		}
	};
	
	private void addTransaction() {
		db.addTransaction(new Transaction(transAccountI, transNameS, transDateS, transAmountS, transCategoryS, transTypeS, transIntervalS, transDescriptionS, transAccounted));
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
		
		/*
		switch (transType.getCheckedRadioButtonId()) {
		case R.id.transTypeCredit:
			// Type is a Credit to the account
			//changeAmount = changeAmount;
			//Toast.makeText(this, "changeAmount="+changeAmount, Toast.LENGTH_LONG).show();
			break;
			
		case R.id.transTypeDebit:
			// Type is a Credit to the account
			changeAmount = -changeAmount;
			//Toast.makeText(this, "changeAmount="+changeAmount, Toast.LENGTH_LONG).show();
			break;
		}
		*/
		
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

	//Update the account if this is an edit. Essentially reversing the original transaction
	// Updated to incorporate new transaction method.
	private void reverseTransaction() {
		Account account1 = db.getAccount(A_ID);
		Transaction transaction = db.getTransaction(T_ID);
		
		int oldBalance;
		int changeAmount;
		int newBalance;
		String accountType;
		changeAmount = Integer.parseInt(transaction.getAmount());
		
		/*
		if (transaction.getType().equals(String.valueOf(R.drawable.credit1))) {
			changeAmount = - changeAmount;
		} else if (transaction.getType().equals(String.valueOf(R.drawable.debit1))) {
			//changeAmount = changeAmount;
		}else {
			//Do nothing for now.
		}
		*/
		
		// Reverses amount if this is a credit card.
		accountType = account1.getType();
		
		if (new String("CR").equals(accountType)) {
			changeAmount = -changeAmount;
		}
		
		oldBalance = Integer.parseInt(account1.getBalance());
		newBalance = oldBalance - changeAmount;
		account1.setBalance(String.valueOf(newBalance));
		db.updateAccount(account1);
		
	}
	
	public boolean inputsValid(){
		boolean valid = true;

		// Transaction name not empty
		if(transNameS.length() == 0){
			transName.setError("Input is required.");
			valid = false;
		}
		
		// Transaction date not empty
		if(transDateS.length() == 0){
			transDate.setError("Input is required.");
			valid = false;
		}
		
		// Transaction Amount not empty		
		if(transAmountS.length() == 0){
			transAmount.setError("Input is required.");
			valid = false;
		}
		
		// Description not empty
		if(transDescriptionS.length() == 0){
			transDescription.setError("Input is required");
			valid = false;
		}
		
		return valid;
	}
	
}
