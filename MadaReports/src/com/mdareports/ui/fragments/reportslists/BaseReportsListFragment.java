package com.mdareports.ui.fragments.reportslists;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.DbChangedNotifier;
import com.mdareports.db.models.Report;
import com.mdareports.ui.fragments.BaseFragment;
import com.mdareports.ui.reportslist.ReportsListCardAdapter;
import com.mdareports.utils.AnimatedQuickReturnListView;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.FontTypeFaceManager;
import com.mdareports.utils.FontTypeFaceManager.CustomFonts;
import com.mdareports.utils.NotificationsManager;
import com.mdareports.utils.SettingsManager;

public abstract class BaseReportsListFragment extends BaseFragment implements DbChangedNotifier {

	protected ReportsListCardAdapter reportsAdapter;
	protected AnimatedQuickReturnListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(
				R.layout.fragment_reports_list_view, container, false);

		listView = (AnimatedQuickReturnListView) rootView.findViewById(android.R.id.list);

		// set the empty list view
		View empty = rootView.findViewById(android.R.id.empty);
		
		// set the view's text font in case of non-hebrew version
		if (!DeviceInfoUtils.isCurrentLanguageHebrew(getActivity())) {
			FontTypeFaceManager.getInstance(getActivity()).setFont(
					(TextView) empty.findViewById(R.id.emptyViewLabel),
					CustomFonts.RobotoThin);
		}		
		listView.setEmptyView(empty);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
		
		listView.initialize(getActivity().findViewById(R.id.reportsListLegend));
		
	}
	
	@Override
	public void onResume() {
		super.onResume();

		reportsAdapter = new ReportsListCardAdapter(getActivity(), getReports());
		listView.setAdapter(reportsAdapter);

		// Register for DbChangedListener
		DatabaseWrapper.getInstance(getActivity()).setDbChangedListener(this);
		
		// Just remove the SMS Received Notification
		NotificationsManager.getInstance(getActivity())
				.removeSmsReceivedNotification();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		// Unregister from the DbChangedListener
		DatabaseWrapper.getInstance(getActivity()).removeDbChangedListener(this);
	}

	@Override
	public void DbChanged() {
		reportsAdapter.updateReports(getReports());
	}

	public abstract List<Report> getReports();

	public void removeDisplayedReportes() {
		Report[] reprots = reportsAdapter.getReports();
		if (DatabaseWrapper.getInstance(getActivity()).deleteReports(reprots)) {
			Toast.makeText(getActivity(), R.string.deleted_successfuly,
					Toast.LENGTH_SHORT).show();
		}
	}

	public Intent getShareIntent() {
		StringBuffer shareString = new StringBuffer();
		List<String> reportsOnScreen = reportsAdapter
				.getToShareStringOfReports();
		for (String currentReport : reportsOnScreen) {
			shareString.append(currentReport);
			shareString.append("\n\n");
		}
		shareString.append(SettingsManager.getInstance(getActivity())
				.getVolunteerSignature());

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareString.toString());
		shareIntent.setType("text/plain");

		return shareIntent;
	}

	public ReportsListCardAdapter getAdapter() {
		return reportsAdapter;
	}

}
