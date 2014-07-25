package com.example.budgetnotebook;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Transaction extends Activity {
	// An id for the account transactions to view.
	// It must be set by the calling class, or it will show all transactions.
	int A_ID;
	int S_A_ID;
	int lowestID;
	
	Account account;
	Transaction transaction;
	
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
		String cal_for_month = Integer.toString(cal.get(Calendar.MONTH)+1);
		String cal_for_year = Integer.toString(cal.get(Calendar.YEAR));
		String cal_for_day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String todayAsString = cal_for_month + "/" + cal_for_day + "/" + cal_for_year;
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
					Intent newIntent = new Intent(Transaction.this, clickedClass);
					
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
	
	public void iconClickHandler(View v) {
		//TODO Do Something when Transaction Icon clicked. Or Nothing.
	}
	
	public void dateClickHandler(View v) {
		showDialog(0);
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
					spinDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
					transDate.setText(spinDate);
					seeFuture(spinDate);
		}
	};
	
	public void seeFuture (String date) {
		// !!! ====================================================================================================================================== !!!
		// !!! TESTING - SET ALL TRANSACTIONS TO BE ACCOUNTED, EVEN FUTURE DATES.
		// !!! ====================================================================================================================================== !!!
				
		List<Transaction> tranList = db.getAllListTransactions(A_ID);
		int numTrans = tranList.size();
		Account account;
		String currBalance;
		String changeAmount;
		int newBalance;
		boolean pAccounted;
				
		for (int i = 0; i < numTrans; i++) {
			account = db.getAccount(tranList.get(i).getAID());
			currBalance = account.getBalance();
			pAccounted = tranList.get(i).getAccounted();
						
			// Set the threshold date here -----------------------------------------------
			tranList.get(i).setAccounted(db.checkAccountedDate(tranList.get(i).getDate(), date));
			// --------------------------------------------------------------------------
			
			db.updateTransaction(tranList.get(i));
			//Toast.makeText(this, String.valueOf(pAccounted), Toast.LENGTH_LONG).show();
			//Toast.makeText(this, String.valueOf(tranList.get(i).getAccounted()), Toast.LENGTH_LONG).show();
			
			if(!pAccounted && tranList.get(i).getAccounted()) {
				changeAmount = tranList.get(i).getAmount();
				newBalance = Integer.parseInt(currBalance) + Integer.parseInt(changeAmount);
				account.setBalance(String.valueOf(newBalance));				
				db.updateAccount(account);
				tranList.get(i).setChange(account.getBalance());
				db.updateTransaction(tranList.get(i));
			} else if (pAccounted && !tranList.get(i).getAccounted()) {
				changeAmount = tranList.get(i).getAmount();
				newBalance = Integer.parseInt(currBalance) - Integer.parseInt(changeAmount);
				account.setBalance(String.valueOf(newBalance));				
				db.updateAccount(account);
				tranList.get(i).setChange(null);
				db.updateTransaction(tranList.get(i));
			} else if(pAccounted == tranList.get(i).getAccounted()) {
				//Do Nothing.
			} else {
				Toast.makeText(this, "An Error has Occured!", Toast.LENGTH_LONG).show();
			}
			
			loadAccountSpinnerData();
			populateListViewTransactions(A_ID);
		}		
		// !!! ====================================================================================================================================== !!!
		// !!! ====================================================================================================================================== !!!
	}
	
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
        
        try {
        	Class<?> clickedClass = Class.forName("com.example.budgetnotebook.TransactionForm");
        	Intent newIntent = new Intent(Transaction.this,clickedClass);

			// Brings us back to the root activity, where exit functions properly.
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Pass the extras to the intent on AccountForm.
        	newIntent.putExtra("A_ID", a_id);
        	newIntent.putExtra("T_ID", t_id);
        	newIntent.putExtra("T_EDIT", true);
        	startActivity(newIntent);
		
        } catch(ClassNotFoundException e) {
        	e.printStackTrace();
        }
	}
	
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
        
		// Alert dialog to affirm delete.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                	reverseTransaction();
                	db.deleteTransaction(transaction);
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
	
	//Update the account if this is an edit. Essentially reversing the original transaction
	//TODO update with new transaction method.
		private void reverseTransaction() {		
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
			accountType = account.getType();
			
			if (new String("CR").equals(accountType)) {
				changeAmount = -changeAmount;
			}
			
			oldBalance = Integer.parseInt(account.getBalance());
			newBalance = oldBalance - changeAmount;
			account.setBalance(String.valueOf(newBalance));
			if(transaction.getAccounted()) db.updateAccount(account);
			
		}
	
	private int _id;
	private int t_a_id;
	private String transaction_name;
	private String transaction_date;
	private String transaction_amount;
	private String transaction_category;
	private String transaction_type;
	private String transaction_interval;
	private String transaction_description;
	private boolean transaction_accounted;
	private String transaction_change;
	
	public Transaction(){}
	
	public Transaction(int t_a_id, String transaction_name, String transaction_date, String transaction_amount, String transaction_category, String transaction_type, String transaction_interval, String transaction_description, boolean transaction_accounted, String transaction_change) {
		super();
		this.t_a_id = t_a_id;
		this.transaction_name = transaction_name;
		this.transaction_date = transaction_date;
		this.transaction_amount = transaction_amount;
		this.transaction_category = transaction_category;
		this.transaction_type = transaction_type;
		this.transaction_interval = transaction_interval;
		this.transaction_description = transaction_description;
		this.transaction_accounted = transaction_accounted;
		this.transaction_change = transaction_change;
	}
	
	@Override
	public String toString() {
		return "Transaction [id=" + _id + ", t_a_id=" + t_a_id + ", transaction_name=" + transaction_name + ", transaction_date=" + transaction_date + ", transaction_amount=" + transaction_amount + ", transaction_category=" + transaction_category + ", transaction_type=" + transaction_type + ", transaction_interval=" + transaction_interval + ", transaction_description=" + transaction_description +", transaction_accounted=" + transaction_accounted + "transaction_change=" + transaction_change + "]";
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
		Cursor cursor = db.getAllTransactions(A_ID);
			
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
		ListView transactionList = (ListView) findViewById(R.id.listViewTrans);
		transactionList.setAdapter(myCursorAdapter);
		
	}
		
	/*
	// Close the Database on destroy.
		@Override
		protected void onDestroy() {
			super.onDestroy();
			db.close();
			finish();
		};
	*/
	
	//Getters --------------------------------------------------------------------
	public int getId(){
		return _id;
	}
	
	public int getAID() {
		return t_a_id;
	}
	
	public String getName() {
		return transaction_name;
	}
	
	public String getDate() {
		return transaction_date;
	}
	
	public String getAmount() {
		return transaction_amount;
	}
	
	public String getCategory() {
		return transaction_category;
	}
	
	public String getType() {
		return transaction_type;
	}
	
	public String getInterval() {
		return transaction_interval;
	}
	
	public String getDescription() {
		return transaction_description;
	}
	
	public boolean getAccounted() {
		return transaction_accounted;
	}
	
	public String getChange() {
		return transaction_change;
	}
	
	//Setters --------------------------------------------------------------------
	public void setViewAccount(int id) {
		A_ID = id;
	}
	
	public void setId(int id){
		this._id = id;
	}
	
	public void setAId(int a_id){
		this.t_a_id = a_id;
	}
	
	public void setName(String name){
		this.transaction_name = name;
	}
	
	public void setDate(String transaction_date) {
		this.transaction_date = transaction_date;
	}
	
	public void setAmount(String transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	
	public void setCategory(String transaction_category) {
		this.transaction_category = transaction_category;
	}
	
	public void setType(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	public void setInterval(String transaction_interval) {
		this.transaction_interval = transaction_interval;
	}
	
	public void setDescription(String transaction_description) {
		this.transaction_description = transaction_description;
	}
	
	public void setAccounted(boolean transaction_accounted) {
		this.transaction_accounted = transaction_accounted;
	}
	
	public void setChange(String transaction_change) {
		this.transaction_change = transaction_change;
	}

}
