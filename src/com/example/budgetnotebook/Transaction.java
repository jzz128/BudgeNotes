package com.example.budgetnotebook;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Transaction extends Activity {
	// An id for the account transactions to view.
	// It must be set by the calling class, or it will show all transactions.
	int A_ID;
	
	Spinner transAccount;
	String[] seperated;
	Button addTransaction;
	DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_transaction);
				
		//Create Database instance
		db = new DBHelper(getBaseContext());
		
		// Initialize the spinner.
		transAccount = (Spinner) findViewById(R.id.transAccountSpinner);
		
		// Add data to the spinner.
		loadAccountSpinnerData();
		
		// Get the extras from previous activity.
		Intent intent = getIntent();
		A_ID = intent.getIntExtra("A_ID",0);
		
		transAccount.setSelection(A_ID-1,false);
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
					Class clickedClass = Class.forName("com.example.budgetnotebook.TransactionForm");
					Intent newIntent = new Intent(Transaction.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});
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
	
	public Transaction(){}
	
	public Transaction(int t_a_id, String transaction_name, String transaction_date, String transaction_amount, String transaction_category, String transaction_type, String transaction_interval, String transaction_description) {
		super();
		this.t_a_id = t_a_id;
		this.transaction_name = transaction_name;
		this.transaction_date = transaction_date;
		this.transaction_amount = transaction_amount;
		this.transaction_category = transaction_category;
		this.transaction_type = transaction_type;
		this.transaction_interval = transaction_interval;
		this.transaction_description = transaction_description;
	}
	
	@Override
	public String toString() {
		return "Goal [id=" + _id + ", t_a_id=" + t_a_id + ", transaction_name=" + transaction_name + ", transaction_date=" + transaction_date + ", transaction_amount=" + transaction_amount + ", transaction_category=" + transaction_category + ", transaction_type=" + transaction_type + ", transaction_interval=" + transaction_interval + ", transaction_description=" + transaction_description +"]";
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
				
		startManagingCursor(cursor);
				
		// Map the TRANSACTION_TABLE fields to the TextViews on the template_list_transaction layout.
		String[] transactionFieldNames = new String[] {DBHelper.T_A_ID, DBHelper.TRANSACTION_DATE, DBHelper.TRANSACTION_AMOUNT};
		int[] toViewIDs = new int[] {R.id.transAccount, R.id.transDate, R.id.transAmount};
			
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
	
	//Setters --------------------------------------------------------------------
	public void setViewAccount(int id) {
		this.A_ID = id;
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

}
