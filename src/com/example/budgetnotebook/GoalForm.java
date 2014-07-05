package com.example.budgetnotebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class GoalForm extends Activity{
	// Eventually these will map to the TextViews on form_goal layout to create a new goal record.
	Button saveGoal;
	DBHelper db;
	Spinner goalAccount;
	String[] seperated;
	EditText goalName;
	Spinner goalType;
	EditText goalEnd;
	EditText goalStart;
	EditText goalDelta;
	EditText goalDescription;
	
	int goalAccountI;
	String goalNameS;
	String goalTypeS;
	String goalEndS;
	String goalStartS;
	String goalDeltaS;
	String goalDescriptionS;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_goal);
		
		// Access the database.
		db = new DBHelper(getBaseContext());
		
		// Initialize the Spinners.
		goalAccount = (Spinner) findViewById(R.id.goalEditAccountSpinner);
		goalType = (Spinner) findViewById(R.id.goalEditTypeSpinner);
		
		// Associate fields in the Goal form (form_goal.xml) to our variables.
		goalName = (EditText) findViewById(R.id.goalEditName);
		goalEnd = (EditText) findViewById(R.id.goalEditEnd);
		goalStart = (EditText) findViewById(R.id.goalEditStart);
		goalDelta = (EditText) findViewById(R.id.goalEditDelta);
		goalDescription = (EditText) findViewById(R.id.goalEditDescription);
		
		// Add data to the spinners.
		loadAccountSpinnerData();
		loadTypeSpinnerData();
		
		// Set a listener for the Account spinner selection.
		goalAccount.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//Get the Account spinner data and put it in a string array.
				seperated = goalAccount.getSelectedItem().toString().split(" ");
					
				// Create an account object with account data of selected account.
				Account account = db.getAccount(Integer.parseInt(seperated[0]));
				
				// Populate the current amount with the account balance
				goalStart.setText(account.getBalance());
										
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
		});
		
		// Set a listener for the Type spinner selection.
		goalType.setOnItemSelectedListener(new OnItemSelectedListener() {
					
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub			
			}
		});
						
		// Set the ADD GOAL button to display the ADD Goal form when clicked
		saveGoal = (Button) findViewById(R.id.goalButtonSave);
		saveGoal.setOnClickListener(new View.OnClickListener() {		
								
			@Override
			public void onClick(View v) {
				try{
															
					// Transfer edit text to GOAL_TABLE attribute types.
					goalAccountI = Integer.parseInt(seperated[0]);
					goalNameS = goalName.getText().toString().trim();
					goalTypeS = goalType.getSelectedItem().toString().trim();
					goalEndS = goalEnd.getText().toString().trim();
					goalStartS = goalStart.getText().toString().trim();
					goalDeltaS = goalDelta.getText().toString().trim();
					goalDescriptionS = goalDescription.getText().toString().trim();
					
					// Call the add goal method to add the goal to the database!
					addGoal();
					
					
					Class clickedClass = Class.forName("com.example.budgetnotebook.Goal");
					Intent newIntent = new Intent(GoalForm.this, clickedClass);
					startActivity(newIntent);
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}				
		});
		
	};
	
	private void addGoal() {
		db.addGoal(new Goal(goalAccountI, goalNameS, goalDescriptionS, goalTypeS, goalStartS, goalDeltaS, goalEndS));
	}

	private void loadAccountSpinnerData() {
		List<String> list = db.getAllStringAccounts();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                     (this, android.R.layout.simple_spinner_item,list);
                      
        dataAdapter.setDropDownViewResource
                     (android.R.layout.simple_spinner_dropdown_item);
		 goalAccount.setAdapter(dataAdapter);
	}
	
	private void loadTypeSpinnerData() {
		ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.goalTypeArray, android.R.layout.simple_spinner_item);
                      
		goalType.setAdapter(dataAdapter);
	}
	
}
