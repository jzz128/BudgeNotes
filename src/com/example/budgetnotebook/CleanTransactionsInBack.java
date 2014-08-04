/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * CleanTransactionsInBack.java
 * 
 *AsyncTask sets the accounted status of each transaction from application splash screen, and updates account balance through the current date.  
 *Updates auto generated alerts through the current date.
 *Displays recurring transaction alerts to the user via Toast.
 * 
 **/

package com.example.budgetnotebook;

import android.content.Context;
import android.os.AsyncTask;

public class CleanTransactionsInBack extends AsyncTask<DBHelper, String, Boolean> {
	Context context;
	DBHelper db;
	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	protected Boolean doInBackground(DBHelper... arguments) {
		db = arguments[0];
		//Clean transactions through current date, and update recurring transaction alerts.
		db.cleanTransactions(context, "now");
		db.checkTransactionStatus();
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		//Display recurring transaction alerts to user.
		super.onPostExecute(result);
		db.toastAlerts(context, "TRAN");
	}	
}