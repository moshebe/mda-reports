package com.mdareports.ui.fragments.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdareports.R;
import com.mdareports.db.models.Report;
import com.mdareports.ui.custom.bloodpressure.BloodPressureField;
import com.mdareports.ui.custom.single.EditableSeekField;

public class TechInfoFragment extends BaseDetailFragment {

	private EditableSeekField esFieldPulse;
	private EditableSeekField esFieldSugar;
	private EditableSeekField esFieldBreath;
	private BloodPressureField bloodPressureField;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tech_info,
				container, false);

		// set the fields members
		esFieldPulse = (EditableSeekField) rootView
				.findViewById(R.id.esfieldPulse);
		esFieldSugar = (EditableSeekField) rootView
				.findViewById(R.id.esfieldSugar);
		esFieldBreath = (EditableSeekField) rootView
				.findViewById(R.id.esfieldBreath);

		bloodPressureField = (BloodPressureField) rootView
				.findViewById(R.id.bloodPressureField);

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();

		// load the details from the report into the fields
		refreshDataWithCurrentReport();
	}

	@Override
	public void saveCurrentReport() {
		// get the current displayed report
		Report currentReport = getCurrentReport();

		currentReport.setPulse(esFieldPulse.getValue());
		currentReport.setSugar(esFieldSugar.getValue());
		currentReport.setBreath(esFieldBreath.getValue());

		currentReport.setMinBloodPressure(bloodPressureField.getLowValue());
		currentReport.setMaxBloodPressure(bloodPressureField.getHighValue());

	}

	@Override
	public void refreshDataWithCurrentReport() {
		// get the current displayed report
		Report currentReport = getCurrentReport();

		esFieldPulse.setValue(currentReport.getPulse());
		esFieldSugar.setValue(currentReport.getSugar());
		esFieldBreath.setValue(currentReport.getBreath());

		bloodPressureField.setValues(currentReport.getMaxBloodPressure(),
				currentReport.getMinBloodPressure());

	}

	@Override
	public int getTabTitleResourceId() {
		return R.string.fragment_tech_info_title;
	}

}
