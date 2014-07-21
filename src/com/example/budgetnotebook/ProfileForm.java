package com.example.budgetnotebook;

import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ProfileForm extends Activity implements InputValidator {
	Button save_profile;
	EditText profileFirstName;
	EditText profileLastName;
	RadioGroup profileGender;
	RadioButton profileGenderSelection;
	TextView profileBirthday;
	ImageButton calendar;
	EditText profileCity;
	EditText profileEmail;
	
	private Calendar cal;
	private int day, month, year;
	
	String profileFirstNameString;
	String profileLastNameString;
	String profileGenderString;
	String profileBirthdayString;
	String profileCityString;
	String profileEmailString;

	boolean profile_exists;
	DBHelper db;
	Profile profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_profile);
		
		//Create Database instance
		db = new DBHelper(getBaseContext());
		//Save the values entered in the Profile form (form_profile.xml).
		saveFieldsToStrings();

		// Initialize the calendar.
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		calendar = (ImageButton) findViewById(R.id.profileButtonCalendar);
		calendar.setOnClickListener(onDate);
		
		// Check if a profile exists and write result to local variable.
		if (db.checkProfileExists() == 0) {
			profile_exists = false;
			// Set the gender selection button to default to MALE
			profileGenderSelection = (RadioButton)findViewById(R.id.profileMale);
			profileGenderSelection.setChecked(true);
			// Set birthday default to 01/01/2000
			profileBirthday.setText("01/01/2000");
			
		} else {
			profile_exists = true;
			// Get profile from the database
			profile = db.getProfile(1);	
			// Display database information in the form fields
			populateForm();
		}
			
		// Set the SAVE button to commit to the database and then display the main menu when clicked
		save_profile = (Button) findViewById(R.id.save_profile);
		save_profile.setOnClickListener(new View.OnClickListener() {				
		
					@Override
					public void onClick(View v) {
						try{
							profileGenderSelection = (RadioButton)findViewById(profileGender.getCheckedRadioButtonId());
							// Transfer edit text to PROFILE_TABLE attribute types.	
							fillProfileVariables();
							if(inputsValid()){
								if (profile_exists) {
									// Populate profile object
									fillProfileObject();
									// Write to database
									db.updateProfile(profile);
									// Finish activity to return to main menu
									finish();
								} else {
									// Write to profile to database									
									addProfile();
									// Display Main Menu after profile is created
									Class<?> clickedClass = Class.forName("com.example.budgetnotebook.MainMenu");
									Intent newIntent = new Intent(ProfileForm.this, clickedClass);
									startActivity(newIntent);
									
									}
								}
							} catch(ClassNotFoundException e) {
								e.printStackTrace();
							}
							
					}				
				});
		
	}
	
	// Save all fields to strings
	private void saveFieldsToStrings() {
		//Save the values entered in the Profile form (form_profile.xml).
		profileFirstName = (EditText)findViewById(R.id.profileFirstName);
		profileLastName = (EditText)findViewById(R.id.profileLastName);							
		profileGender = (RadioGroup)findViewById(R.id.profileGender);	
		profileBirthday = (TextView)findViewById(R.id.profileBirthday);
		profileCity = (EditText)findViewById(R.id.profileCity);
		profileEmail = (EditText)findViewById(R.id.profileEmail);
	}
	
	// Populate profile object
	private void fillProfileObject() {
		profile.setFirstName(profileFirstNameString);
		profile.setLastName(profileLastNameString);
		profile.setGender(profileGenderString);
		profile.setBirthday(profileBirthdayString);
		profile.setCity(profileCityString);
		profile.setEmail(profileEmailString);
	}
	
	// Display database information in the form fields
	private void populateForm() {
		profileFirstName.setText(profile.getFirstName());
		profileLastName.setText(profile.getLastName());
		
		// Set button to be male id if Male, female if female.
		if (profile.getGender().equals("F")){
			profileGenderSelection = (RadioButton)findViewById(R.id.profileFemale);
			profileGenderSelection.setChecked(true);
		} else {
			profileGenderSelection = (RadioButton)findViewById(R.id.profileMale);
			profileGenderSelection.setChecked(true);
		}
	
		// ---------------------------------------------------------------------
		
		profileBirthday.setText(profile.getBirthday());
		profileCity.setText(profile.getCity());
		profileEmail.setText(profile.getEmail());
	}
		
	// Transfer edit text to PROFILE_TABLE attribute types.	
	private void fillProfileVariables() {								
		profileFirstNameString = profileFirstName.getText().toString().trim();
		profileLastNameString = profileLastName.getText().toString().trim();
		profileGenderString = profileGenderSelection.getText().toString().trim();
		profileBirthdayString = profileBirthday.getText().toString().trim();
		profileCityString = profileCity.getText().toString().trim();
		profileEmailString = profileEmail.getText().toString().trim();
	}
	
	// Adds current profile object to the database. Note: Creates a new row in the DB.
	private void addProfile() {
		db.addProfile(new Profile(profileFirstNameString, profileLastNameString, profileGenderString, profileBirthdayString, profileCityString, profileEmailString));
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
					profileBirthday.setText((selectedMonth + 1) + " / " + selectedDay + " / " + selectedYear);
}
};

	// Validates inputs on the profile form
	@Override
	public boolean inputsValid() {
		boolean valid = true;
		
		// Profile first name is not empty
		if(profileFirstNameString.length() == 0){
			profileFirstName.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Profile last name is not empty
		if(profileLastNameString.length() == 0){
			profileLastName.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Profile gender is not empty
		if(profileGenderString.length() == 0){
			profileGenderSelection.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Profile birthday is not empty
		if(profileBirthdayString.length() == 0){
			profileBirthday.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Profile city is not empty
		if(profileCityString.length() == 0){
			profileCity.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Profile email is not empty
		if(profileEmailString.length() == 0){
			profileEmail.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Profile email contains @ symbol
		if(!profileEmailString.contains("@")){
			// Change to invalid input?
			profileEmail.setError(InputValidator.EMAIL_REQUIRED);
			valid = false;
		}
		
		// Profile email contains . symbol
		if(!profileEmailString.contains(".")){
			profileEmail.setError(InputValidator.EMAIL_REQUIRED);
			valid = false;
		}
		
		return valid;
	}

	// Defines what to do when the activity pauses
	@Override
	protected void onPause() {
		// Kill activity once complete
		super.onPause();
		finish();
	}

}
