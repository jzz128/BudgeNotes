package com.example.budgetnotebook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailActivity extends Activity {
	EditText item = null;
	EditText amount = null;
	EditText notes = null;
	RadioGroup types = null;
	RecordHelper helper = null;
	String recordId = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		helper = new RecordHelper(this);
		item = (EditText) findViewById(R.id.item);
		amount = (EditText) findViewById(R.id.amount);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.types);
		Button save = (Button) findViewById(R.id.save);
		
		save.setOnClickListener(onSave);

		//setContentView(R.layout.detail);
		recordId = getIntent().getStringExtra(MenuActivity.ID_EXTRA);
		Log.d("DetailActivity","recordId=" + recordId);
		if (recordId != null) {
			load();
		}
	}

	//private View.OnClickListener onSave = new View.OnClickListener() {
	//	public void onClick(View v) {

	//		String type = null;


			// helper.insert(item.getText().toString(), amount.getText()
			// .toString(), type, notes.getText().toString());
			// model.requery();

			// clear the EditText fields after save button was pressed
			//item.setText("");
			//amount.setText("");
			//notes.setText("");
			//types.clearCheck();
		//}
	//};

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
			if (recordId == null) {
				helper.insert(item.getText().toString(), amount.getText()
						.toString(), type, notes.getText().toString());
			} else {
				helper.update(recordId, item.getText().toString(), amount
						.getText().toString(), type, notes.getText().toString());
			}
			finish();
		}
	};

	private void load() {
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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}
}
