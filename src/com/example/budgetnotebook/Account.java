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
	private TextView accountIdField;
	private EditText accountName;
	private EditText accountNumber;
	private RadioGroup accountTypes;
	private EditText beginningBalance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_account);
		
		accountIdField = (TextView)findViewById(R.id.accountId);
		accountName = (EditText)findViewById(R.id.name);
		accountNumber = (EditText)findByViewId(R.id.accountNumber);
		accountTypes = (RadioGroup)findByViewId(R.id.types);
		beginningBalance = (EditText)findByViewId(R.id.beginningBalance);
		
		Button save = (Button)findByViewId(R.id.save);
		save.setOnClickListener(onSave);
	}
	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			// TODO commit to db
		}
	};
}

