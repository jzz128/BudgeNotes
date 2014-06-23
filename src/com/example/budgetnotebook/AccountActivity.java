package com.example.budgetnotebook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class AccountActivity extends Activity {
	EditText a_name = null;
	EditText a_number = null;
	EditText a_notes = null;
	AccountHelper helper = null;
	String accountId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_detail);
		helper = new AccountHelper(this);
		a_name = (EditText) findViewById(R.id.a_name);
		a_number = (EditText) findViewById(R.id.a_number);
		a_notes = (EditText) findViewById(R.id.a_notes);
		Button save = (Button) findViewById(R.id.a_save);
		Log.d("AccountActivity","Button save=" + save);
		save.setOnClickListener(onSave);

		accountId = getIntent().getStringExtra(MainActivity.ID_EXTRA);
		if (accountId != null) {
			load();
		}
	}
	


	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
				helper.insert(a_name.getText().toString(), a_number.getText()
						.toString(), a_notes.getText().toString());
			finish();
			MainActivity.account_updated = true;
		}
	};

	private void load() {
		Cursor c = helper.getById(accountId);
		c.moveToFirst();
		a_name.setText(helper.getAccountName(c));
		a_number.setText(helper.getAccountNumber(c));
		a_notes.setText(helper.getAccountNotes(c));
		c.close();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}
}
