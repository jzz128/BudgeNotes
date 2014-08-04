/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * AdvancedMenu.java
 * 
 * View activity for displaying options for advanced functionality.
 * 
 **/

package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdvancedMenu extends Activity {
	// UI components
	Button vewReports;
	Button viewRecomendations;
	Button exportTrans;
	Button importTrans;
	Button viewAlerts;

	// Perform operations when the class is created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_menu);
		
		// Listener for Report View Button.
		vewReports = (Button) findViewById(R.id.advancedReports);
		vewReports.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
			try{
				Class<?> clickedClass = Class.forName("com.example.budgetnotebook.Report");
				Intent newIntent = new Intent(AdvancedMenu.this, clickedClass);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}	
		});
		
		// Listener for Recommendation View Button.
		viewRecomendations = (Button) findViewById(R.id.advancedRecommendations);
		viewRecomendations.setOnClickListener(new View.OnClickListener() {		
						
		@Override
		public void onClick(View v) {
			try{
				// Start the view recommendation activity
				Class<?> clickedClass = Class.forName("com.example.budgetnotebook.RecommendationView");
				Intent newIntent = new Intent(AdvancedMenu.this, clickedClass);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}	
		});
		
		//Listener for Export Transaction Button.
		exportTrans = (Button) findViewById(R.id.advancedExportTransactions);
		exportTrans.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
			try{
				// Start the export transaction activity
				Class<?> clickedClass = Class.forName("com.example.budgetnotebook.ExportActivity");
				Intent newIntent = new Intent(AdvancedMenu.this, clickedClass);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}	
			}	
		});
		
		//Listener for Import Transaction Button.
		importTrans = (Button) findViewById(R.id.advancedImportTransactions);
		importTrans.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
			try{
				// Start the import transaction activity 
				Class<?> clickedClass = Class.forName("com.example.budgetnotebook.ImportActivity");
				Intent newIntent = new Intent(AdvancedMenu.this, clickedClass);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}	
			}	
		});
		
		//Listener for Alert View Button.
		viewAlerts = (Button) findViewById(R.id.advancedAlerts);
		viewAlerts.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
			try{
				Class<?> clickedClass = Class.forName("com.example.budgetnotebook.AlertActivity");
				Intent newIntent = new Intent(AdvancedMenu.this, clickedClass);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}	
			}	
		});
	}
}
