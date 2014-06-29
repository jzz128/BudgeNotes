package com.example.budgetnotebook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class RecommendActivity extends Activity {
	RecommendHelper helper;
	EditText re_notes = null;
	boolean bRecReady = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		re_notes = (EditText) findViewById(R.id.re_notes);
		helper = new RecommendHelper(this);

		Cursor c = helper.getById();

		if (c.moveToFirst()) {
			re_notes.setText(helper.getNotes(c));
			bRecReady = true;
			Log.d("RecommendActivity", "bRecReady=true");
		} else {
			bRecReady = false;
			Log.d("RecommendActivity", "bRecReady=false");
			
			/* temporally add sample entry for testing, remove the following later */
			helper.insert("Sample Recommendations!!!");
			/* done */
		}
		c.close();
	}

}
