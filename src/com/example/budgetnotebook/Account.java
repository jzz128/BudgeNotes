package com.example.budgetnotebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Added the adapter scaffolding and getters / setters.
 * 
 */

public class Account extends Activity {
	Button addAccount;	
	DBHelper db;
	
	RelativeLayout vwParentRow;
	ListView accountList;
	
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
					
					// Brings us back to the root activity, where exit functions properly.
					newIntent.setFlags(newIntent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});
	};
	
	/*
	// Close the Database on destroy.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		finish();
	};
	*/
	
	// This method uses the Cursor getAllAccounts and populates the ListView on the view_account layout with a list of template_list_accounts (layouts)
	@SuppressWarnings("deprecation")
	private void populateListViewAccounts() {
	
		// Set a cursor with all the Accounts
		Cursor cursor = db.getAllAccounts();
		
		//startManagingCursor(cursor);
		
		// Map the ACCOUNT_TABLE fields to the TextViews on the template_list_account layout.
		String[] accountFieldNames = new String[] {db.A_ID, db.ACCOUNT_NAME, db.BALANCE};
		int[] toViewIDs = new int[] {R.id.accountID, R.id.accountName, R.id.accountBalance};
	
		// Fills the ListView with all the Accounts in the Table.
		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.template_list_account,
				cursor,
				accountFieldNames,
				toViewIDs
				);
		accountList = (ListView) findViewById(R.id.listViewAccounts);
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
	
	// Set Icon Button click action.
	public void iconClickHandler(View v) {
		
		int a_id;
				
		// Get the row the clicked button is in
        vwParentRow = (RelativeLayout)v.getParent();
        
        // Get the object that the account ID is stored in
        TextView child = (TextView)vwParentRow.getChildAt(1);
        
     // Store the account id in the variable integer.
        a_id = Integer.parseInt((child.getText().toString().trim()));
        
        try {
        	Class clickedClass = Class.forName("com.example.budgetnotebook.Transaction");
        	Intent newIntent = new Intent(Account.this,clickedClass);

			// Brings us back to the root activity, where exit functions properly.
			newIntent.setFlags(newIntent.FLAG_ACTIVITY_CLEAR_TOP);
        	newIntent.putExtra("A_ID", a_id);
        	startActivity(newIntent);
		
        } catch(ClassNotFoundException e) {
        	e.printStackTrace();
        }
	}
	
	// Set Edit Button click action.
		public void editClickHandler(View v) {
			
			int a_id;
					
			// Get the row the clicked button is in
	        vwParentRow = (RelativeLayout)v.getParent();
	        
	        // Get the object that the account ID is stored in
	        TextView child = (TextView)vwParentRow.getChildAt(1);
	        
	     // Store the account id in the variable integer.
	        a_id = Integer.parseInt((child.getText().toString().trim()));
	        
	        try {
	        	Class clickedClass = Class.forName("com.example.budgetnotebook.AccountForm");
	        	Intent newIntent = new Intent(Account.this,clickedClass);

				// Brings us back to the root activity, where exit functions properly.
				newIntent.setFlags(newIntent.FLAG_ACTIVITY_CLEAR_TOP);
				
				// Pass the extras to the intent on AccountForm.
	        	newIntent.putExtra("A_ID", a_id);
	        	newIntent.putExtra("A_EDIT", true);
	        	startActivity(newIntent);
			
	        } catch(ClassNotFoundException e) {
	        	e.printStackTrace();
	        }
		}
	
	// Set Delete Button click action.
		public void deleteClickHandler(View v) {
			
			int a_id;
			String a_name;
			
			// Get the row the clicked button is in
	        vwParentRow = (RelativeLayout)v.getParent();
	        
	        // Get the object that the account ID is stored in
	        TextView child = (TextView)vwParentRow.getChildAt(1);
	        
	        // Get the object that the account name is stored in
	        TextView child2 = (TextView)vwParentRow.getChildAt(2);
	        
	        // Store the account id in the variable integer.
	        a_id = Integer.parseInt((child.getText().toString().trim()));
	        
	        // Get the account referenced by the variable integer.
	        final Account account = db.getAccount(a_id);
	        
	        // Store the account name in the variable string.
	        a_name = child2.getText().toString().trim();
	        
	        // Alert dialog to affirm delete.
	        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                switch (which){
	                case DialogInterface.BUTTON_POSITIVE:
	                    db.deleteAccount(account);
	                    
	                 // Populate the ListView
	            		populateListViewAccounts();	
	                    
	                    break;

	                case DialogInterface.BUTTON_NEGATIVE:
	                    //Do Nothing.
	                    break;
	                }
	            }
	        };

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Are you sure you want to delete account, " +a_name+"?").setPositiveButton("Yes", dialogClickListener)
	            .setNegativeButton("No", dialogClickListener).show();
	        
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

