package com.example.budgetnotebook;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlertActivity extends ListActivity {
	DBHelper db;
	List<Alert> list = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);
		
		// Access the database.
		db = new DBHelper(getBaseContext());
		
		list = db.getListAllAlerts();
		ListAdapter AlertsAdapter = new ListAdapter();
		setListAdapter(AlertsAdapter);
	}
	
	
	class ListAdapter extends ArrayAdapter<Alert> {
		ListAdapter() {
			super(AlertActivity.this, R.layout.alert_row, list);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			AlertHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.alert_row, parent, false);
				holder = new AlertHolder(row);
				row.setTag(holder);
			} else {
				holder = (AlertHolder) row.getTag();
			}
			holder.populateFrom(list.get(position));
			return (row);
		}

	}
	
	static class AlertHolder {
		private TextView name = null;
		private TextView description = null;
		private TextView date = null;
		@SuppressWarnings("unused")
		private View row = null;

		AlertHolder(View row) {
			this.row = row;
			name = (TextView) row.findViewById(R.id.alert_name);
			description = (TextView) row.findViewById(R.id.alert_description);
			date = (TextView) row.findViewById(R.id.due_date);
		}

		void populateFrom(Alert at) {
			name.setText(at.getName());
			description.setText(at.getDescription());
			date.setText(at.getDueDate());

		}
	}

}
