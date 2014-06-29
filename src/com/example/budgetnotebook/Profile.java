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
}
