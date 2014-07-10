package com.example.budgetnotebook;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Added the adapter scaffolding and getters / setters.
 * 
 */

public class Account extends Activity {
	Button addAccount;	
	DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_account);

		//Create Database instance
		db = new DBHelper(getBaseContext());
						
		// Populate the ListView
		populateListViewAccounts();	
		
		// --------------------------------------------------------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
		
		// Set the ADD ACCOUNT button to display the ADD Account form when clicked
		addAccount = (Button) findViewById(R.id.addAccount);
		addAccount.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
				try{
					Class clickedClass = Class.forName("com.example.budgetnotebook.AccountForm");
					Intent newIntent = new Intent(Account.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});
	};
	
	// Close the Database on destroy.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	};
	
	// This method uses the Cursor getAllAccounts and populates the ListView on the view_account layout with a list of template_list_accounts (layouts)
	@SuppressWarnings("deprecation")
	private void populateListViewAccounts() {
	
		// Set a cursor with all the Accounts
		Cursor cursor = db.getAllAccounts();
		
		startManagingCursor(cursor);
		
		// Map the ACCOUNT_TABLE fields to the TextViews on the template_list_account layout.
		String[] accountFieldNames = new String[] {db.ACCOUNT_NAME, db.BALANCE};
		int[] toViewIDs = new int[] {R.id.accountName, R.id.accountBalance};
	
		// Fills the ListView with all the Accounts in the Table.
		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.template_list_account,
				cursor,
				accountFieldNames,
				toViewIDs
				);
		ListView accountList = (ListView) findViewById(R.id.listViewAccounts);
		accountList.setAdapter(myCursorAdapter);

	}
	
	
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

