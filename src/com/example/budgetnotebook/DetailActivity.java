package com.example.budgetnotebook;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class DetailActivity extends Activity {
	EditText item = null;
	EditText amount = null;
	EditText notes = null;
	RadioGroup types = null;
	EditText interval = null;
	EditText date = null;
	RecordHelper helper = null;
	String recordId = null;
	Transaction ts = null;
	boolean finalDB = true;
	private Calendar cal;
	private int day;
	private int month;
	private int year;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		helper = new RecordHelper(this);
		item = (EditText) findViewById(R.id.item);
		amount = (EditText) findViewById(R.id.amount);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.types);
		date = (EditText) findViewById(R.id.t_date);
		interval = (EditText) findViewById(R.id.interval);
		Button save = (Button) findViewById(R.id.save);

		save.setOnClickListener(onSave);

		// setContentView(R.layout.detail);
		recordId = getIntent().getStringExtra(MenuActivity.ID_EXTRA);
		Log.d("DetailActivity", "recordId=" + recordId);
		if (recordId != null) {
			load();
		}
		
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		ImageButton calendar = (ImageButton) findViewById(R.id.t_calendar);
		calendar.setOnClickListener(onDate);
	}

	// private View.OnClickListener onSave = new View.OnClickListener() {
	// public void onClick(View v) {

	// String type = null;

	// helper.insert(item.getText().toString(), amount.getText()
	// .toString(), type, notes.getText().toString());
	// model.requery();

	// clear the EditText fields after save button was pressed
	// item.setText("");
	// amount.setText("");
	// notes.setText("");
	// types.clearCheck();
	// }
	// };

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			String type = null;
			switch (types.getCheckedRadioButtonId()) {
			case R.id.income:
				// current.setType("Income");
				type = "Income";
				break;

			case R.id.spend:
				// current.setType("Spend");
				type = "Spend";
				break;

			case R.id.credit:
				// current.setType("Credit");
				type = "Credit";
				break;

			case R.id.debt:
				// current.setType("Debt");
				type = "Debt";
				break;
			}
			// using the DBHelper
			if (finalDB) {
				ts = new Transaction(0, item.getText().toString(), date
						.getText().toString(), amount.getText().toString(),
						"void", type, interval.getText().toString(), notes
								.getText().toString());
				if (recordId == null) {

					MainActivity.db_helper.addTransaction(ts);

				} else {

					MainActivity.db_helper.updateTransaction(ts);

				}
			} else {
				if (recordId == null) {
					helper.insert(item.getText().toString(), amount.getText()
							.toString(), type, notes.getText().toString());

				} else {

					helper.update(recordId, item.getText().toString(), amount
							.getText().toString(), type, notes.getText()
							.toString());

				}
			}
			finish();
		}
	};

	private void load() {
		if (!finalDB) {
			Cursor c = helper.getById(recordId);
			c.moveToFirst();
			item.setText(helper.getItem(c));
			amount.setText(helper.getAmount(c));
			notes.setText(helper.getNotes(c));
			if (helper.getType(c).equals("Income")) {
				types.check(R.id.income);
			} else if (helper.getType(c).equals("Spend")) {
				types.check(R.id.spend);
			} else if (helper.getType(c).equals("Credit")) {
				types.check(R.id.credit);
			} else {
				types.check(R.id.debt);
			}
			c.close();
		} else {
			Transaction t = MainActivity.db_helper.getTransaction(Integer
					.valueOf(recordId)+1);
			item.setText(t.getName());
			amount.setText(t.getAmount());
			notes.setText(t.getDescription());
			interval.setText(t.getInterval());
			date.setText(t.getDate());

			if (t.getType().equals("Income")) {
				types.check(R.id.income);
			} else if (t.getType().equals("Spend")) {
				types.check(R.id.spend);
			} else if (t.getType().equals("Credit")) {
				types.check(R.id.credit);
			} else {
				types.check(R.id.debt);
			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!finalDB)
			helper.close();
	}
	
	private View.OnClickListener onDate = new View.OnClickListener() {
		public void onClick(View v) {
			showDialog(0);
		}
	};
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			date.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
					+ selectedYear);
		}
	};
}
