package com.mdareports.ui.fragments.reportslists;

import java.util.List;

import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.models.Report;


public class UnreadReportsFragment extends BaseReportsListFragment{

	@Override
	public List<Report> getReports()
	{
		return DatabaseWrapper.getInstance(getActivity()).getUnreadReports();		
	}
}
