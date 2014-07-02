package com.example.budgetnotebook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
//import android.app.Fragment;
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

public class Transaction extends Activity {

	// I commented out what was in here because I just wanted to be consistent with what everything else looks like.  Also some of the methods and Types are deprecated. - DJM
	
	/** 
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
	 **/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.view_transaction);

		// DATABASE OPEN AND TEST USING TOAST ---------------------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
				
		//Create Database instance
		DBHelper db = new DBHelper(getBaseContext());
						
		// Testing Transaction with Toast 
		db.toastTransaction(getBaseContext());
		
		// Add a Transaction -- Test Success
		db.addTransaction(new Transaction(1, "Trans 1", "7-1-2014", "$500", "Rent", "Debit", "Monthly", "Made my rent payment."));
	
		// Testing Transaction with Toast
		db.toastTransaction(getBaseContext());
		
		// Delete Transaction -- Test Success
		db.deleteTransaction(db.getTransaction(1));
		
		// Testing Transaction with Toast
		db.toastTransaction(getBaseContext());
		
		// Close db
		db.close();
				
		// --------------------------------------------------------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
		
		
		
		/**
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
		**/
	}
	
	private int _id;
	private int t_a_id;
	private String transaction_name;
	private String transaction_date;
	private String transaction_amount;
	private String transaction_category;
	private String transaction_type;
	private String transaction_interval;
	private String transaction_description;
	
	public Transaction(){}
	
	public Transaction(int t_a_id, String transaction_name, String transaction_date, String transaction_amount, String transaction_category, String transaction_type, String transaction_interval, String transaction_description) {
		super();
		this.t_a_id = t_a_id;
		this.transaction_name = transaction_name;
		this.transaction_date = transaction_date;
		this.transaction_amount = transaction_amount;
		this.transaction_category = transaction_category;
		this.transaction_type = transaction_type;
		this.transaction_interval = transaction_interval;
		this.transaction_description = transaction_description;
	}
	
	@Override
	public String toString() {
		return "Goal [id=" + _id + ", t_a_id=" + t_a_id + ", transaction_name=" + transaction_name + ", transaction_date=" + transaction_date + ", transaction_amount=" + transaction_amount + ", transaction_category=" + transaction_category + ", transaction_type=" + transaction_type + ", transaction_interval=" + transaction_interval + ", transaction_description=" + transaction_description +"]";
	}
	
	//Getters --------------------------------------------------------------------
	public int getId(){
		return _id;
	}
	
	public int getAID() {
		return t_a_id;
	}
	
	public String getName() {
		return transaction_name;
	}
	
	public String getDate() {
		return transaction_date;
	}
	
	public String getAmount() {
		return transaction_amount;
	}
	
	public String getCategory() {
		return transaction_category;
	}
	
	public String getType() {
		return transaction_type;
	}
	
	public String getInterval() {
		return transaction_interval;
	}
	
	public String getDescription() {
		return transaction_description;
	}
	
	//Setters --------------------------------------------------------------------
	public void setId(int id){
		this._id = id;
	}
	
	public void setAId(int a_id){
		this.t_a_id = a_id;
	}
	
	public void setName(String name){
		this.transaction_name = name;
	}
	
	public void setDate(String transaction_date) {
		this.transaction_date = transaction_date;
	}
	
	public void setAmount(String transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	
	public void setCategory(String transaction_category) {
		this.transaction_category = transaction_category;
	}
	
	public void setType(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	public void setInterval(String transaction_interval) {
		this.transaction_interval = transaction_interval;
	}
	
	public void setDescription(String transaction_description) {
		this.transaction_description = transaction_description;
	}
	
	/**
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
			super(Transaction.this, c);
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
	**/

}
