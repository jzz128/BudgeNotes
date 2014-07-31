package com.example.budgetnotebook;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateDatabase extends AsyncTask<DBHelper, String, Boolean> {
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Boolean doInBackground(DBHelper... arguments) {
		DBHelper db = arguments[0];
		int numAccounts;
		int numTran = 0;
		float tranSum = 0;
		float accBase = 0;
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
					query = "SELECT * FROM " + DBHelper.TRANSACTION_TABLE + " WHERE " + DBHelper.T_A_ID + " = " + a_id;
					//Returns the total count of transactions associated with the current account.
					numTran = db.queryCount(query);
					Log.d("numTran", Integer.toString(numTran));
					//Returns a sum of all Transactions that are currently accounted for.
					tranSum = db.querySum(query + " AND " + DBHelper.TRANSACTION_ACCOUNTED + " = " + 1);
					//Returns the base (beginning) balance of the current account.
					accBase = Float.parseFloat((accList.get(i).getBalance())) - tranSum;
					
					// Reset the current account base balance.
					accList.get(i).setBalance(String.format("%.2f",accBase));
					db.updateAccount(accList.get(i));
					
				}
				// Get a list of all the transactions and a count of how many there are.
				List<Transaction> tranList = db.getAllListTransactions(0);
				int count = tranList.size();
				
				Account account;
				String currBalance;
				String changeAmount;
				float newBalance;
				// Iterate through all the transactions
				for (int i = 0; i < count; i++) {
					//Get the current transaction Account and balance.
					account = db.getAccount(tranList.get(i).getAID());
					currBalance = account.getBalance();
					// Set the accounted flag against today's date and update the transaction
					tranList.get(i).setAccounted(db.checkAccountedDate(tranList.get(i).getDate(), "now"));
					db.updateTransaction(tranList.get(i));
					//Get the transaction amount.
					changeAmount = tranList.get(i).getAmount();
					// If the transaction is accounted update the account balance.
					if(tranList.get(i).getAccounted()) {
						newBalance = Float.parseFloat(currBalance) + Float.parseFloat(changeAmount);
						account.setBalance(String.format("%.2f",newBalance));
						db.updateAccount(account);
						tranList.get(i).setChange(account.getBalance());
						
						if (newBalance > 100) {tranList.get(i).setCColor("#006400");} else
						if (0 <= newBalance  && newBalance <= 100) {tranList.get(i).setCColor("#DAA520");} else
						if (newBalance < 0) {tranList.get(i).setCColor("#FF0000");}
						
						db.updateTransaction(tranList.get(i));
					} else {
						tranList.get(i).setChange(null);
						tranList.get(i).setCColor(null);
						db.updateTransaction(tranList.get(i));
					}
				}
			} else {
				//Toast.makeText(context, "No Transactions Exist to update!", Toast.LENGTH_LONG).show();
			}
			
		} else {
			//Toast.makeText(context, "No Accounts Exist to update!", Toast.LENGTH_LONG).show();
		}
		return true;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
}
