package com.example.budgetnotebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnItemSelectedListener {
	Button alert_button, recommends_buttton;
	Button account_add_button, goal_add_button, userinfo_button;
	Spinner account_sp, goal_sp;
	public final static String ID_EXTRA = "apt.tutorial.ACCOUNT_ID";
	private boolean account_sp_started, goal_sp_started;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		account_sp = (Spinner) findViewById(R.id.account_sp);
		goal_sp = (Spinner) findViewById(R.id.goal_sp);
		account_add_button = (Button) findViewById(R.id.account_add_b);
		goal_add_button = (Button) findViewById(R.id.goal_add_b);
		userinfo_button = (Button) findViewById(R.id.ui_b);
		recommends_buttton = (Button) findViewById(R.id.rec_b);
		alert_button = (Button) findViewById(R.id.alert_b);

		populateItemOnAccounts();
		populateItemOnGoals();
		account_sp.setOnItemSelectedListener(this);
		goal_sp.setOnItemSelectedListener(this);

		account_add_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
			}
		});

		goal_add_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
			}
		});
		userinfo_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
			}
		});
		recommends_buttton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
			}
		});
	}

	// add items into spinner dynamically
	public void populateItemOnAccounts() {

		List<String> list = new ArrayList<String>();
		list.add("Account 1");
		list.add("Account 2");
		list.add("Account 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		account_sp.setAdapter(dataAdapter);
		account_sp_started = false;
	}

	public void populateItemOnGoals() {

		List<String> list = new ArrayList<String>();
		list.add("Goal 1");
		list.add("Goal 2");
		list.add("Goal 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		goal_sp.setAdapter(dataAdapter);
		goal_sp_started = false;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Log.v("TEST", "onItemSelected parent = " + parent.getId());
		if (parent.getId() == R.id.account_sp) {
			if(account_sp_started == false) {
				account_sp_started = true;
				return;
			}
		//	startActivity(new Intent(MainActivity.this, MenuActivity.class));
			Intent i = new Intent(MainActivity.this, MenuActivity.class);
			i.putExtra(ID_EXTRA, String.valueOf(parent.getItemAtPosition(pos)));
			startActivity(i);
		}
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
}
