package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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