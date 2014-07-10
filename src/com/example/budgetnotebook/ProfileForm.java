package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ProfileForm extends Activity implements InputValidator {
	Button save_profile;
	EditText profileFirstName;
	EditText profileLastName;
	RadioGroup profileGender;
	RadioButton profileGenderSelection;
	EditText profileBirthday;
	EditText profileCity;
	EditText profileEmail;
	
	String profileFirstNameString;
	String profileLastNameString;
	String profileGenderString;
	String profileBirthdayString;
	String profileCityString;
	String profileEmailString;

	DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_profile);
		
		// Set the SAVE button to commit to the database and then display the main menu when clicked
				save_profile = (Button) findViewById(R.id.save_profile);
				save_profile.setOnClickListener(new View.OnClickListener() {			
					
					@Override
					public void onClick(View v) {
						try{
							//Create Database instance
							DBHelper db1 = new DBHelper(getBaseContext());
							//Save the values entered in the Profile form (form_profile.xml).
							profileFirstName = (EditText)findViewById(R.id.profileFirstName);
							profileLastName = (EditText)findViewById(R.id.profileLastName);
														
							profileGender = (RadioGroup)findViewById(R.id.profileGender);
							profileGenderSelection = (RadioButton)findViewById(profileGender.getCheckedRadioButtonId());
							
							profileBirthday = (EditText)findViewById(R.id.profileBirthday);
							profileCity = (EditText)findViewById(R.id.profileCity);
							profileEmail = (EditText)findViewById(R.id.profileEmail);
							
							// Transfer edit text to PROFILE_TABLE attribute types.							
							profileFirstNameString = profileFirstName.getText().toString().trim();
							profileLastNameString = profileLastName.getText().toString().trim();
							profileGenderString = profileGenderSelection.getText().toString().trim();;
							profileBirthdayString = profileBirthday.getText().toString().trim();;
							profileCityString = profileCity.getText().toString().trim();;
							profileEmailString = profileEmail.getText().toString().trim();;
							
							// Create new profile object using converted strings
							Profile newProfile = new Profile(profileFirstNameString,profileLastNameString,profileGenderString,profileBirthdayString,profileCityString,profileEmailString);
							
							// Write profile to database
							/* if(inputsValid()){
							 if (db.getProfile(1) == null) { // Use addProfile if creating profile for the first time
							db1.addProfile(newProfile);
							Class clickedClass = Class.forName("com.example.budgetnotebook.MainMenu");
							Intent newIntent = new Intent(ProfileForm.this, clickedClass);
							startActivity(newIntent);
							// 
							} else { // Use updateProfile if profile already exists
							db1.updateProfile(newProfile);
							// Finish to return to My Profile layer
							finish();
							}
							}*/
							
							
														
							if(inputsValid()){
								// Call the add profile method to add the profile to the database!
								db1.addProfile(newProfile);
							}
							Class clickedClass = Class.forName("com.example.budgetnotebook.MainMenu");
							Intent newIntent = new Intent(ProfileForm.this, clickedClass);
							startActivity(newIntent);
							} catch(ClassNotFoundException e) {
								e.printStackTrace();
							}
					}				
				});
		
	}
	
	private void addProfile() {
		db.addProfile(new Profile(profileFirstNameString, profileLastNameString, profileGenderString, profileBirthdayString, profileCityString, profileEmailString));
	}
	
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
		
		return valid;
	}
	
	@Override
	protected void onPause() {
		// Kill activity once complete
		super.onPause();
		finish();
	}

}
