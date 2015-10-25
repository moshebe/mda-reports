package com.mdareports.ui.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.sms.SmsSender;
import com.mdareports.ui.custom.patient.report.PatientReportField;

public class PatientReportActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_patient_report);

		// Get the action bar and set it up
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		getButton(R.id.btnSendReport).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String reportMsg = getReportMessage();

				// send the report in the native SMS application
				SmsSender.send(v.getContext(), reportMsg);

				Toast.makeText(v.getContext(), reportMsg, Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

	private String getContent(int resId) {
		return ((PatientReportField) findViewById(resId)).getContent();
	}

	private String getReportMessage() {
		String result = "";
		String visa, commitment, sum, form, code, firstName, familyName;

		// get the values from the fields
		visa = getContent(R.id.prFieldVisa);
		commitment = getContent(R.id.prFieldCommitment);
		sum = getContent(R.id.prFieldSum);
		form = getContent(R.id.prFieldForm);
		code = getContent(R.id.prFieldCode);
		familyName = getContent(R.id.prFieldFamilyName);
		firstName = getContent(R.id.prFieldFirstName);

		// set the values in the report
		if (visa != "")
			result += "ויזה:" + visa + ", ";

		result += "התחייבות:" + commitment + ", ";
		result += "סכום:" + sum + ", ";
		result += "טופס:" + form + ", ";
		result += "קוד:" + code + ", ";
		result += "שם פרטי:" + firstName + ", ";
		result += "משפחה:" + familyName;

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
