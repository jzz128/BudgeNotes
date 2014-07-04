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
	boolean bAlert = false;
	public static AlertHelper alert_helper;
	public static DBHelper db_helper;
	boolean finalDB = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		account_sp = (NewSpinner) findViewById(R.id.account_sp);
		goal_sp = (NewSpinner) findViewById(R.id.goal_sp);

		userinfo_button = (Button) findViewById(R.id.ui_b);
		recommends_buttton = (Button) findViewById(R.id.rec_b);
		alert_button = (ImageButton) findViewById(R.id.alert_b);
		if (!finalDB) {
			a_helper = new AccountHelper(this);
			g_helper = new GoalHelper(this);
		} else {
			db_helper = new DBHelper(this);
		}

		populateItemOnAccounts();
		populateItemOnGoals();
		account_sp.setOnItemSelectedListener(this);
		account_sp.setOnItemSelectedEvenIfUnchangedListener(this);
		goal_sp.setOnItemSelectedListener(this);
		goal_sp.setOnItemSelectedEvenIfUnchangedListener(this);
		registerForContextMenu(account_sp);
		registerForContextMenu(goal_sp);

		userinfo_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				Intent intent = new Intent(MainActivity.this,
						ProfileActivity.class);
				startActivity(intent);
			}
		});

		recommends_buttton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				Intent intent = new Intent(MainActivity.this,
						RecommendActivity.class);
				startActivity(intent);
			}
		});

		alert_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				Intent intent = new Intent(MainActivity.this,
						AlertActivity.class);
				startActivity(intent);

			}
		});

		alert_helper = new AlertHelper(this);

		Cursor c = alert_helper.getById();

		if (c.moveToFirst()) {
			alert_button.setClickable(true);
			alert_button.setEnabled(true);
			alert_button.setImageResource(R.drawable.alert);
			bAlert = true;
			Log.d("AlertActivity", "bAlert=true");
		} else {
			alert_button.setClickable(false);
			alert_button.setEnabled(false);
			alert_button.setImageResource(R.drawable.alert_off);
			bAlert = false;
			Log.d("MainActivity", "bAlert=false");
			/*
			 * temporally add sample entry for testing, remove the following
			 * later
			 */
			alert_helper.insert("Sample Alerts!!!");
			/* done */
		}
		c.close();
		alert_button.setClickable(false);
		alert_button.setEnabled(false);
		alert_button.setImageResource(R.drawable.alert_off);
	}

	// add items into spinner dynamically
	public void populateItemOnAccounts() {
		List<String> list = new ArrayList<String>();
		if (finalDB) {
			int count, i = 0;
			Account a = null;
			List<Account> a_list = db_helper.getAllAccounts();
			count = a_list.size();
			while (i < count) {
				a = a_list.get(i);
				list.add(a.getName());
				i++;
			}
		} else {

			model = a_helper.getAll();
			startManagingCursor(model);
			while (model.moveToNext()) {
				list.add(a_helper.getAccountName(model));
			}
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		account_sp.setAdapter(dataAdapter);
		account_sp_started = false;
	}

	public void populateItemOnGoals() {
		List<String> list = new ArrayList<String>();
		if (finalDB) {
			int count, i = 0;
			Goal g = null;
			List<Goal> g_list;
			g_list = db_helper.getAllGoals();
			count = g_list.size();
			while (i < count) {
				g = g_list.get(i);
				list.add(g.getName());
				i++;
			}
		} else {
			model = g_helper.getAll();
			startManagingCursor(model);
			while (model.moveToNext()) {
				list.add(g_helper.getGoalName(model));
			}
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		goal_sp.setAdapter(dataAdapter);
		goal_sp_started = false;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Log.d("TEST", "onItemSelected parent = " + parent.getId());
		if (parent.getId() == R.id.account_sp) {
			if (account_sp_started == false) {
				account_sp_started = true;
				return;
			}

			Intent i = new Intent(MainActivity.this, MenuActivity.class);
			i.putExtra(ID_EXTRA, String.valueOf(parent.getItemAtPosition(pos)));
			startActivity(i);
		} else if (parent.getId() == R.id.goal_sp) {
			if (goal_sp_started == false) {
				goal_sp_started = true;
				return;
			}

			Intent i = new Intent(MainActivity.this, GoalActivity.class);
			i.putExtra(ID_EXTRA, String.valueOf(parent.getItemAtPosition(pos)));
			startActivity(i);
		}

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
			startActivity(new Intent(MainActivity.this, GoalActivity.class));
			return (true);
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
		if (bAlert) {
			alert_button.setClickable(true);
			alert_button.setEnabled(true);
			alert_button.setImageResource(R.drawable.alert);
		}
	}

}
