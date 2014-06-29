package com.example.budgetnotebook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

public class AlertActivity extends Activity {
	EditText al_notes = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);
		al_notes = (EditText) findViewById(R.id.al_notes);

		Cursor c = MainActivity.alert_helper.getById();

		if (c.moveToFirst()) {
			al_notes.setText(MainActivity.alert_helper.getNotes(c));

		} else {
			/*
			 * temporally add sample entry for testing, remove the following
			 * later
			 */
			MainActivity.alert_helper.insert("Sample Alerts!!!");
			/* done */
		}
		c.close();
	}

}
