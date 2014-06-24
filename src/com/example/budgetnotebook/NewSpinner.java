package com.example.budgetnotebook;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public 	class NewSpinner extends Spinner {
	OnItemSelectedListener listener;

	public NewSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setSelection(int position) {
		super.setSelection(position);
		if (listener != null)
			listener.onItemSelected(this, null, position, 0);
	}

	public void setOnItemSelectedEvenIfUnchangedListener(
			OnItemSelectedListener listener) {
		this.listener = listener;
	}
}