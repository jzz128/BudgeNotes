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
	EditText a_balance = null;
	AccountHelper helper = null;
	String accountId = null;
	boolean finalDB = true;
	RadioGroup a_types = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_detail);
		if (!finalDB)
			helper = new AccountHelper(this);
		a_name = (EditText) findViewById(R.id.a_name);
		a_number = (EditText) findViewById(R.id.a_number);
		a_balance = (EditText) findViewById(R.id.a_balance);
		a_types = (RadioGroup) findViewById(R.id.a_types);
		Button save = (Button) findViewById(R.id.a_save);
		Log.d("AccountActivity", "Button save=" + save);
		save.setOnClickListener(onSave);

		accountId = getIntent().getStringExtra(MenuActivity.ID_EXTRA);
		Log.v("AccountActivity", "accountId = " + accountId);
		if (accountId != null) {
			load();
		}
	}

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			if (!finalDB) {
				helper.insert(a_name.getText().toString(), a_number.getText()
						.toString(), a_balance.getText().toString());
			} else {
				String type = null;
				switch (a_types.getCheckedRadioButtonId()) {
				case R.id.a_income:
					// current.setType("Income");
					type = "Income";
					break;

				case R.id.a_spend:
					// current.setType("Spend");
					type = "Spend";
					break;

				case R.id.a_credit:
					// current.setType("Credit");
					type = "Credit";
					break;

				case R.id.a_debt:
					// current.setType("Debt");
					type = "Debt";
					break;
				}

				Account account = new Account(a_name.getText().toString(),
						a_number.getText().toString(), type, a_balance
								.getText().toString());
				MainActivity.db_helper.addAccount(account);
			}
			finish();
			MainActivity.account_updated = true;
		}
	};

	private void load() {
		if (finalDB) {

			Account a = MainActivity.db_helper.getAccount(Integer
					.valueOf(accountId));
			a_name.setText(a.getName());
			a_number.setText(a.getNumber());
			a_balance.setText(a.getBalance());

			if (a.getType().equals("Income")) {
				a_types.check(R.id.a_income);
			} else if (a.getType().equals("Spend")) {
				a_types.check(R.id.a_spend);
			} else if (a.getType().equals("Credit")) {
				a_types.check(R.id.a_credit);
			} else {
				a_types.check(R.id.a_debt);
			}
		} else {
			Cursor c = helper.getById(accountId);
			c.moveToFirst();
			a_name.setText(helper.getAccountName(c));
			a_number.setText(helper.getAccountNumber(c));
			a_balance.setText(helper.getAccountNotes(c));
			c.close();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(!finalDB)
		helper.close();
	}
}
