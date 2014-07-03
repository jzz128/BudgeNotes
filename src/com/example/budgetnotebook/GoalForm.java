package com.example.budgetnotebook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class GoalForm extends Activity {
	// Eventually these will map to the TextViews on form_goal layout to create a new goal record.
	Button saveGaol;
	EditText goalAccount;
	EditText goalName;
	EditText goalType;
	EditText goalEnd;
	EditText goalStart;
	EditText goalDelta;
	EditText goalDescription;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_goal);
	};
}
