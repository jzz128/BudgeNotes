/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * TransactionView.java
 * 
 * Displays transactions to the user for a user set account in a user set date range.
 * 
 **/

package com.example.budgetnotebook;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class TransactionView extends Activity {

	int A_ID;
	int AFTER_EDIT;
	int S_A_ID;
	int lowestID;
	
	Account account;
	Transaction transaction;
	Transaction trans2;
	TextView tv;
	
	Spinner transAccount;
	String[] seperated;
	Button addTransaction;
	DBHelper db;
	
	RelativeLayout vwParentRow;
	ListView transactionList;
	
	TextView transDate, transDateEnd;
	private Calendar cal;
	private int day,last_month,next_month, year;
	private String spinDate, spinDateEnd;
	
	String[] interval;
	int intVal;
	Intent newIntent;
	
	String[] nSplit;
	int baseID;
	
	boolean start;
	
	DatePicker startDate, endDate;
	
	//Perform operations when class created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_transaction);
				
		//Create Database instance
		db = new DBHelper(getBaseContext());
		
		// Initialize the spinner.
		transAccount = (Spinner) findViewById(R.id.transAccountSpinner);
		
		// Initialize date field.
		transDate = (TextView) findViewById(R.id.tranViewSpinner);
		transDateEnd = (TextView) findViewById(R.id.tranViewSpinnerEnd);
		
		// Initialize the calendar.
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		last_month = cal.get(Calendar.MONTH)-1;
		next_month = cal.get(Calendar.MONTH)+1;
		year = cal.get(Calendar.YEAR);
		
		// Set date to todays date
		String cal_for_month_today, cal_for_last_month, cal_for_next_month;
		String cal_for_day;
		
		// Set month values to string
		cal_for_month_today = Integer.toString(cal.get(Calendar.MONTH)+1);
		//if (cal.get(Calendar.MONTH)+1 < 10) cal_for_month_today = "0" + Integer.toString(cal.get(Calendar.MONTH)+1);
		
		cal_for_last_month = Integer.toString(cal.get(Calendar.MONTH));
		//if (cal.get(Calendar.MONTH) < 10) cal_for_last_month = "0" + Integer.toString(cal.get(Calendar.MONTH));
		
		cal_for_next_month = Integer.toString(cal.get(Calendar.MONTH)+2);
		//if (cal.get(Calendar.MONTH)+2 < 10) cal_for_next_month = "0" + Integer.toString(cal.get(Calendar.MONTH)+2);
		
		// Set day value to string
		cal_for_day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		//if (cal.get(Calendar.DAY_OF_MONTH) < 10) cal_for_day = "0" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

		// Set year value to string
		String cal_for_year = Integer.toString(cal.get(Calendar.YEAR));

		// Combine strings to get today, one month ago and one month in the future strings
		String todayAsString = cal_for_month_today + "/" + cal_for_day + "/" + cal_for_year;
		Log.d("Today=",todayAsString);
		String oneMonthAgo = cal_for_last_month + "/" + cal_for_day + "/" + cal_for_year;
		Log.d("One Month Ago=",oneMonthAgo);
		String oneMonthInTheFuture = cal_for_next_month + "/" + cal_for_day + "/" + cal_for_year;
		Log.d("One Month Ahead=",oneMonthInTheFuture);
		
		// Get the extras from previous activity.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",0);
		S_A_ID = db.correctSpinID(A_ID);
		AFTER_EDIT = intent.getIntExtra("AFTER_EDIT",0);
		
		// Set default to be one month before and after current date
		spinDate = oneMonthAgo;
		transDate.setText(spinDate);
		
		spinDateEnd = oneMonthInTheFuture;
		transDateEnd.setText(spinDateEnd);

		// Add data to the spinner.
		loadAccountSpinnerData();
        
		transAccount.setSelection(S_A_ID, false);
		// Set a listener for the Account spinner selection.
		transAccount.setOnItemSelectedListener(new OnItemSelectedListener() {
			
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			//Get the Account spinner data and put it in a string array.
			seperated = transAccount.getSelectedItem().toString().split(" ");
									
			// Populate the list view with transactions from specified account.
			setViewAccount(Integer.parseInt(seperated[0]));
			populateListViewTransactions(A_ID);
						
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				//  Do Nothing			
			}
		});
							
		// Set the ADD TRANSACTION button to display the ADD Transaction form when clicked
		addTransaction = (Button) findViewById(R.id.addTransaction);
		addTransaction.setOnClickListener(new View.OnClickListener() {		
								
		@Override
			public void onClick(View v) {
				try{
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.TransactionForm");
					Intent newIntent = new Intent(TransactionView.this, clickedClass);
					
					// Brings us back to the root activity, where exit functions properly.
					newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					newIntent.putExtra("A_ID", A_ID);
					//newIntent.putExtra("S_A_ID", S_A_ID);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});
		
	// Populate the ListView
	db.seeFuture(getApplicationContext(),spinDateEnd,A_ID);	
	populateListViewTransactions(A_ID);

	}
	
	//Perform operations when the icon button is clicked.
	public void iconClickHandler(View v) {
		//Do Nothing.
	}
	
	// Show the date picker when the start date picker icon is clicked.
	@SuppressWarnings("deprecation")
	public void dateClickHandler(View v) {
		start = true;
		showDialog(0);
	}
	
	// Show the date picker when the end date picker icon is clicked.
	@SuppressWarnings("deprecation")
	public void dateClickHandlerEnd(View v) {
		start = false;
		showDialog(1);
	}
	
	//Make the date picker dialog.
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		if (start == true) {
			return new DatePickerDialog(this, datePickerListener, year, last_month, day);
		} else	{
			return new DatePickerDialog(this, datePickerListener, year, next_month, day);
		}
	}

	//Listener for date picker set button.
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			if (start == true) {		
				spinDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
				transDate.setText(spinDate);
			} else	{
				spinDateEnd = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
				transDateEnd.setText(spinDateEnd);
			}
			db.cleanTransactions(getBaseContext(), "now");
			db.seeFuture(getBaseContext(),spinDateEnd,A_ID);
			loadAccountSpinnerData();
			transAccount.setSelection(S_A_ID);
			populateListViewTransactions(A_ID);
		
		}
	};
	
	//Perform operations when the edit button is clicked.
	public void editClickHandler(View v) {
		int t_id;
		int a_id;
		
		// Get the row the clicked button is in
        vwParentRow = (RelativeLayout)v.getParent();
        
        // Get the object that the transaction and account ID are stored in
        TextView child = (TextView)vwParentRow.getChildAt(1);
        TextView child2 = (TextView)vwParentRow.getChildAt(2);
        
        // Store the account and transaction id in the variable integers.
        t_id = Integer.parseInt((child.getText().toString().trim()));
        a_id = Integer.parseInt((child2.getText().toString().trim()));
        
        transaction = db.getTransaction(t_id);
        
        interval = transaction.getInterval().split(" - ");
        intVal = Integer.parseInt(interval[0]);
        
        try {
           	Class<?> clickedClass = Class.forName("com.example.budgetnotebook.TransactionForm");
        	newIntent = new Intent(TransactionView.this,clickedClass);

			// Brings us back to the root activity, where exit functions properly.
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Pass the extras to the intent on AccountForm.
        	newIntent.putExtra("A_ID", a_id);
        	newIntent.putExtra("T_ID", t_id);
        	//newIntent.putExtra("S_A_ID", S_A_ID);
        	newIntent.putExtra("T_EDIT", true);
        	
        	// 
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                    case 0:      	
                    	newIntent.putExtra("E_SCOPE", 0);
                    	startActivity(newIntent);
                        break;

                    case 1:
                    	newIntent.putExtra("E_SCOPE", 1);
                    	startActivity(newIntent);
                        break;
                    case 2:
                    	newIntent.putExtra("E_SCOPE", 2);
                    	startActivity(newIntent);
                        break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (intVal != 0) {
            	builder.setTitle(R.string.pick_scope).setItems(R.array.scope_array, dialogClickListener).show();
            } else {
            	newIntent.putExtra("E_SCOPE", 0);
            	startActivity(newIntent);
            }
		
        } catch(ClassNotFoundException e) {
        	e.printStackTrace();
        }
	}
	
	//Perform operations when the delete button is clicked.
	public void deleteClickHandler(View v) {
		int t_id;
		int a_id;
		
		// Get the row the clicked button is in
        vwParentRow = (RelativeLayout)v.getParent();
        
        // Get the object that the transaction and account ID are stored in
        TextView child = (TextView)vwParentRow.getChildAt(1);
        TextView child2 = (TextView)vwParentRow.getChildAt(2);
        
        // Store the account and transaction id in the variable integers.
        t_id = Integer.parseInt((child.getText().toString().trim()));
        a_id = Integer.parseInt((child2.getText().toString().trim()));
		
        // Initialize the objects.
        account = db.getAccount(a_id);
        transaction = db.getTransaction(t_id);
        
        interval = transaction.getInterval().split(" - ");
        intVal = Integer.parseInt(interval[0]);
        
		// Alert dialog to affirm delete.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:      	
                	if (intVal == 0) {
                		reverseTransaction();
                		db.deleteTransaction(transaction);
                	} else {
                		checkAgain();
                	}
                	loadAccountSpinnerData();
                	S_A_ID = db.correctSpinID(A_ID);
            		transAccount.setSelection(S_A_ID);
            		AFTER_EDIT = 1;
            		onResume();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Do Nothing.
                    break;
                }
               
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete transaction?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();      
	}
	
	//Ask if the user wants to delete associated transactions or just the selected one.
	private void checkAgain() {
		if(transaction.getName().toString().contains("-")) {
			nSplit = new String[2];
			nSplit = transaction.getName().toString().split("-");
			baseID = Integer.parseInt(nSplit[1].toString());
		}
		DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if(transaction.getName().toString().contains("-")) {
						//nSplit = new String[2];
						//nSplit = transaction.getName().toString().split("-");
						//baseID = Integer.parseInt(nSplit[1].toString());
						//Log.d(nSplit[1].toString(), nSplit[1].toString());
						transaction = db.getTransaction(baseID);
					} else {
						// Do Nothing.
					}
					db.deleteRecurringTransactions(transaction);
					db.recalcAlert(transaction);
            		db.cleanTransactions(getBaseContext(), spinDateEnd);
            		db.checkTransactionStatus();
            		break;
				case DialogInterface.BUTTON_NEGATIVE:
					if(!transaction.getName().toString().contains("-")) {Toast.makeText(getBaseContext(), "Can not delete a base transaction!" , Toast.LENGTH_LONG).show(); break;}
					reverseTransaction();
            		db.deleteTransaction(transaction);
					db.recalcAlert(db.getTransaction(baseID));
            		db.checkTransactionStatus();
                    break;
				}
				loadAccountSpinnerData();
            	S_A_ID = db.correctSpinID(A_ID);
        		transAccount.setSelection(S_A_ID);
			}			
		};
		
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setMessage("Delete all recurring transactions associated as well?").setPositiveButton("Yes", dialogClickListener2)
            .setNegativeButton("No", dialogClickListener2).show();
	}
	
	//Update the account if this is an edit. Essentially reversing the original transaction
	private void reverseTransaction() {		
		float oldBalance;
		float changeAmount;
		float newBalance;
		String accountType;
			
		changeAmount = Float.parseFloat(transaction.getAmount());
			
		// Reverses amount if this is a credit card.
		accountType = account.getType();
			
		if (new String("CR").equals(accountType)) {
			changeAmount = -changeAmount;
		}
			
		oldBalance = Float.parseFloat(account.getBalance());
		newBalance = oldBalance - changeAmount;
		account.setBalance(String.format("%.2f",newBalance));
		if(transaction.getAccounted()) db.updateAccount(account);
		
	}
	
	// Populate the spinner with account numbers	
	private void loadAccountSpinnerData() {
		
		Log.d("POPULATE TRANSACTIONS RUN!","-");
		Log.d("A_ID = ", String.valueOf(A_ID));
		Log.d("SPIN_DATE = ", String.valueOf(spinDate));
		Log.d("SPIN_DATE_END = ", String.valueOf(spinDateEnd));	
		
		List<String> list = db.getAllStringAccounts();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                     (this, android.R.layout.simple_spinner_item,list);
                      
        dataAdapter.setDropDownViewResource
                     (android.R.layout.simple_spinner_dropdown_item);
		 transAccount.setAdapter(dataAdapter);		 
	}
	
	// Fill the ListView with transactions.
	@SuppressWarnings("deprecation")
	private void populateListViewTransactions(int A_ID) {
		// Set a cursor with all the Transactions
		Cursor cursor = db.getAllTransactionsInRange(A_ID, spinDate, spinDateEnd);
				
		// Map the TRANSACTION_TABLE fields to the TextViews on the template_list_transaction layout.
		String[] transactionFieldNames = new String[] {DBHelper.T_ID, DBHelper.T_A_ID, DBHelper.TRANSACTION_NAME, DBHelper.TRANSACTION_DATE, DBHelper.TRANSACTION_AMOUNT, DBHelper.TRANSACTION_TYPE, DBHelper.TRANSACTION_CHANGE};
		int[] toViewIDs = new int[] {R.id.transactionID, R.id.transactionAccountID, R.id.transName, R.id.transDate, R.id.transAmount, R.id.transcactionButtonIcon, R.id.transChangeAmount};
			
		// Fills the ListView with all the Transactions in the Table.
		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.template_list_transaction,
				cursor,
				transactionFieldNames,
				toViewIDs
				);
		
		SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnUndex) {
				if (view.getId() == R.id.transChangeAmount)
                { 
                  String s = cursor.getString(11);
                  TextView tv = (TextView)view;

                  if(s != null) tv.setTextColor(Color.parseColor(s));
                  tv.setText(cursor.getString(10));
                 return true;

            }
              return false;
            }
			
		};
		myCursorAdapter.setViewBinder(binder);
		
		ListView transactionList = (ListView) findViewById(R.id.listViewTrans);
		transactionList.setAdapter(myCursorAdapter);

	}
	
	//Refresh the view on activity resume.
		@Override
		protected void onResume() {
			super.onResume();
			if(AFTER_EDIT == 1) {
				Log.d("CALLED ON RESUME!!!!!!!!!!!!!!!!!!!!!!", "STOP IT!!");
				db.cleanTransactions(this, spinDateEnd);
				loadAccountSpinnerData();
				transAccount.setSelection(S_A_ID);
				populateListViewTransactions(A_ID);
			}
		};
	
	//Set the value of the currently viewed account id.
	public void setViewAccount(int id) {
		A_ID = id;
	}
}
