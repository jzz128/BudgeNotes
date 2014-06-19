package com.example.budgetnotebook;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainMenu extends Activity {
	//String classes[]  = {"MainActivity", "view_account", "add_transaction", "view_profile", "view_goals", "view_recommendations", "exit"};
	boolean profile_exists = true;
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
		if (profile_exists) {
			setContentView(R.layout.main_menu);
		}
		//Display the Create Profile page if a profile does not exist
		else {
			setContentView(R.layout.create_profile);	
		}
		// Set the VIEW ACCOUNT button to display the VIEW ACCOUNT page when clicked
		view_account = (Button) findViewById(R.id.view_account);
		view_account.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					Class mainClass = Class.forName("com.example.budgetnotebook.MainActivity");
					Intent mainIntent = new Intent(MainMenu.this, mainClass);
					startActivity(mainIntent);
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
				setContentView(R.layout.add_transaction);
						
			}
		});
		
		// Set the VIEW PROFILE button to display the VIEW PROFILE page when clicked
		view_profile = (Button) findViewById(R.id.view_profile);
		view_profile.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				setContentView(R.layout.view_profile);
						
			}
		});
		
		// Set the VIEW GOALS button to display the VIEW GOALS page when clicked
		view_goals = (Button) findViewById(R.id.view_goals);
		view_goals.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				setContentView(R.layout.view_goals);
						
			}
		});
		
		// Set the VIEW RECOMMENDATIONS button to display the VIEW RECOMMENDATIONS page when clicked
		view_recommendations = (Button) findViewById(R.id.view_recommendations);
		view_recommendations.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				setContentView(R.layout.view_recommendations);
						
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
		//setListAdapter(new ArrayAdapter<String>(MainMenu.this, android.R.layout.simple_expandable_list_item_1, classes));
		
	}
}
	/*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String className = classes[position];
		try{
			Class mainClass = Class.forName("com.example.budgetnotebook." + className);
			Intent mainIntent = new Intent(MainMenu.this, mainClass);
			startActivity(mainIntent);
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}*/
		

