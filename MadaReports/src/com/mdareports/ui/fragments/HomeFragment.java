package com.mdareports.ui.fragments;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;

public class HomeFragment extends BaseFragment {
	private BarGraph graph;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		changeTitle(R.string.app_name);

		final View rootView = inflater.inflate(R.layout.fragment_home,
				container, false);

		// Set the bars in the Graph object
		graph = (BarGraph) rootView.findViewById(R.id.graph);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		DatabaseWrapper databaseWrapper = DatabaseWrapper
				.getInstance(getActivity());
		Resources resources = getResources();
		int countAllReports = databaseWrapper.countAllReports();
		int countUnreadReports = databaseWrapper.countUnreadReports();
		int countUnreportedReports = databaseWrapper.countUnreportedReports();

		// Init the bars and the points
		ArrayList<Bar> points = new ArrayList<Bar>();
		Bar allReportsBar = new Bar();
		allReportsBar.setColor(resources
				.getColor(R.color.bar_color_all_reports));
		allReportsBar.setName(resources.getString(R.string.bar_title_all));
		allReportsBar.setValue(countAllReports);
		Bar unreadReportsBar = new Bar();
		unreadReportsBar.setColor(resources
				.getColor(R.color.bar_color_unread_reports));
		unreadReportsBar
				.setName(resources.getString(R.string.bar_title_unread));
		unreadReportsBar.setValue(countUnreadReports);
		Bar unreportedBar = new Bar();
		unreportedBar.setColor(resources
				.getColor(R.color.bar_color_unreported_reports));
		unreportedBar.setName(resources
				.getString(R.string.bar_title_unreported));
		unreportedBar.setValue(countUnreportedReports);

		// Add the points of the bars
		points.add(allReportsBar);
		points.add(unreadReportsBar);
		points.add(unreportedBar);

		graph.setBars(points);
	}
}
