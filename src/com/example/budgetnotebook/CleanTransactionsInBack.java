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
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Boolean doInBackground(DBHelper... arguments) {
		db = arguments[0];
		db.cleanTransactions(context, "now");
		db.checkTransactionStatus();
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
		db.toastAlerts(context, "TRAN");
	}	
}