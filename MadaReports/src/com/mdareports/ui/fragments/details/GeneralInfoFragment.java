package com.mdareports.ui.fragments.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.mdareports.R;
import com.mdareports.db.models.Report;
import com.mdareports.ui.activities.ReportLocationActivity;
import com.mdareports.ui.activities.details.DetailsActivity;
import com.mdareports.utils.HelpUtils;

public class GeneralInfoFragment extends BaseDetailFragment {

	private EditText txtReportId;
	private EditText txtAddress;
	private EditText txtDescription;
	private EditText txtNotes;
	private CheckBox ckbIsReported;
	private ImageView imgIsReportedIcon;
	private ImageView imgPinLocation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_general_info,
				container, false);

		rootView.findViewById(R.id.imgPinLocation).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(v.getContext(),
								ReportLocationActivity.class);
						i.putExtra(ReportLocationActivity.REPORT_ID_EXTRA,
								getCurrentReport().getId());
						startActivity(i);
					}
				});

		txtReportId = (EditText) rootView.findViewById(R.id.txtReportId);
		txtAddress = (EditText) rootView.findViewById(R.id.txtAddress);
		txtDescription = (EditText) rootView.findViewById(R.id.txtDescription);
		txtNotes = (EditText) rootView.findViewById(R.id.txtNotes);
		ckbIsReported = (CheckBox) rootView.findViewById(R.id.ckbIsReported);
		imgIsReportedIcon = (ImageView) rootView
				.findViewById(R.id.imgIsReportedIcon);
		imgPinLocation = (ImageView) rootView.findViewById(R.id.imgPinLocation);

		OnCheckedChangeListener isReportedCheckedChangedListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				imgIsReportedIcon
						.setImageResource((isChecked ? R.drawable.ic_action_accept
								: R.drawable.ic_action_warning));
			}
		};

		// first call to onChecked change to initialize the icon
		isReportedCheckedChangedListener.onCheckedChanged(ckbIsReported,
				getCurrentReport().isReported());
		ckbIsReported
				.setOnCheckedChangeListener(isReportedCheckedChangedListener);

		HelpUtils.showHelp(imgPinLocation,
				R.string.help_showcase_report_details_location_title,
				R.string.help_showcase_report_details_location_description,
				getActivity());

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();

		Report currentReport = getCurrentReport();

		txtReportId.setText(String.valueOf(currentReport.getReportId()));
		txtAddress.setText(currentReport.getAddress());
		txtDescription.setText(currentReport.getDescription());
		txtNotes.setText(currentReport.getNotes());
		ckbIsReported.setChecked(currentReport.isReported());
	}

	@Override
	public void onResume() {
		super.onResume();

		// report information (like the address) might be changed in
		// the map activity so re-load it from the DB and refresh the fields
		((DetailsActivity) getActivity())
				.loadCurrentReportFromDb(getCurrentReport().getId());
		refreshDataWithCurrentReport();
	}

	@Override
	public void saveCurrentReport() {
		Report currentReport = getCurrentReport();

		currentReport.setReportId(Integer.valueOf(txtReportId.getText()
				.toString()));
		currentReport.setAddress(txtAddress.getText().toString());
		currentReport.setDescription(txtDescription.getText().toString());
		currentReport.setNotes(txtNotes.getText().toString());
		currentReport.setReported(ckbIsReported.isChecked());
	}

	@Override
	public void refreshDataWithCurrentReport() {
		txtReportId.setText(String.valueOf(getCurrentReport().getReportId()));
		txtAddress.setText(getCurrentReport().getAddress());
		txtDescription.setText(String.valueOf(getCurrentReport()
				.getDescription()));
		txtNotes.setText(getCurrentReport().getNotes());
		ckbIsReported.setChecked(getCurrentReport().isReported());
		imgPinLocation
				.setImageResource(getCurrentReport().hasLocation() ? R.drawable.maps_pin
						: R.drawable.maps_pin_disabled);
	}

	@Override
	public int getTabTitleResourceId() {
		return R.string.fragment_general_info_general_info_tab_title;
	}

}
