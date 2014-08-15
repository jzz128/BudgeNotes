/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * CheckGoalStatus.java
 * 
 *AsyncTask launched from the application splash screen.  Updates the status of user created goals and auto generated alerts.
 *Alerts are displayed via Toast to the user when task complete.
 * 
 **/

package com.example.budgetnotebook;

import android.content.Context;
import android.os.AsyncTask;

public class CheckGoalStatus extends AsyncTask<DBHelper, String, Boolean> {
	Context context;
	DBHelper db;
	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	protected Boolean doInBackground(DBHelper... arguments) {
		db = arguments[0];
		//Check the status of goals, update goals and update goal alerts.
		db.checkGoalStatus("now");
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		//Display Goal alerts to user.
		super.onPostExecute(result);
		db.toastAlerts(context, "GOAL");
	}
	
}