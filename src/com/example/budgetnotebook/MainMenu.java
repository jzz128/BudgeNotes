package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
	boolean profile_exists = true; // This is being used temporarily until the profile functionality is created
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
		
		// Display the Main Menu if a profile exists
		if (profile_exists) {
			setContentView(R.layout.main_menu);
		}
		// Display the Create Profile page if a profile does not exist
		else {
			setContentView(R.layout.create_profile);	
		}
		
		// ---------------------------------------------------------------------------------------------------
		// Testing the Goal Database -------------------------------------------------------------------------
		// ---------------------------------------------------------------------------------------------------
		
		DBHelper db = new DBHelper(this);
		/**
		 * CRUD Operations
		 * */
		
		// Add Goal
		db.addGoal(new Goal("Goal1", "This is a goal", "Purchase", "500", "200", "One Week"));
		db.addGoal(new Goal("Goal2", "This is another goal", "Purchase", "700", "200", "Two Weeks"));
		db.addGoal(new Goal("Goal3", "This is another goal", "Purchase", "700", "200", "Two Weeks"));
		
		// Get All Goals
		//List<Goal> list = db.getAllGoals();
		db.toastGoal(getBaseContext());
		
		// Delete one Goal
		//db.deleteGoal(list.get(0));
		
		// Get all Goals
		db.getAllGoals();
				
		// ---------------------------------------------------------------------------------------------------
		// ---------------------------------------------------------------------------------------------------
		
		//Set the VIEW ACCOUNT button to display the VIEW ACCOUNT page when clicked
		view_account = (Button) findViewById(R.id.view_account);
		view_account.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					Class clickedClass = Class.forName("com.example.budgetnotebook.Account");
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
					Class clickedClass = Class.forName("com.example.budgetnotebook.Transaction");
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
				//setContentView(R.layout.view_profile);
				try{
					Class clickedClass = Class.forName("com.example.budgetnotebook.Profile");
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
				//setContentView(R.layout.view_goals);
				try{
					Class clickedClass = Class.forName("com.example.budgetnotebook.Goal");
					Intent newIntent = new Intent(MainMenu.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
						
			}
		});
		
		// Set the VIEW RECOMMENDATIONS button to display the VIEW RECOMMENDATIONS page when clicked
		view_recommendations = (Button) findViewById(R.id.view_recommendations);
		view_recommendations.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//setContentView(R.layout.view_recommendations);
				try{
					Class clickedClass = Class.forName("com.example.budgetnotebook.Recommendation");
					Intent newIntent = new Intent(MainMenu.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
						
			}
		});
		
		// Set the EXIT button to exit the application when clicked
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				finish();
	            System.exit(0);
						
			}

		});	
	}
}



