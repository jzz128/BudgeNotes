package com.example.budgetnotebook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TabActivity {

	Cursor model = null;
	RecordAdapter adapter = null;
	EditText item = null;
	EditText amount = null;
	EditText notes = null;
	RadioGroup types = null;
	Record current = null;
	RecordHelper helper = null;
	AtomicBoolean isActive = new AtomicBoolean(true);
	int progress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);

		helper = new RecordHelper(this);
		item = (EditText) findViewById(R.id.item);
		amount = (EditText) findViewById(R.id.amount);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.types);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(onSave);

		ListView list = (ListView) findViewById(R.id.records);
		// list.setOnItemClickListener(onListClick);

		model = helper.getAll();
		startManagingCursor(model);
		adapter = new RecordAdapter(model);
		list.setAdapter(adapter);

		TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
		spec.setContent(R.id.records);
		spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
		getTabHost().addTab(spec);

		spec = getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.details);
		spec.setIndicator("Details",
				getResources().getDrawable(R.drawable.details));
		getTabHost().addTab(spec);

		getTabHost().setCurrentTab(1);
		list.setOnItemClickListener(onListClick);
	}

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			// Record r = new Record();
			// EditText item = (EditText) findViewById(R.id.item);
			// EditText amount = (EditText) findViewById(R.id.amount);
			current = new Record();

			current.setItem(item.getText().toString());
			current.setAmount(amount.getText().toString());
			current.setNotes(notes.getText().toString());
			String type = null;

			// RadioGroup types = (RadioGroup) findViewById(R.id.types);

			switch (types.getCheckedRadioButtonId()) {
			case R.id.income:
				current.setType("Income");
				type = "Income";
				break;

			case R.id.spend:
				current.setType("Spend");
				type = "Spend";
				break;

			case R.id.credit:
				current.setType("Credit");
				type = "Credit";
				break;

			case R.id.debt:
				current.setType("Debt");
				type = "Debt";
				break;
			}

			helper.insert(item.getText().toString(), amount.getText()
					.toString(), type, notes.getText().toString());
			model.requery();
			
			// clear the EditText fields after save button was pressed
			item.setText("");
			amount.setText("");
			notes.setText("");
			types.clearCheck();
		}
	};

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			model.moveToPosition(position);
			item.setText(helper.getItem(model));
			amount.setText(helper.getAmount(model));
			if (helper.getType(model).equals("Income")) {
				types.check(R.id.income);
			} else if (helper.getType(model).equals("Spend")) {
				types.check(R.id.spend);
			} else if (helper.getType(model).equals("Credit")) {
				types.check(R.id.credit);
			} else {
				types.check(R.id.debt);
			}
			getTabHost().setCurrentTab(1);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.toast) {
			String message = "No record selected";
			if (current != null) {
				message = current.getNotes();
			}
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			return (true);
		} else if (item.getItemId() == R.id.run) {
			startWork();
			setProgressBarVisibility(true);
		}
		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onPause() {
		super.onPause();
		isActive.set(false);
	}

	@Override
	public void onResume() {
		super.onResume();
		isActive.set(true);
		if (progress > 0) {
			startWork();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	private void startWork() {
		setProgressBarVisibility(true);
		new Thread(longTask).start();
	}

	private void doSomeLongWork(final int incr) {
		SystemClock.sleep(250); // should be something more useful!
	}

	private Runnable longTask = new Runnable() {
		public void run() {
			for (int i = progress; i < 10000 && isActive.get(); i += 200) {
				doSomeLongWork(200);
			}
			if (isActive.get()) {
				runOnUiThread(new Runnable() {
					public void run() {
						setProgressBarVisibility(false);
						progress = 0;
					}
				});
			}
		}
	};

	class RecordAdapter extends CursorAdapter {
		RecordAdapter(Cursor c) {
			super(MainActivity.this, c);
		}

		public void bindView(View row, Context ctxt, Cursor c) {
			RecordHolder holder = (RecordHolder) row.getTag();
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RecordHolder holder = new RecordHolder(row);
			row.setTag(holder);
			return (row);
		}
	}

	static class RecordHolder {
		private TextView name = null;
		private TextView amount = null;
		private ImageView icon = null;
		private View row = null;

		RecordHolder(View row) {
			this.row = row;
			name = (TextView) row.findViewById(R.id.title);
			amount = (TextView) row.findViewById(R.id.number);
			icon = (ImageView) row.findViewById(R.id.icon);
		}

		void populateFrom(Cursor c, RecordHelper r) {
			name.setText(r.getItem(c));
			amount.setText(r.getAmount(c));
			if (r.getType(c) == null)
				return;
			if (r.getType(c).equals("Income")) {
				icon.setImageResource(R.drawable.income);
			} else if (r.getType(c).equals("Spend")) {
				icon.setImageResource(R.drawable.spend);
			} else if (r.getType(c).equals("Credit")) {
				icon.setImageResource(R.drawable.credit);
			} else {
				icon.setImageResource(R.drawable.debt);
			}
		}
	}

}
