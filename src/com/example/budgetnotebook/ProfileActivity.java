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

public class ProfileActivity extends Activity {
	EditText p_firstname = null;
	EditText p_lastname = null;
	EditText p_gender = null;
	EditText p_birthday = null;
	EditText p_city = null;
	EditText p_email = null;
	ProfileHelper helper = null;
	Boolean bFilled = false;
	private Calendar cal;
	private int day;
	private int month;
	private int year;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		helper = new ProfileHelper(this);
		p_firstname = (EditText) findViewById(R.id.first_name);
		p_lastname = (EditText) findViewById(R.id.last_name);
		p_gender = (EditText) findViewById(R.id.gender);
		p_city = (EditText) findViewById(R.id.city);
		p_email = (EditText) findViewById(R.id.email);
		p_birthday = (EditText) findViewById(R.id.b_date);
		ImageButton calendar = (ImageButton) findViewById(R.id.p_calendar);
		Button save = (Button) findViewById(R.id.p_save);
		Log.d("ProfileActivity", "Button save=" + save);
		save.setOnClickListener(onSave);

		load();

		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		calendar.setOnClickListener(onDate);

	}

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			if (bFilled == false) {
				helper.insert(p_firstname.getText().toString(), p_lastname
						.getText().toString(), p_gender.getText().toString(),
						p_city.getText().toString(), p_email.getText()
								.toString(), p_birthday.getText().toString());

			} else {
				helper.update("1", p_firstname.getText().toString(), p_lastname
						.getText().toString(), p_gender.getText().toString(),
						p_city.getText().toString(), p_email.getText()
								.toString(), p_birthday.getText().toString());
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
		Cursor c = helper.getById();
		if (c.moveToFirst()) {
			p_firstname.setText(helper.getFirstName(c));
			p_lastname.setText(helper.getLastName(c));
			p_gender.setText(helper.getGender(c));
			p_city.setText(helper.getCity(c));
			p_email.setText(helper.getEmail(c));
			p_birthday.setText(helper.getBirthday(c));
			bFilled = true;
			Log.d("ProfileActivity", "bFilled=true");
		} else {
			bFilled = false;
			Log.d("ProfileActivity", "bFilled=false");
		}
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
			p_birthday.setText(selectedDay + " / " + (selectedMonth + 1)
					+ " / " + selectedYear);
		}
	};

}