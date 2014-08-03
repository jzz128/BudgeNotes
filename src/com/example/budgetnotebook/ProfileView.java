package com.example.budgetnotebook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileView extends Activity {
	Button edit_profile;
	DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_profile);
		
		//Create Database instance
		DBHelper db = new DBHelper(getBaseContext());
		//Display Profile Information from Database
		Profile profileInfo = db.getProfile(1);
		String first_name = profileInfo.getFirstName();
		String last_name = profileInfo.getLastName();
		String gender = profileInfo.getGender();
		String birthday = profileInfo.getBirthday();
		String city = profileInfo.getCity();
		String email = profileInfo.getEmail();
		
		// Set First Name
		TextView tv_first_name = (TextView)findViewById(R.id.profileFirstName);
		tv_first_name.setText(first_name);
		// Set Last Name
		TextView tv_last_name = (TextView)findViewById(R.id.profileLastName);
		tv_last_name.setText(last_name);
		// Set Gender
		TextView tv_gender = (TextView)findViewById(R.id.profileGender);
		tv_gender.setText(gender);
		// Set Birthday
		TextView tv_birthday = (TextView)findViewById(R.id.profileBirthday);
		tv_birthday.setText(birthday);
		// Set City
		TextView tv_city = (TextView)findViewById(R.id.profileCity);
		tv_city.setText(city);
		// Set Email
		TextView tv_email = (TextView)findViewById(R.id.profileEmail);
		tv_email.setText(email);
		
		
		// Set the EDIT BUTTON to display the profile form when clicked
		edit_profile = (Button) findViewById(R.id.editProfile);
		edit_profile.setOnClickListener(new View.OnClickListener() {			
			
			@Override
			public void onClick(View v) {
				try{
					// Finish the Profile activity and start the Profile Form activity
					finish();
					Class<?> clickedClass = Class.forName("com.example.budgetnotebook.ProfileForm");
					Intent newIntent = new Intent(ProfileView.this, clickedClass);
					startActivity(newIntent);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
			}				
		});		
	}	
}