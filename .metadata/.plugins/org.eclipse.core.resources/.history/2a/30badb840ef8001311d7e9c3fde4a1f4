package com.example.budgetnotebook.test;

import android.app.Activity;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.budgetnotebook.MainActivity;
import com.example.budgetnotebook.R;

public class ActivityValidation extends ActivityInstrumentationTestCase2 {
	EditText item, amount, notes;
	RadioGroup types;
	MainActivity activity;
	Button save;
	TextView listItem, listAmount;
	TabHost tabHost;

	
	public ActivityValidation(String name) {
		super("com.example",MainActivity.class);
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		// Find views
		activity = (MainActivity) this.getActivity();
		assertNotNull(activity);
		item = (EditText) activity.findViewById(com.example.budgetnotebook.R.id.item);
		amount = (EditText) activity.findViewById(com.example.budgetnotebook.R.id.amount);
		notes = (EditText) activity.findViewById(com.example.budgetnotebook.R.id.notes);
		types = (RadioGroup) activity.findViewById(com.example.budgetnotebook.R.id.types);
		save = (Button) activity.findViewById(com.example.budgetnotebook.R.id.save);
	//	tabHost = (TabHost)activity.findViewById(com.example.budgetnotebook.R.id.details);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	@SmallTest
	public void testEmptyOnSave() {
	//	activity.getTabHost().setCurrentTab(1);
		TouchUtils.tapView(this, item);
		sendKeys("B i c y c l e");
		TouchUtils.tapView(this, amount);
		sendKeys("1 9 9 . 0 0");
		TouchUtils.tapView(this, notes);
		sendKeys("C h r i s t m a s p r e s e n t   f o r   R i c h a r d .");
		RadioButton spend = (RadioButton) activity.findViewById(com.example.budgetnotebook.R.id.spend);
		TouchUtils.tapView(this, spend);
		TouchUtils.clickView(this, save);
		SystemClock.sleep(1000);
		super.assertEquals("Item field is not clear upon save", "", item.getText().toString());
		super.assertEquals("Amount field is not empty upon save", "", amount.getText().toString());
		super.assertEquals("Notes field is not empty upon save", "", notes.getText().toString());	
		
	}
	
	@UiThreadTest
	public void testViewsCreated() {
		assertNotNull(this.getActivity());
		assertNotNull(item);
		assertNotNull(amount);
		assertNotNull(notes);
		assertNotNull(types);
	}
	
	@UiThreadTest
	public void testViewVisible() {
		activity.getTabHost().setCurrentTab(1);
		ViewAsserts.assertOnScreen(item.getRootView(), amount);
		ViewAsserts.assertOnScreen(amount.getRootView(), item);
		ViewAsserts.assertOnScreen(amount.getRootView(), notes);
		ViewAsserts.assertOnScreen(amount.getRootView(), types);
	}
	
	@UiThreadTest
	public void testStartingEmpty() {
		activity.getTabHost().setCurrentTab(1);
		super.assertEquals("Item field is not empty", "", item.getText().toString());
		super.assertEquals("Amount field is not empty", "", amount.getText().toString());
		super.assertEquals("Notes field is not empty", "", notes.getText().toString());
	}
	
}
