package com.mdareports.ui.fragments.details;

import java.util.List;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.models.Treatment;
import com.mdareports.db.models.TreatmentsToReports;

public class TreatmentsToReportFragment extends BaseDetailFragment {

	private static final Integer ADD = 1;
	private static final Integer DELETE = 2;

	// Lists of the Treatments of the currentReport and of the Treatments that
	// are note connected the the currentReport
	List<Treatment> allCurrentReportTreatments;
	List<Treatment> allOtherTreatmentsToReports;

	// Lists of TreatmentsToReports for adding to the DB or deleting from the DB
	// when the user click on the save button
	SparseIntArray treatmentsIdToAdd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize the list of the treatments to show to the user
		// This initialization is for the first time the fragment created -->
		// when the user enter to the DetailActivity
		// afterwards, onCreate is not called, just the
		// onCreateView-->onStart-->etc.
		allCurrentReportTreatments = getAllCurrentReportTreatments();
		allOtherTreatmentsToReports = getAllOtherTreatmentsToReports();

		// Initialize the TreatmentsToReports lists
		treatmentsIdToAdd = new SparseIntArray();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_treatments_to_report,
				container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		// Init the listView of all the treatments
		ListView allTreatmentsListView = (ListView) getActivity().findViewById(
				R.id.all_treatments_list);
		final ArrayAdapter<Treatment> allTreatmentsListViewAdapter = getArrayAdapter(allOtherTreatmentsToReports);
		allTreatmentsListView.setAdapter(allTreatmentsListViewAdapter);

		// Init the listView of treatments of the current report
		ListView treatmentsOfReportListView = (ListView) getActivity()
				.findViewById(R.id.treatments_of_report_list);
		final ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = getArrayAdapter(allCurrentReportTreatments);
		treatmentsOfReportListView
				.setAdapter(treatmentsOfReportListViewAdapter);

		// Init the onItemClickListeners
		allTreatmentsListView
				.setOnItemClickListener(getAllTreatmentsListViewOnClickListener());
		treatmentsOfReportListView
				.setOnItemClickListener(getTreatmentsOfReportListViewOnClickListener());
	}

	/**
	 * Find all the Treatments of the currentReport
	 * 
	 * @return List of all the Treatments of the currentReport
	 */
	private List<Treatment> getAllCurrentReportTreatments() {
		int currentReportId = getCurrentReport().getId();
		List<Treatment> treatmentsByReportId = DatabaseWrapper.getInstance(
				getActivity()).getTreatmentsByReportId(currentReportId);

		return treatmentsByReportId;
	}

	/**
	 * Find all the Treatments that are not connected to the currentReport
	 * 
	 * @return List of all the Treatments that are not connected to the
	 *         currentReport
	 */
	private List<Treatment> getAllOtherTreatmentsToReports() {
		return DatabaseWrapper.getInstance(getActivity())
				.getAllOtherTreatments(getCurrentReport().getId());
	}

	private OnItemClickListener getAllTreatmentsListViewOnClickListener() {
		return new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Find the selected Treatment
				Treatment selectedTreatment = (Treatment) parent
						.getItemAtPosition(position);

				// Add the TreatmentsToReports row to the add list
				treatmentsIdToAdd.put(selectedTreatment.getId(), ADD);

				ListView allTreatmentsListView = (ListView) getActivity()
						.findViewById(R.id.all_treatments_list);
				ArrayAdapter<Treatment> allTreatmentsListViewAdapter = (ArrayAdapter<Treatment>) allTreatmentsListView
						.getAdapter();
				ListView treatmentsOfReportListView = (ListView) getActivity()
						.findViewById(R.id.treatments_of_report_list);
				ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = (ArrayAdapter<Treatment>) treatmentsOfReportListView
						.getAdapter();

				// update UI
				allTreatmentsListViewAdapter.remove(selectedTreatment);
				treatmentsOfReportListViewAdapter.add(selectedTreatment);
			}
		};
	}

	private OnItemClickListener getTreatmentsOfReportListViewOnClickListener() {
		return new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Find the selected Treatment
				Treatment selectedTreatment = (Treatment) parent
						.getItemAtPosition(position);

				treatmentsIdToAdd.put(selectedTreatment.getId(), DELETE);

				ListView allTreatmentsListView = (ListView) getActivity()
						.findViewById(R.id.all_treatments_list);
				ArrayAdapter<Treatment> allTreatmentsListViewAdapter = (ArrayAdapter<Treatment>) allTreatmentsListView
						.getAdapter();
				ListView treatmentsOfReportListView = (ListView) getActivity()
						.findViewById(R.id.treatments_of_report_list);
				ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = (ArrayAdapter<Treatment>) treatmentsOfReportListView
						.getAdapter();

				// update UI
				treatmentsOfReportListViewAdapter.remove(selectedTreatment);
				allTreatmentsListViewAdapter.add(selectedTreatment);

			}
		};
	}

	/**
	 * Create {@link ArrayAdapter} to the ListView in the tab.
	 * 
	 * @param treatments
	 *            List of Treatments for the adapter
	 * @return {@link ArrayAdapter} to the ListViews in the tab
	 */
	private ArrayAdapter<Treatment> getArrayAdapter(List<Treatment> treatments) {
		return new ArrayAdapter<Treatment>(getActivity(),
				android.R.layout.simple_list_item_1, treatments);
	}

	@Override
	public void saveCurrentReport() {
		int size = treatmentsIdToAdd.size();
		DatabaseWrapper databaseWrapper = DatabaseWrapper
				.getInstance(getActivity());
		for (int i = 0; i < size; i++) {
			int treatmentId = treatmentsIdToAdd.keyAt(i);
			int state = treatmentsIdToAdd.get(treatmentId);
			if (state == ADD) {
				TreatmentsToReports newTreatmentsToReports = new TreatmentsToReports(
						getCurrentReport(), new Treatment(treatmentId));
				databaseWrapper.createTreatmentToReport(newTreatmentsToReports);
			} else if (state == DELETE) {
				databaseWrapper.deleteTreatmentsToReportByReportAndTreatmentId(
						getCurrentReport().getId(), treatmentId);
			}
		}
	}

	@Override
	public void refreshDataWithCurrentReport() {
		allCurrentReportTreatments = getAllCurrentReportTreatments();
		allOtherTreatmentsToReports = getAllOtherTreatmentsToReports();
		treatmentsIdToAdd = new SparseIntArray();

		// Init the listView of all the treatments
		ListView allTreatmentsListView = (ListView) getActivity().findViewById(
				R.id.all_treatments_list);
		final ArrayAdapter<Treatment> allTreatmentsListViewAdapter = getArrayAdapter(allOtherTreatmentsToReports);
		allTreatmentsListView.setAdapter(allTreatmentsListViewAdapter);

		// Init the listView of treatments of the current report
		ListView treatmentsOfReportListView = (ListView) getActivity()
				.findViewById(R.id.treatments_of_report_list);
		final ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = getArrayAdapter(allCurrentReportTreatments);
		treatmentsOfReportListView
				.setAdapter(treatmentsOfReportListViewAdapter);

		// Init the onItemClickListeners
		allTreatmentsListView
				.setOnItemClickListener(getAllTreatmentsListViewOnClickListener());
		treatmentsOfReportListView
				.setOnItemClickListener(getTreatmentsOfReportListViewOnClickListener());
	}

	@Override
	public int getTabTitleResourceId() {
		return R.string.fragment_treatments_to_report_title;
	}
}