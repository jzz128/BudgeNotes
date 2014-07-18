package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdvancedMenu extends Activity {
	Button vewReports;
	Button viewRecomendations;
	Button exportTrans;
	Button importTrans;
	Button viewAlerts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_menu);
		
		vewReports = (Button) findViewById(R.id.advancedReports);
		vewReports.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
			try{
				Class clickedClass = Class.forName("com.example.budgetnotebook.Report");
				Intent newIntent = new Intent(AdvancedMenu.this, clickedClass);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}	
		});
		
		viewRecomendations = (Button) findViewById(R.id.advancedRecommendations);
		viewRecomendations.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
			try{
				Class clickedClass = Class.forName("com.example.budgetnotebook.Recommendations");
				Intent newIntent = new Intent(AdvancedMenu.this, clickedClass);
				startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}	
		});
		
		exportTrans = (Button) findViewById(R.id.advancedExportTransactions);
		exportTrans.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
				// TODO Export Transactions table to a CSV file.
			}	
		});
		
		importTrans = (Button) findViewById(R.id.advancedImportTransactions);
		importTrans.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
				// TODO Import Transactions from a CSV file.
			}	
		});
		
		viewAlerts = (Button) findViewById(R.id.advancedAlerts);
		viewAlerts.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
				// TODO Open Alert view populated with alert data.
				// TODO Create Alert.java class
			}	
		});
	}
}