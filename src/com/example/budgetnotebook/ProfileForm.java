package com.example.budgetnotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
		profileFirstName = (EditText)findViewById(R.id.profileFirstName);
		profileLastName = (EditText)findViewById(R.id.profileLastName);
									
		profileGender = (RadioGroup)findViewById(R.id.profileGender);
				
		profileBirthday = (EditText)findViewById(R.id.profileBirthday);
		profileCity = (EditText)findViewById(R.id.profileCity);
		profileEmail = (EditText)findViewById(R.id.profileEmail);
		
		// Check if a profile exists.
		if (db.checkProfileExists() == 0) {
			profile_exists = false;
		} else {
			profile_exists = true;
			profile = db.getProfile(1);	
			populateForm();
		}
		
		// Set the SAVE button to commit to the database and then display the main menu when clicked
		save_profile = (Button) findViewById(R.id.save_profile);
		save_profile.setOnClickListener(new View.OnClickListener() {				
		
					@Override
					public void onClick(View v) {
						try{
							profileGenderSelection = (RadioButton)findViewById(profileGender.getCheckedRadioButtonId());
							fillProfileVariables();
							
							if (profile_exists) {
								fillProfileObject();
								db.updateProfile(profile);
								finish();
							} else {
								profile = new Profile(profileFirstNameString,profileLastNameString,profileGenderString,profileBirthdayString,profileCityString,profileEmailString);	
								db.addProfile(profile);
								
							}
							// Display Main Menu after profile is created or edited
							Class clickedClass = Class.forName("com.example.budgetnotebook.MainMenu");
							//Intent newIntent = new Intent(ProfileForm.this, clickedClass);
							//startActivity(newIntent);
							} catch(ClassNotFoundException e) {
								e.printStackTrace();
							}
					}				
				});
		
	}
	
	private void fillProfileObject() {
		profile.setFirstName(profileFirstNameString);
		profile.setLastName(profileLastNameString);
		profile.setGender(profileGenderString);
		profile.setBirthday(profileBirthdayString);
		profile.setCity(profileCityString);
		profile.setEmail(profileEmailString);
	}
	
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
	
	private void fillProfileVariables() {		
		// Transfer edit text to PROFILE_TABLE attribute types.							
		profileFirstNameString = profileFirstName.getText().toString().trim();
		profileLastNameString = profileLastName.getText().toString().trim();
		profileGenderString = profileGenderSelection.getText().toString().trim();
		profileBirthdayString = profileBirthday.getText().toString().trim();
		profileCityString = profileCity.getText().toString().trim();
		profileEmailString = profileEmail.getText().toString().trim();
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
