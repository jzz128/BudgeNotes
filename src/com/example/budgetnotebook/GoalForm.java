package com.example.budgetnotebook;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class GoalForm extends Activity implements InputValidator{

	Button saveGoal;
	DBHelper db;
	Spinner goalAccount;
	String[] seperated;
	EditText goalName;
	Spinner goalType;
	EditText goalEnd;
	ImageButton calendar;
	EditText goalStart;
	EditText goalDelta;
	EditText goalDescription;
	
	private Calendar cal;
	private int day, month, year;
	
	private int goalAccountI;
	private String goalNameS;
	private String goalTypeS;
	private String goalEndS;
	private String goalStartS;
	private String goalDeltaS;
	private String goalDescriptionS;
	
	int G_ID;
	boolean G_EDIT;
	Goal goal;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_goal);
		
		// Access the database.
		db = new DBHelper(getBaseContext());
		
		// Get the id of the goal from the spinner on the goal view.
		Intent intent = getIntent();
		G_ID = intent.getIntExtra("G_ID",0);
		G_EDIT = intent.getBooleanExtra("G_EDIT", false);
				
		// Initialize the Spinners.
		goalAccount = (Spinner) findViewById(R.id.goalEditAccountSpinner);
		goalType = (Spinner) findViewById(R.id.goalEditTypeSpinner);
		
		// Associate fields in the Goal form (form_goal.xml) to our variables.
		saveFieldsToStrings();
		
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
		
		// Initialize the calendar.
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		calendar = (ImageButton) findViewById(R.id.goalButtonCalendar); 
		calendar.setOnClickListener(onDate);
		
		// Auto fill the form if this is an edit.
		if (G_EDIT) {
			goal = db.getGoal(1);
			populateForm();
		} else {
			//Do Nothing.
		}
						
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
					
					// Validate inputs
					if(inputsValid()){
						
						if (G_EDIT) {
							// Update the goal record.
							fillGoalObject();
							db.updateGoal(goal);
						} else {
							// Call the add goal method to add the goal to the database
							addGoal();
						}
					}

												
						Class clickedClass = Class.forName("com.example.budgetnotebook.Goal");
						Intent newIntent = new Intent(GoalForm.this, clickedClass);
						
						// Brings us back to the root activity, where exit functions properly.
						newIntent.setFlags(newIntent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(newIntent);						
					
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
			goalEnd = (EditText) findViewById(R.id.goalEditEnd);
			goalStart = (EditText) findViewById(R.id.goalEditStart);
			goalDelta = (EditText) findViewById(R.id.goalEditDelta);
			goalDescription = (EditText) findViewById(R.id.goalEditDescription);
		}
	
		// Fill the goal variable with updated information.
		private void fillGoalObject() {
			//goal.setId(G_ID);
			goal.setId(1);
			goal.setAId(goalAccountI);
			goal.setName(goalNameS);
			goal.setDescription(goalDescriptionS);
			goal.setType(goalTypeS);
			goal.setStartAmount(goalStartS);
			goal.setDeltaAmount(goalDeltaS);
			goal.setEndDate(goalEndS);
		}
		
		// Fill the form fields with database data.
		private void populateForm() {			
			// Set goal to account ID (subtract 1 because list is 0 based)
			goalAccount.setSelection(goal.getAId()-1);
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
			// Set goal description text
			goalDescription.setText(goal.getDescription());
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
				goalEnd.setText((selectedMonth + 1) + " / " + selectedDay + " / " + selectedYear);
	}
	};
	
	public boolean inputsValid(){
		boolean valid = true;
		
		// Goal name not empty
		if(goalNameS.length() == 0){
			goalName.setError("Input is required.");
			valid = false;
		}
		
		// Target goal end date not empty and valid date
		if(goalEndS.length() == 0){
			goalEnd.setError("Input is required.");
			valid = false;
		}
		else{
			// TODO validate to an agreed upon format 	 	
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
		
		return valid;
	}
	
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
	
	// Close the Database on destroy.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		finish();
	};
}
