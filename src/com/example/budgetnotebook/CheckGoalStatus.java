package com.example.budgetnotebook;

import android.os.AsyncTask;

public class CheckGoalStatus extends AsyncTask<DBHelper, String, Boolean> {

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Boolean doInBackground(DBHelper... arguments) {
		DBHelper db = arguments[0];
		//TODO Check the status of goals and update them.
		db.checkGoalStatus();
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
