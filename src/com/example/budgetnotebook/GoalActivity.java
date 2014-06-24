package com.example.budgetnotebook;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class GoalActivity extends Activity {
	EditText g_name = null;
	EditText g_amount = null;
	EditText g_notes = null;
	EditText g_date = null;
	GoalHelper helper = null;
	String goalId = null;
	private Calendar cal;
	private int day;
	private int month;
	private int year;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal);
		helper = new GoalHelper(this);
		g_name = (EditText) findViewById(R.id.g_name);
		g_amount = (EditText) findViewById(R.id.g_amount);
		g_notes = (EditText) findViewById(R.id.g_notes);
		g_date = (EditText) findViewById(R.id.g_date);
		ImageButton calendar = (ImageButton) findViewById(R.id.g_calendar);
		Button save = (Button) findViewById(R.id.g_save);
		Log.d("GoalActivity", "Button save=" + save);
		save.setOnClickListener(onSave);

		goalId = getIntent().getStringExtra(MainActivity.ID_EXTRA);
		Log.v("GoalActivity", "goalId = " +goalId);
		if (goalId != null) {
			load();
		}

		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		calendar.setOnClickListener(onDate);

	}

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			if (goalId ==null) {
			helper.insert(g_name.getText().toString(), g_amount.getText()
					.toString(), g_date.getText().toString(), g_notes.getText()
					.toString());
			MainActivity.goal_updated = true;
			} else {
				helper.update(g_name.getText().toString(), g_amount.getText()
						.toString(), g_date.getText().toString(), g_notes.getText()
						.toString());
			}
			finish();
		}
	};


	private View.OnClickListener onDate = new View.OnClickListener() {
		public void onClick(View v) {
			showDialog(0);
		}
	};

	private void load() {
		Cursor c = helper.getById(goalId);
		c.moveToFirst();
		g_name.setText(helper.getGoalName(c));
		g_amount.setText(helper.getGoalAmount(c));
		g_notes.setText(helper.getGoalNotes(c));
		g_date.setText(helper.getGoalDate(c));
		c.close();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			g_date.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
					+ selectedYear);
		}
	};

}