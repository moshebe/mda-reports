package com.mdareports.ui.fragments;

import java.security.InvalidParameterException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.sms.SmsSender;
import com.mdareports.ui.custom.patient.report.PatientReportField;

public class PatientReportFragment extends BaseFragment {

	private View rootView; // cached root view

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(
				R.layout.fragment_patient_report, container, false);

		changeTitle(R.string.title_patient_report);

		rootView.findViewById(R.id.btnSendReport).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							String reportMsg = getReportMessage();

							// send the report in the native SMS application
							SmsSender.send(v.getContext(), reportMsg);

							Toast.makeText(v.getContext(), reportMsg,
									Toast.LENGTH_SHORT).show();

						} catch (Exception e) {
							Toast.makeText(getActivity(), e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}

					}
				});

		// cache the root view for re-using
		this.rootView = rootView;

		return rootView;
	}

	private String getContent(int resId) {
		return ((PatientReportField) rootView.findViewById(resId)).getContent();
	}

	private String getReportMessage() {
		String result = "";
		String visa, commitment, sum, form, code, firstName, familyName, patientID, operationalCode;

		// get the values from the fields
		patientID = getContent(R.id.prFieldPatientID);

		if (!isValidIsraelID(patientID)) {
			throw new InvalidParameterException(getResources().getString(
					R.string.patient_report_invalid_patient_id));
		}

		visa = getContent(R.id.prFieldVisa);
		operationalCode = getContent(R.id.prFieldOperationalCode);
		commitment = getContent(R.id.prFieldCommitment);
		sum = getContent(R.id.prFieldSum);
		form = getContent(R.id.prFieldForm);
		code = getContent(R.id.prFieldCode);
		familyName = getContent(R.id.prFieldFamilyName);
		firstName = getContent(R.id.prFieldFirstName);

		// set the values in the report
		if (visa != "")
			result += "ויזה:" + visa + ", ";

		result += "קוד מבצעי:" + operationalCode + ", ";
		result += "התחייבות:" + commitment + ", ";
		result += "סכום:" + sum + ", ";
		result += "טופס:" + form + ", ";
		result += "קוד:" + code + ", ";
		result += "תז:" + patientID + ", ";
		result += "שם פרטי:" + firstName + ", ";
		result += "משפחה:" + familyName;

		return result;
	}

	private boolean isValidIsraelID(String id) {

		try {
			int size = id.length();

			if (size != 9) {
				return false;
			}
			int counter = 0, incNum;

			for (int i = 0; i < size; i++) {
				incNum = Integer.parseInt(String.valueOf(id.charAt(i)))
						* ((i % 2) + 1); // multiply digit by 1 or 2
				counter += (incNum > 9) ? incNum - 9 : incNum;// sum the digits
																// up and add to
																// counter
			}
			return (counter % 10 == 0);

		} catch (Exception e) {
			return false;
		}
	}
}