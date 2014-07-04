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
	boolean finalDB = true;
	RadioGroup g_types = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal);
		if (!finalDB)
			helper = new GoalHelper(this);
		g_name = (EditText) findViewById(R.id.g_name);
		g_amount = (EditText) findViewById(R.id.g_amount);
		g_notes = (EditText) findViewById(R.id.g_notes);
		g_date = (EditText) findViewById(R.id.g_date);
		g_types = (RadioGroup) findViewById(R.id.g_types);

		ImageButton calendar = (ImageButton) findViewById(R.id.g_calendar);
		Button save = (Button) findViewById(R.id.g_save);
		Log.d("GoalActivity", "Button save=" + save);
		save.setOnClickListener(onSave);

		goalId = getIntent().getStringExtra(MainActivity.ID_EXTRA);
		Log.v("GoalActivity", "goalId = " + goalId);
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
			if (finalDB) {
				String type = null;
				switch (g_types.getCheckedRadioButtonId()) {
				case R.id.g_income:
					// current.setType("Income");
					type = "Income";
					break;

				case R.id.g_spend:
					// current.setType("Spend");
					type = "Spend";
					break;

				case R.id.g_credit:
					// current.setType("Credit");
					type = "Credit";
					break;

				case R.id.g_debt:
					// current.setType("Debt");
					type = "Debt";
					break;
				}

				Goal goal = new Goal(0, g_name.getText().toString(), g_notes
						.getText().toString(), type, g_amount.getText()
						.toString(), g_amount.getText().toString(), g_date
						.getText().toString());

				if (goalId == null) {
					MainActivity.db_helper.addGoal(goal);
					MainActivity.goal_updated = true;
				} else {
					MainActivity.db_helper.updateGoal(goal);
				}

			} else {
				if (goalId == null) {
					helper.insert(g_name.getText().toString(), g_amount
							.getText().toString(), g_date.getText().toString(),
							g_notes.getText().toString());
					MainActivity.goal_updated = true;
				} else {
					helper.update(g_name.getText().toString(), g_amount
							.getText().toString(), g_date.getText().toString(),
							g_notes.getText().toString());
				}
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
		if (finalDB) {

			Goal g = MainActivity.db_helper
					.getGoal(Integer.valueOf(goalId)+1);
			g_name.setText(g.getName());
			g_amount.setText(g.getStartAmount());
			g_notes.setText(g.getDescription());

			if (g.getType().equals("Income")) {
				g_types.check(R.id.g_income);
			} else if (g.getType().equals("Spend")) {
				g_types.check(R.id.g_spend);
			} else if (g.getType().equals("Credit")) {
				g_types.check(R.id.g_credit);
			} else {
				g_types.check(R.id.g_debt);
			}
		} else {
			Cursor c = helper.getById(goalId);
			c.moveToFirst();
			g_name.setText(helper.getGoalName(c));
			g_amount.setText(helper.getGoalAmount(c));
			g_notes.setText(helper.getGoalNotes(c));
			g_date.setText(helper.getGoalDate(c));
			c.close();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!finalDB)
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