package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// This method displays the splash screen briefly and then starts the main program

public class Splash extends Activity {
	boolean profile_exists, finished;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// Display splash view then wait 1 second
		setContentView(R.layout.splash);
		
		// Initialize an instance of the database.
		DBHelper db = new DBHelper(getBaseContext());
		
		// Clean the transactions accounting through the current date.
		CleanTransactionsInBack clean = new CleanTransactionsInBack ();
		clean.setContext(getApplicationContext());
		clean.execute(db);	
		
		//Check the status of the Goals and adjust their icons.
		CheckGoalStatus checkGoal = new CheckGoalStatus ();
		checkGoal.setContext(getApplicationContext());
		checkGoal.execute(db);	

		// Check if a profile exists.
		if (db.checkProfileExists() == 0) {
			profile_exists = false;
		} else {
			profile_exists = true;
		}
		
		Thread timer = new Thread(){
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// Display main menu if a profile exists. Otherwise prompt user to create a profile
					Intent openMainActivity;

					if (profile_exists) {
						openMainActivity = new Intent("com.example.budgetnotebook.MAINMENU");
					} else {
						//openMainActivity = new Intent("com.example.budgetnotebook.PROFILE");
						openMainActivity = new Intent("com.example.budgetnotebook.PROFILEFORM");
					}
							
					startActivity(openMainActivity);
					
				}
			}
		};
		
		timer.start();
	}
	
	@Override
	protected void onPause() {
		// Kill splash activity once complete
		super.onPause();
		finish();
	}
}