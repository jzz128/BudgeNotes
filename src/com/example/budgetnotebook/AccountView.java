/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * AccountView.java
 * 
 * View activity for displaying records in the DB Account Table.
 * 
 **/

package com.example.budgetnotebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountView extends Activity {
	//Class variable definitions.
	Button addAccount;	
	DBHelper db;
	
	RelativeLayout vwParentRow;
	ListView accountList;
	
	//Perform operations when the class is created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_account);

		//Create Database instance
		db = new DBHelper(getBaseContext());
						
		// Populate the ListView
		populateListViewAccounts();	
				
		// Set the ADD ACCOUNT button to display the ADD Account form when clicked
		addAccount = (Button) findViewById(R.id.addAccount);
		addAccount.setOnClickListener(new View.OnClickListener() {		
						
		@Override
		public void onClick(View v) {
			try{
				//Take us to the account form.
				Class<?> clickedClass = Class.forName("com.example.budgetnotebook.AccountForm");
				Intent newIntent = new Intent(AccountView.this, clickedClass);
					
				// Brings us back to the root activity, where exit functions properly.
				newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}				
		});
	};
	
	//Refresh the view on activity resume.
	@Override
	protected void onResume() {
		super.onResume();
		db.cleanTransactions(this);
		populateListViewAccounts();
	};
	
	// This method uses the Cursor getAllAccounts and populates the ListView on the view_account layout with a list of template_list_accounts (layouts)
	@SuppressWarnings("deprecation")
	private void populateListViewAccounts() {
	
		// Set a cursor with all the Accounts
		Cursor cursor = db.getAllAccounts();
		
		//startManagingCursor(cursor);
		
		// Map the ACCOUNT_TABLE fields to the TextViews on the template_list_account layout.
		String[] accountFieldNames = new String[] {DBHelper.A_ID, DBHelper.ACCOUNT_NAME, DBHelper.BALANCE};
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
        	//Take us to the transaction view on click.
        	Class<?> clickedClass = Class.forName("com.example.budgetnotebook.TransactionView");
        	Intent newIntent = new Intent(AccountView.this,clickedClass);

			// Brings us back to the root activity, where exit functions properly.
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	newIntent.putExtra("A_ID", a_id);
        	newIntent.putExtra("AFTER_EDIT", 0);
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
	       	Class<?> clickedClass = Class.forName("com.example.budgetnotebook.AccountForm");
	       	Intent newIntent = new Intent(AccountView.this,clickedClass);

			// Brings us back to the root activity, where exit functions properly.
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
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
}

