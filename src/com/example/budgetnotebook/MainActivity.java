package com.example.budgetnotebook;

import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.budgetnotebook.MenuActivity.RecordAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnItemSelectedListener {
	ImageButton alert_button;
	Button recommends_buttton;
	Button userinfo_button;
	NewSpinner account_sp, goal_sp;
	public final static String ID_EXTRA = "apt.tutorial.ACCOUNT_ID";
	private boolean account_sp_started, goal_sp_started;
	Cursor model = null;
	AccountHelper a_helper = null;
	GoalHelper g_helper = null;
	public static boolean account_updated = false;
	public static boolean goal_updated = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		account_sp = (NewSpinner) findViewById(R.id.account_sp);
		goal_sp = (NewSpinner) findViewById(R.id.goal_sp);
		// account_add_button = (Button) findViewById(R.id.account_add_b);
		// goal_add_button = (Button) findViewById(R.id.goal_add_b);
		userinfo_button = (Button) findViewById(R.id.ui_b);
		recommends_buttton = (Button) findViewById(R.id.rec_b);
		alert_button = (ImageButton) findViewById(R.id.alert_b);
		a_helper = new AccountHelper(this);
		g_helper = new GoalHelper(this);

		populateItemOnAccounts();
		populateItemOnGoals();
		account_sp.setOnItemSelectedListener(this);
		account_sp.setOnItemSelectedEvenIfUnchangedListener(this);
		goal_sp.setOnItemSelectedListener(this);
		goal_sp.setOnItemSelectedEvenIfUnchangedListener(this);
		registerForContextMenu(account_sp);
		registerForContextMenu(goal_sp);

		// account_add_button.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// Do something in response to button click
		// }
		// });

		// goal_add_button.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// Do something in response to button click
		// }
		// });
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
		
		alert_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
			//	Intent intent = new Intent(MainActivity.this,NextActivity.class);
	         //    startActivity(intent, 0);

			}
		});
		
		alert_button.setClickable(false);
		alert_button.setEnabled(false);
		alert_button.setImageResource(R.drawable.alert_off);
	}

	// add items into spinner dynamically
	public void populateItemOnAccounts() {
		model = a_helper.getAll();
		startManagingCursor(model);
		List<String> list = new ArrayList<String>();
		while (model.moveToNext()) {
			list.add(a_helper.getAccountName(model));
		}

		// List<String> list = new ArrayList<String>();
		// list.add("Account 1");
		// list.add("Account 2");
		// list.add("Account 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		account_sp.setAdapter(dataAdapter);
		account_sp_started = false;
	}

	public void populateItemOnGoals() {
		model = g_helper.getAll();
		startManagingCursor(model);
		List<String> list = new ArrayList<String>();
		while (model.moveToNext()) {
			list.add(g_helper.getGoalName(model));
		}
		// List<String> list = new ArrayList<String>();
		// list.add("Goal 1");
		// list.add("Goal 2");
		// list.add("Goal 3");
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
			if (account_sp_started == false) {
				account_sp_started = true;
				return;
			}
			// startActivity(new Intent(MainActivity.this, MenuActivity.class));
			Intent i = new Intent(MainActivity.this, MenuActivity.class);
			i.putExtra(ID_EXTRA, String.valueOf(parent.getItemAtPosition(pos)));
			startActivity(i);
		} else if (parent.getId() == R.id.goal_sp) {
			if (goal_sp_started == false) {
				goal_sp_started = true;
				return;
			}
			// startActivity(new Intent(MainActivity.this, MenuActivity.class));
			Intent i = new Intent(MainActivity.this, GoalActivity.class);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v == account_sp) {
			new MenuInflater(this).inflate(R.menu.account_spinner_option, menu);
		}

		else if (v == goal_sp) {
			new MenuInflater(this).inflate(R.menu.goal_spinner_option, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.account_add) {
			startActivity(new Intent(MainActivity.this, AccountActivity.class));
			return (true);
		} else if (item.getItemId() == R.id.goal_add) {
			// startWork();
			// setProgressBarVisibility(true);
		}
		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		a_helper.close();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		if (account_updated) {
			populateItemOnAccounts();
			account_updated = false;
		}
		if (goal_updated) {
			populateItemOnGoals();
			goal_updated = false;
		}
	}


}
