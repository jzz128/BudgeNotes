package com.example.budgetnotebook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Profile extends Activity {
	Button save_profile;
	boolean profile_exists = false; // This is being used temporarily until the profile functionality is created
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_profile);
		
		// Set the SAVE commit to the database and then display the main menu when clicked
				save_profile = (Button) findViewById(R.id.save_profile);
				save_profile.setOnClickListener(new View.OnClickListener() {			
					
					@Override
					public void onClick(View v) {
						try{
							Class clickedClass = Class.forName("com.example.budgetnotebook.MainMenu");
							Intent newIntent = new Intent(Profile.this, clickedClass);
							startActivity(newIntent);
							} catch(ClassNotFoundException e) {
								e.printStackTrace();
							}
					}				
				});
		
	}

	@Override
	protected void onPause() {
		// Kill create profile activity so it cannot be accessed via back button
		super.onPause();
		finish();
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
