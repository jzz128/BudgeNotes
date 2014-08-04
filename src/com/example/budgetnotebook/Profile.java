/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * Profile.java
 * 
 * DBHelper adapter used to to modify the profile table in the DB.
 * 
 **/

package com.example.budgetnotebook;

public class Profile {

	// Class variables
	private int _id;
	private String first_name;
	private String last_name;
	private String gender;
	private String birthday;
	private String city;
	private String email;
	
	public Profile(){}
	
	// Create a new Profile object using parameters
	public Profile(String first_name, String last_name, String gender, String birthday, String city, String email) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.gender = gender;
		this.birthday = birthday;
		this.city = city;
		this.email = email;
	}
	
	// Return the profile object as a string
	@Override
	public String toString() {
		return "Profile [id=" + _id + ", first_name=" + first_name + ", last_name=" + last_name + ", gender=" + gender + ", birthday=" + birthday + ", city=" + city + ", email=" + email +"]";
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
