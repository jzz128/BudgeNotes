package com.example.budgetnotebook;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
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
	//TextView profileBirthday;
	EditText profileBirthday;
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
		//profileBirthday = (TextView)findViewById(R.id.profileBirthday);
		profileBirthday = (EditText)findViewById(R.id.profileBirthday);
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
			String dayString = Integer.toString(selectedDay);
			String monthString = Integer.toString(selectedMonth+1);
			String yearString = Integer.toString(selectedYear);
			// Add 0 to the front of day or month if less than 10 so that format is correct
			if 	(selectedDay < 10){
				dayString = "0" + Integer.toString(selectedDay);
			} else if (selectedMonth + 1 < 10) {
				monthString = "0" + Integer.toString(selectedMonth+1);
			}
			profileBirthday.setText(monthString + "/" + dayString + "/" + yearString);
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
		
		// Name cannot contain numbers
		boolean hasNonAlphaFirst = profileFirstNameString.matches("^.*[^a-zA-Z].*$");
		if(hasNonAlphaFirst){
			profileFirstName.setError(InputValidator.ALPHA_REQUIRED);
			valid = false;
		}
				
		// Profile last name is not empty
		if(profileLastNameString.length() == 0){
			profileLastName.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// Name cannot contain numbers
		boolean hasNonAlphaLast = profileLastNameString.matches("^.*[^a-zA-Z].*$");
		if(hasNonAlphaLast){
			profileLastName.setError(InputValidator.ALPHA_REQUIRED);
			valid = false;
		}
		
		// Profile gender is not empty
		if(profileGenderString.length() == 0){
			profileGenderSelection.setError(InputValidator.INPUT_REQUIRED);
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
		
		// Get current date
		Calendar today = Calendar.getInstance(); 
	    // Set calendar to use for birth date
		Calendar birthDate = Calendar.getInstance();
	    // Split birthday string in to day, month and year
		String birthdaySplit[] = profileBirthdayString.split("/");
	    String day = birthdaySplit[1].trim();
	    Log.d("day",day);
	    String month = birthdaySplit[0].trim();
	    Log.d("month",month);
	    String year = birthdaySplit[2].trim();
	    Log.d("year",year); 
	    // Save each value as an integer
	    int yearInt = Integer.parseInt(year);
	    int monthInt = Integer.parseInt(month);
	    int dayInt = Integer.parseInt(day);

	    // Set birth date calendar date to birth date
	    birthDate.set(yearInt,monthInt,dayInt);
	    Log.d("date to string",birthDate.toString());
	    // Check if birth date is in the future
	    if (birthDate.after(today)) {
	    	profileBirthday.setError(InputValidator.FUTURE_BDAY);
	    	valid = false;
	    }
	    
	    // Validate date format (MM/DD/YYYY)
	    Log.d("profileBirthdayString",profileBirthdayString); 
	    if (!validateDate(profileBirthdayString.trim())) {
	    	profileBirthday.setError(InputValidator.INVALID_DATE);
	    	valid = false;
	    }
	    
		// Profile city is not empty
		if(profileCityString.length() == 0){
			profileCity.setError(InputValidator.INPUT_REQUIRED);
			valid = false;
		}
		
		// City cannot contain numbers
		boolean hasNonAlphaCity = profileCityString.matches("^[\\p{L} .'-]+$");
		if(!hasNonAlphaCity){
			profileCity.setError(InputValidator.ALPHA_REQUIRED);
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
	
	
	// Date Validation
	private Pattern pattern;
	private Matcher matcher;
	
	// Regular expression to verify date is in MM/DD/YYYY format
	private static final String DATE_PATTERN = 
			"(0[1-9]|1[012])/0?(0[1-9]|[12][0-9]|3[01])/(19|20)\\d\\d";
	 
	// Checks date and returns false if not a valid format    
	public boolean validateDate(final String date){
		pattern = Pattern.compile(DATE_PATTERN);	  
		matcher = pattern.matcher(date);
	 
	     if(matcher.matches()){
	 
		 matcher.reset();
		 // Validate all fields of the month, day and year
		 if(matcher.find()){
			 // Assign strings to groups from the regular expression
			 String month = matcher.group(1);
	         String day = matcher.group(2);		
		     int year = Integer.parseInt(matcher.group(3));
		     // Verify values against real valid calendar values
		     if (day.equals("31") && 
			  (month.equals("4") || month .equals("6") || month.equals("9") ||
	                  month.equals("11") || month.equals("04") || month .equals("06") ||
	                  month.equals("09"))) {
				return false; // only 1,3,5,7,8,10,12 has 31 days
		     } else if (month.equals("2") || month.equals("02")) {
	                  //leap year
			  if(year % 4==0){
				  if(day.equals("30") || day.equals("31")){
					  return false;
				  }else{
					  return true;
				  }
			  }else{
			         if(day.equals("29")||day.equals("30")||day.equals("31")){
					  return false;
			         }else{
					  return true;
				  }
			  }
		      }else{				 
			return true;				 
		      }
		   }else{
	    	      return false;
		   }		  
	     }else{
		  return false;
	     }			    
	   }
	  
}
