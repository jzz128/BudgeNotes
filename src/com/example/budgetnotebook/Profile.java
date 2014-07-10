package com.example.budgetnotebook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends Activity {
	Button edit_profile;
	DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_profile);
		/*
		// DATABASE OPEN AND TEST USING TOAST ---------------------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
		
		//Create Database instance
			DBHelper db = new DBHelper(getBaseContext());
						
		// Testing Goal with Toast
		db.toastProfile(getBaseContext());
						
		// Close db
		db.close();*/
		
		// --------------------------------------------------------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
		//Display Profile Information - Need to get from Database
		String first_name = "Ryan";
		String last_name = "Donovan";
		String gender = "M";
		String birthday = "01/18/1984";
		String city = "Philadelphia";
		String email = "rmd259@psu.edu";
		
		/*Profile profileInfo = db.getProfile(1);
		String first_name_db = profileInfo.getFirstName();
		String last_name = profileInfo.getLastName();
		String gender = profileInfo.getGender();
		String birthday = profileInfo.getBirthday();
		String city = profileInfo.getCity();
		String email = profileInfo.getEmail();*/
		
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
							Class clickedClass = Class.forName("com.example.budgetnotebook.ProfileForm");
							Intent newIntent = new Intent(Profile.this, clickedClass);
							startActivity(newIntent);
							} catch(ClassNotFoundException e) {
								e.printStackTrace();
							}
					}				
				});
		
	}
	@Override
	public String toString() {
		return "Profile [id=" + _id + ", first_name=" + first_name + ", last_name=" + last_name + ", gender=" + gender + ", birthday=" + birthday + ", city=" + city + ", email=" + email +"]";
	}
	
	private int _id;
	private String first_name;
	private String last_name;
	private String gender;
	private String birthday;
	private String city;
	private String email;
	
	public Profile(){}
	
	public Profile(String first_name, String last_name, String gender, String birthday, String city, String email) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.gender = gender;
		this.birthday = birthday;
		this.city = city;
		this.email = email;
	}
	
	//Getters --------------------------------------------------------------------
		public int getId() {
			return _id;
		}
				
		public String getFirstName() {
			return first_name;
		}
		
		public String getLastName() {
			return last_name;
		}
		
		public String getGender() {
			return gender;
		}
		
		public String getBirthday() {
			return birthday;
		}
		
		public String getCity() {
			return city;
		}
		
		public String getEmail() {
			return email;
		}
		
		//Setters --------------------------------------------------------------------
		public void setId(int id) {
			this._id = id;
		}
				
		public void setFirstName(String first_name) {
			this.first_name = first_name;
		}
		
		public void setLastName(String last_name) {
			this.last_name = last_name;
		}
		
		public void setGender(String gender) {
			this.gender = gender;
		}
		
		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		
		public void setCity(String city) {
			this.city = city;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
}
