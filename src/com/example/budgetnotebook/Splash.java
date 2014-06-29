package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// This method displays the splash screen briefly and then starts the main program

public class Splash extends Activity {
	boolean profile_exists = true; // This is being used temporarily until the profile functionality is created

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display splash view then wait 1 second
		setContentView(R.layout.splash);		
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
						openMainActivity = new Intent("com.example.budgetnotebook.PROFILE");
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
