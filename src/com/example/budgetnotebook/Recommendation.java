package com.example.budgetnotebook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Recommendation extends Activity {
	
	Button viewRec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_recommendations);
		
		viewRec = (Button) findViewById(R.id.viewRecommendations);
		viewRec.setOnClickListener(new View.OnClickListener() {		
						
		@Override
			public void onClick(View v) {
				// TODO Provide Recommendations.
			}	
		});
	}
}
