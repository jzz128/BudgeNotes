package com.example.budgetnotebook;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {
	Button view_account;
	Button add_transaction;
	Button view_profile;
	Button view_goals;
	Button view_recommendations;
	Button exit;
	Button main_menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		// !!! ================================================================== Testing Some App Open Functionality for Recurring / Future Transactions ======================== !!!
		
		DBHelper db = new DBHelper(this);
		int numAccounts;
		int numTran = 0;
		int tranSum = 0;
		int accBase = 0;
		int a_id;
		String query;
		
		// Check if any accounts exist. Could be replaced with comparison based on a queryCount() call.
		if (db.checkAccountExists()) {
			// Get a list off all the accounts and a count of how many there are.
			List<Account> accList = db.getListAllAccounts();
			numAccounts = accList.size();				
			
			// Check if any transactions exist.
			if(db.checkTransExists()) {
				// Iterate through all accounts.
				for (int i = 0; i < numAccounts; i++) {
					// Get the _id of the current account.
					a_id = accList.get(i).getId();
					//Establish a base query returning all transactions associated with an account.
					query = "SELECT * FROM " + db.TRANSACTION_TABLE + " WHERE " + db.T_A_ID + " = " + a_id;
					//Returns the total count of transactions associated with the current account.
					numTran = db.queryCount(query);
					//Returns a sum of all Transactions that are currently accounted for.
					tranSum = db.querySum(query + " AND " + db.TRANSACTION_ACCOUNTED + " = " + 1);
					//Returns the base (beginning) balance of the current account.
					accBase = Integer.parseInt(accList.get(i).getBalance()) - tranSum;
					
					// Reset the current account base balance.
					accList.get(i).setBalance(String.valueOf(accBase));
					db.updateAccount(accList.get(i));
					
					//Toast.makeText(this, "There are: " + String.valueOf(numTran) + " in Account with ID= " + a_id, Toast.LENGTH_LONG).show();
					//Toast.makeText(this, "There is a total sum of: " + String.valueOf(tranSum) + " in Account with ID= " + a_id, Toast.LENGTH_LONG).show();
					//Toast.makeText(this, "There is a base balance of: " + String.valueOf(accBase) + " in Account with ID= " + a_id, Toast.LENGTH_LONG).show();
				}
				// Get a list of all the transactions and a count of how many there are.
				List<Transaction> tranList = db.getAllListTransactions();
				int count = tranList.size();
				
				Account account;
				String currBalance;
				String changeAmount;
				int newBalance;
				// 
				for (int i = 0; i < count; i++) {
					account = db.getAccount(tranList.get(i).getAID());
					currBalance = account.getBalance();
					
					tranList.get(i).setAccounted(db.checkAccountedDate(tranList.get(i).getDate()));
					db.updateTransaction(tranList.get(i));
					
					changeAmount = tranList.get(i).getAmount();
					
					if(tranList.get(i).getAccounted()) {
						
						newBalance = Integer.parseInt(currBalance) + Integer.parseInt(changeAmount);
						account.setBalance(String.valueOf(newBalance));
						
						db.updateAccount(account);
					}
				}
			} else {
				Toast.makeText(this, "No Transactions Exist to update!", Toast.LENGTH_LONG).show();
			}
			
		} else {
			Toast.makeText(this, "No Accounts Exist to update!", Toast.LENGTH_LONG).show();
		}
		
		db.close();
		
		// !!! =================================================================================================================================================================== !!!
		
		//Set the VIEW ACCOUNT button to display the VIEW ACCOUNT page when clicked
		view_account = (Button) findViewById(R.id.view_account);
		view_account.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				try{
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.Account");
					Intent newIntent = new Intent(MainMenu.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}
		});
			
		// Set the ADD TRANSACTION button to display the ADD TRANSACTION page when clicked
		add_transaction = (Button) findViewById(R.id.add_transaction);
		add_transaction.setOnClickListener(new View.OnClickListener() {
					
			
			@Override
			public void onClick(View v) {
				try{
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.TransactionForm");
					Intent newIntent = new Intent(MainMenu.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});
		
		// Set the VIEW PROFILE button to display the VIEW PROFILE page when clicked
		view_profile = (Button) findViewById(R.id.view_profile);
		view_profile.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				try{
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.Profile");
					Intent newIntent = new Intent(MainMenu.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}		
			}
		});
		
		// Set the VIEW GOALS button to display the VIEW GOALS page when clicked
		view_goals = (Button) findViewById(R.id.view_goals);
		view_goals.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				try{
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.Goal");
					Intent newIntent = new Intent(MainMenu.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}	
			}
		});
		
		// ======================================================================================================================
		// Set the VIEW RECOMMENDATIONS button to display the VIEW RECOMMENDATIONS page when clicked
		view_recommendations = (Button) findViewById(R.id.view_recommendations);
		view_recommendations.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				try{
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.AdvancedMenu");
					Intent newIntent = new Intent(MainMenu.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}		
			}
		});
		// ======================================================================================================================
		
		// Set the EXIT button to exit the application when clicked
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//finish();
	            System.exit(0);
						
			}

		});	
	
	}

}