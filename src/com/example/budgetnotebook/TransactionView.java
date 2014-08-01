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

public class TransactionView extends Activity {

	int A_ID;
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
	
	TextView transDate;
	private Calendar cal;
	private int day, month, year;
	private String spinDate;
	
	String[] interval;
	int intVal;
	Intent newIntent;
	
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
		
		// Initialize the calendar.
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		
		// Set date to todays date
		//cal= Calendar.getInstance();
		String cal_for_month;
		String cal_for_day;
		if(cal.get(Calendar.MONTH)+1 < 10) {
			cal_for_month = "0" + Integer.toString(cal.get(Calendar.MONTH)+1);
		} else {
			cal_for_month = Integer.toString(cal.get(Calendar.MONTH)+1);
		}
		
		if(cal.get(Calendar.DAY_OF_MONTH) < 10) {
			cal_for_day = "0" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		} else {
			cal_for_day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		}
		
		String cal_for_year = Integer.toString(cal.get(Calendar.YEAR));
		
		
		String todayAsString = cal_for_month + "/" + cal_for_day + "/" + cal_for_year;
		//rngDate = cal_for_year + cal_for_month + cal_for_day;
		spinDate = "now";
		transDate.setText(todayAsString);
		
		// Add data to the spinner.
		loadAccountSpinnerData();
		
		// Get the extras from previous activity.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",0);
        lowestID = db.lowestAccountID();
        S_A_ID = A_ID - lowestID + 1 ;
		
		transAccount.setSelection(S_A_ID-1,false);
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
				// TODO Auto-generated method stub		
			}
		});
				
		// Populate the ListView
		populateListViewTransactions(A_ID);
				
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
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});
		
	}
	
	//Perform operations when the icon button is cliacked.
	public void iconClickHandler(View v) {
		//TODO Do Something when Transaction Icon clicked. Or Nothing.
	}
	
	// Show the date picker when the date picker icon is clicked.
	@SuppressWarnings("deprecation")
	public void dateClickHandler(View v) {
		showDialog(0);
	}
	
	//Make the date picker dialog.
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	//Listener for date picker set button.
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
					spinDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
					//rngDate = Integer.toString(selectedYear) + Integer.toString((selectedMonth + 1)) + Integer.toString(selectedDay);
					transDate.setText(spinDate);
					db.seeFuture(getBaseContext(),spinDate,A_ID);
					loadAccountSpinnerData();
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
                	S_A_ID = A_ID - lowestID + 1 ;
            		transAccount.setSelection(S_A_ID-1);
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
		
		DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if(transaction.getName().toString().contains("-")) {
						String[] nSplit = new String[2];
						nSplit = transaction.getName().toString().split("-");
						Log.d(nSplit[1].toString(), nSplit[1].toString());
						transaction = db.getTransaction(Integer.parseInt(nSplit[1].toString()));
					} else {
						//Do Nothing.
					}
					db.deleteReccTransactions(transaction);
            		db.cleanTransactions(getBaseContext());
            		break;
				case DialogInterface.BUTTON_NEGATIVE:
					reverseTransaction();
            		db.deleteTransaction(transaction);
                    break;
				}
				loadAccountSpinnerData();
            	S_A_ID = A_ID - lowestID + 1 ;
        		transAccount.setSelection(S_A_ID-1);

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
		//Cursor cursor = db.getAllTransactions(A_ID);
		Cursor cursor = db.getAllTransactionsInRange(A_ID, spinDate);
		//startManagingCursor(cursor);
				
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
                  //Log.d("cursor value =",String.valueOf(s));
                  TextView tv = (TextView)view;

                  if(s != null) tv.setTextColor(Color.parseColor(s));
                  tv.setText(cursor.getString(10));
                  //Log.d("change amount=",cursor.getString(10));
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
			db.cleanTransactions(this);
			populateListViewTransactions(A_ID);
		};
	
	//Set the value of the currently viewed account id.
	public void setViewAccount(int id) {
		A_ID = id;
	}
}
