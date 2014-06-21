package com.example.budgetnotebook;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Account extends TabActivity {
	private EditText accountName;
	private EditText accountNumber;
	private RadioGroup accountTypes;
	private EditText beginningBalance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_account);
		
		accountName = (EditText)findViewById(R.id.accountName);
		accountNumber = (EditText)findViewById(R.id.accountNumber);
		accountTypes = (RadioGroup)findViewById(R.id.types);
		beginningBalance = (EditText)findViewById(R.id.beginningBalance);
		
		Button save = (Button)findViewById(R.id.save);
		save.setOnClickListener(onSave);
	}
	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			// TODO commit to db
		}
	};
}

