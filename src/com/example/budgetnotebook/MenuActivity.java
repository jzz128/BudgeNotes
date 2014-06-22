package com.example.budgetnotebook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
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

public class MenuActivity extends ListActivity {

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
	public final static String ID_EXTRA="apt.tutorial._ID";
	String account_id=null;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);

		helper = new RecordHelper(this);
		item = (EditText) findViewById(R.id.item);
		amount = (EditText) findViewById(R.id.amount);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.types);

		//Button save = (Button) findViewById(R.id.save);
		//save.setOnClickListener(onSave);

		//ListView list = (ListView) findViewById(R.id.records);
		// list.setOnItemClickListener(onListClick);
		
		
		account_id = getIntent().getStringExtra(MainActivity.ID_EXTRA);
		if (account_id != null) {
			model = helper.getAll();
			startManagingCursor(model);
			adapter = new RecordAdapter(model);
			setListAdapter(adapter);
		}

		//model = helper.getAll();
		//startManagingCursor(model);
		//adapter = new RecordAdapter(model);
		//setListAdapter(adapter);

	}


	
	public void onListItemClick(ListView list, View view,
			int position, long id) {
			Intent i=new Intent(MenuActivity.this, DetailActivity.class);
			i.putExtra(ID_EXTRA, String.valueOf(id));
			startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(MenuActivity.this, DetailActivity.class));
			return(true);
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
			super(MenuActivity.this, c);
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