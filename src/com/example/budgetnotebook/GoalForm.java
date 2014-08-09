/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * GoalForm.java
 * 
 *Activity for the goal form.  Allows user to create / edit goals via form entry.
 * 
 **/

package com.example.budgetnotebook;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GoalForm extends Activity implements InputValidator{
	// UI Components
	Button saveGoal;
	DBHelper db;
	Spinner goalAccount;
	String[] seperated;
	EditText goalName;
	Spinner goalType;
	TextView goalEnd;
	ImageButton calendar;
	TextView goalStart;
	EditText goalDelta;
	EditText goalDescription;
	
	// UI Component backing variables
	private Calendar cal;
	private int day, month, year;
	
	private int goalAccountI;
	private String goalNameS;
	private String goalTypeS;
	private String goalEndS;
	private String goalStartS;
	private String goalDeltaS;
	private String goalDescriptionS;
	private String goalStatus;
	private String formatMonth, formatDay;
	
	int G_ID;
	int A_ID;
	int S_A_ID; // This will be used to set the spinner for the account. It is set in the populate form method. - DJM
	boolean G_EDIT;
	Goal goal;
	Alert alert;
	String query, alt_id;
	Cursor  cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.form_goal);
		
		// Access the database.
		db = new DBHelper(getBaseContext());
		
		// Get the id of the goal from the spinner on the goal view.
		Intent intent = getIntent();
		G_ID = intent.getIntExtra("G_ID",0);
		A_ID = intent.getIntExtra("A_ID", 0);
		G_EDIT = intent.getBooleanExtra("G_EDIT", false);
		S_A_ID = db.correctSpinID(A_ID);
		
		// Initialize the Spinners.
		goalAccount = (Spinner) findViewById(R.id.goalEditAccountSpinner);
		goalType = (Spinner) findViewById(R.id.goalEditTypeSpinner);
		
		// Add data to the spinners.
		loadAccountSpinnerData();
		loadTypeSpinnerData();
		
		// Associate fields in the Goal form (form_goal.xml) to our variables.
		saveFieldsToStrings();	// Moved this down in the list. - DJM
				
		// Auto fill the form if this is an edit.
		if (G_EDIT) {
			goal = db.getGoal(G_ID);
			alt_id = String.valueOf(goal.getId());
			query = "SELECT * FROM " + DBHelper.ALERT_TABLE + " WHERE substr(" + DBHelper.ALERT_NAME + ",6) LIKE " + alt_id;
			cursor = db.dbQuery(query);
			Log.d("CURSOR SIZE=",String.valueOf(cursor.getCount()));
			if(cursor.moveToFirst())
				alert = db.getAlert(cursor.getInt(0));
			Log.d("G_ID", String.valueOf(G_ID));
			populateForm();
		} else {
			// Set date to todays date
			cal= Calendar.getInstance();
			
		    String cal_for_month = Integer.toString(cal.get(Calendar.MONTH)+1);
		    if (cal.get(Calendar.MONTH)+1 < 10 ) cal_for_month = "0" + cal_for_month;
		    
		    String cal_for_year = Integer.toString(cal.get(Calendar.YEAR));
		    String cal_for_day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		    if (cal.get(Calendar.DAY_OF_MONTH) < 10 ) cal_for_day = "0" + cal_for_day;
		    
			String todayAsString = cal_for_month + "/" + cal_for_day + "/" + cal_for_year;
			goalEnd.setText(todayAsString);
		}
		
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
				// Do Nothing	
			}
		});
		
		// Set a listener for the Type spinner selection.
		goalType.setOnItemSelectedListener(new OnItemSelectedListener() {
					
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// Do Nothing	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do Nothing		
			}
		});
		
		// Initialize the calendar.
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		calendar = (ImageButton) findViewById(R.id.goalButtonCalendar); 
		calendar.setOnClickListener(onDate);
		
				
		// Set the ADD GOAL button to display the ADD Goal form when clicked
		saveGoal = (Button) findViewById(R.id.goalButtonSave);
		saveGoal.setOnClickListener(new View.OnClickListener() {		
								
			@Override
			public void onClick(View v) {
				setProgressBarIndeterminateVisibility(true);
				try{										
					// Transfer edit text to GOAL_TABLE attribute types.
					if(!G_EDIT) goalAccountI = Integer.parseInt(seperated[0]);
					goalNameS = goalName.getText().toString().trim();
					goalTypeS = goalType.getSelectedItem().toString().trim();
					goalEndS = goalEnd.getText().toString().trim();
					goalStartS = goalStart.getText().toString().trim();
					goalDeltaS = goalDelta.getText().toString().trim();
					goalDescriptionS = goalDescription.getText().toString().trim();
					
					goalStatus = String.valueOf(R.drawable.goal_prog);
										
					// Validate inputs
					if(inputsValid()){
						
						if (G_EDIT) {
							// Update the goal record.
							fillGoalObject();
							fillAlertObject();
							db.updateGoal(goal);
							db.updateAlert(alert);
							db.checkGoalStatus();
						} else {
							// Call the add goal method to add the goal to the database
							addGoal();
							alert = new Alert(goalAccountI, "GOAL-" + String.valueOf(G_ID), null, goalEndS);
							db.addAlert(alert);
							db.checkGoalStatus();

						}
						Class<?> clickedClass = Class.forName("com.example.budgetnotebook.GoalView");
						Intent newIntent = new Intent(GoalForm.this, clickedClass);
						
						// Brings us back to the root activity, where exit functions properly.
						newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						
						startActivity(newIntent);		
					}

				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}				
		});
		
	};
	
	// Save all fields to strings
	private void saveFieldsToStrings() {
		// Associate fields in the Goal form (form_goal.xml) to our variables.
		goalName = (EditText) findViewById(R.id.goalEditName);
		goalEnd = (TextView) findViewById(R.id.goalEditEnd);
		goalStart = (TextView) findViewById(R.id.goalEditStart);
		goalDelta = (EditText) findViewById(R.id.goalEditDelta);
		goalDescription = (EditText) findViewById(R.id.goalEditDescription);
	}
	
	// Fill the goal variable with updated information.
	private void fillGoalObject() {
		goal.setId(G_ID);
		goal.setAId(goalAccountI);
		goal.setName(goalNameS);
		goal.setDescription(goalDescriptionS);
		goal.setType(goalTypeS);
		goal.setStartAmount(goalStartS);
		goal.setDeltaAmount(goalDeltaS);
		goal.setEndDate(goalEndS);
		goal.setStatus(goalStatus);
	}
	
	// Fill the alert variable with updated information.
	private void fillAlertObject() {
		alert.setAId(goalAccountI);
		alert.setDueDate(goalEndS);
	}
	
	// Fill the form fields with database data.
	private void populateForm() {	
		//Toast.makeText(this, String.valueOf(goal.getStartAmount()), Toast.LENGTH_LONG).show();
		// Set goal to account ID (subtract 1 because list is 0 based)
		goalAccount.setSelection(S_A_ID, false);
		goalAccountI = goal.getAId();
		// Set goal name text
		goalName.setText(goal.getName());
		// Set goal type spinner
		if (goal.getType().equals("Save DELTA Amount.")) {
			goalType.setSelection(0);
		} else if (goal.getType().equals("Pay off DELTA Amount.")) {
			goalType.setSelection(1);
		} else if (goal.getType().equals("Do not Spend more than DELTA.")) {
			goalType.setSelection(2);
		}
		// Set goal end date text
		goalEnd.setText(goal.getEndDate());
		// Set goal delta amount text
		goalDelta.setText(goal.getDeltaAmount());
		// Set goal start amount text
		goalStart.setText(goal.getStartAmount());
		//Toast.makeText(this, goalStart.getText(), Toast.LENGTH_LONG).show();
		// Set goal description text
		goalDescription.setText(goal.getDescription());
		
		goalStatus = goal.getStatus();
	}


	// Defines onClickListener for the date selection action
	private View.OnClickListener onDate = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {
			showDialog(0);
		}
	};
	
	// Defines calendar dialog pop up
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	// Sets form text to the selected date after DONE is pressed
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
	
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			
			if (selectedMonth < 9) {
				formatMonth = "0" + String.valueOf(selectedMonth + 1);
			} else {
				formatMonth = String.valueOf(selectedMonth + 1);
			}
			
			if (selectedDay < 10) {
				formatDay = "0" + String.valueOf(selectedDay);
			} else {
				formatDay = String.valueOf(selectedDay);
			}
			
			goalEnd.setText(formatMonth + "/" + formatDay + "/" + selectedYear);
		}
	};
	
	// Validate all form input
	public boolean inputsValid(){
		boolean valid = true;
		Account account = db.getAccount(goalAccountI);
		// Goal name not empty
		if(goalNameS.length() == 0){
			goalName.setError("Input is required.");
			valid = false;
		}
		
		// Target goal end date not empty
		if(goalEndS.length() == 0){
			goalEnd.setError("Input is required.");
			valid = false;
		}
		
		// Delta Amount not empty		
		if(goalDeltaS.length() == 0){
			goalDelta.setError("Input is required.");
			valid = false;
		}
		
		// Description not empty
		if(goalDescriptionS.length() == 0){
			goalDescription.setError("Input is required");
			valid = false;
		}
		
		if(goalTypeS.equals("Pay off DELTA Amount.") && !account.getType().equals("CR")) {
			Toast.makeText(this,"Goal type only supported by Credit Card accounts.", Toast.LENGTH_LONG).show();
			setProgressBarIndeterminateVisibility(false);
			valid = false;
		}
		
		return valid;
	}
	
	private void addGoal() {
		db.addGoal(new Goal(goalAccountI, goalNameS, goalDescriptionS, goalTypeS, goalStartS, goalDeltaS, goalEndS, goalStatus));
		G_ID = db.lastRowID("SELECT " + DBHelper.G_ID + " from " + DBHelper.GOAL_TABLE + " order by " + DBHelper.G_ID + " DESC limit 1");
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
		ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.goalTypeArray, android.R.layout.simple_spinner_item);
                      
		goalType.setAdapter(dataAdapter);
	}
	
	// Close the Database on destroy.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		setProgressBarIndeterminateVisibility(false);
		db.close();
		finish();
	};
}
