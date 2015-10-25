package com.mdareports.ui.custom.patient.report;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdareports.R;

public class PatientReportField extends LinearLayout {

	EditText txtValue;
	TextView tvLabel;

	public PatientReportField(Context context, AttributeSet attrs) {
		super(context, attrs);

		// inflate the layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.field_patient_report, this, true);

		// set the views members
		txtValue = (EditText) findViewById(R.id.txtPatientReportValue);
		tvLabel = (TextView) findViewById(R.id.tvPatientReportLabel);

		// get the attributes from the XML
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PatientReportField, 0, 0);
		try {
			// set the label
			int labelResId = a.getResourceId(
					R.styleable.PatientReportField_labelText, -1);
			if (labelResId != -1)
				tvLabel.setText(labelResId);

			// set the value
			int valueEnum = a.getInt(R.styleable.PatientReportField_inputType,
					-1);

			if (valueEnum == 1/* number */) {
				txtValue.setRawInputType(InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			}

		} finally {
			a.recycle();
		}

	}

	public String getContent() {
		String result = "";
		if (txtValue != null)
			result = txtValue.getText().toString();

		return result;
	}

}
