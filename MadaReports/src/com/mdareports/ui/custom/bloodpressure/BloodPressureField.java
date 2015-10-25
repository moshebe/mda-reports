package com.mdareports.ui.custom.bloodpressure;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdareports.R;

/**
 * Represents range field in the technical tab, currently only the blood
 * pressure field.
 */
public class BloodPressureField extends RelativeLayout {
	private TextView tvLabel;
	private TextView tvValueHigh;
	private TextView tvValueLow;
	private ImageView imgEdit;
		
	/**
	 * Inflate the field layout, set the binding between the related views and
	 * initialize using the attributes from the XML layout
	 * 
	 * @param context
	 *            - the current context
	 * @param attrs
	 *            - the attributes defined in the layout
	 */
	public BloodPressureField(Context context, AttributeSet attrs) {
		super(context, attrs);

		// inflate the layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.field_technical_editable_blood_pressure, this, true);

		// set the views members
		tvLabel = (TextView) findViewById(R.id.tvLabelBloodPressure);
		tvValueHigh = (TextView) findViewById(R.id.tvBloodPressureValueHigh);
		tvValueLow = (TextView) findViewById(R.id.tvBloodPressureValueLow);
		imgEdit = (ImageView) findViewById(R.id.imgEditBloodPressureField);

		// set the label
		tvLabel.setText(R.string.fragment_tech_info_blood_pressure);

		// set the edit image click handler
		imgEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final BloodPressureDialog myLayout = new BloodPressureDialog(
						getContext(), null);
				
				// pass the values to the dialog
				myLayout.setValues(getHighValue(), getLowValue());		
				
				// open the dialog
				myLayout.openDialog(getContext(), tvLabel.getText().toString(),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {							
								tvValueHigh.setText(String.valueOf(myLayout.getHighValue()));
								tvValueLow.setText(String.valueOf(myLayout.getLowValue()));
							}
						});
			}
		});

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
		tvValueHigh.setText(String.valueOf(high));
		tvValueLow.setText(String.valueOf(low));
	}

	/**
	 * Get the current values of the field
	 */
	public int getHighValue() {
		return getValue(tvValueHigh);
	}

	public int getLowValue() {
		return getValue(tvValueLow);
	}

	private int getValue(TextView txt) {
		try {
			return Integer.parseInt(txt.getText().toString());
		} catch (Exception e) {
			return -1;
		}
	}

}
