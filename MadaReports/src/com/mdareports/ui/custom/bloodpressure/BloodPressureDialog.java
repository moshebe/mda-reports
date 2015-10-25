package com.mdareports.ui.custom.bloodpressure;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mdareports.R;

public class BloodPressureDialog extends RelativeLayout {

	private EditText txtValueHigh;
	private EditText txtValueLow;

	public BloodPressureDialog(Context context, AttributeSet attrs) {
		super(context, attrs);

		// inflate the layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.dialog_blood_pressure, this, true);

		// set the views members
		txtValueHigh = (EditText) findViewById(R.id.txtBloodPressureValueHigh);
		txtValueLow = (EditText) findViewById(R.id.txtBloodPressureValueLow);

	}

	public void openDialog(Context context, String title,
			DialogInterface.OnClickListener onPositiveClick) {
		new AlertDialog.Builder(context)
				.setView(this)
				.setTitle(title)
				.setPositiveButton(android.R.string.ok, onPositiveClick)
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).show();
	}

	/**
	 * Set the values of the field.
	 * 
	 * @param high
	 *            - the high blood pressure value. the left value.
	 * @param low
	 *            - the low blood pressure value. the right value.
	 */
	public void setValues(int high, int low) {
		txtValueHigh.setText(String.valueOf(high));
		txtValueLow.setText(String.valueOf(low));
	}

	/**
	 * Get the current values of the field
	 */
	public int getHighValue() {
		ensureHighValueInRight();
		return getValue(txtValueHigh);
	}

	public int getLowValue() {
		ensureHighValueInRight();
		return getValue(txtValueLow);
	}

	private int getValue(EditText txt) {
		try {
			return Integer.parseInt(txt.getEditableText().toString());
		} catch (Exception e) {
			return -1;
		}
	}

	private void ensureHighValueInRight() {
		int high = getValue(txtValueHigh);
		int low = getValue(txtValueLow);
		if (low > high) {
			txtValueHigh.setText(String.valueOf(low));
			txtValueLow.setText(String.valueOf(high));
		}
	}
}
