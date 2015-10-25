package com.mdareports.ui.activities.details;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.mdareports.ui.fragments.details.BaseDetailFragment;
import com.mdareports.ui.fragments.details.GeneralInfoFragment;
import com.mdareports.ui.fragments.details.TechInfoFragment;
import com.mdareports.ui.fragments.details.TreatmentsToReportFragment;

public class MdaPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<BaseDetailFragment> tabPages;

	public MdaPagerAdapter(FragmentManager fm, Context context) {
		super(fm);

		tabPages = new ArrayList<BaseDetailFragment>();

		tabPages.add(new GeneralInfoFragment());
		tabPages.add(new TechInfoFragment());
		tabPages.add(new TreatmentsToReportFragment());

	}

	public void initActionBar(ActionBar supportActionBar, ViewPager viewPager) {
		for (BaseDetailFragment tabPage : tabPages) {

			supportActionBar.addTab(supportActionBar.newTab()
					.setText(tabPage.getTabTitleResourceId())
					.setTabListener(new MadaTabListener(viewPager)));
		}
	}

	public void saveAllTabs() {
		for (BaseDetailFragment baseDetailsFragment : tabPages) {
			// TODO: treatments throws exceptions. check why
			try {
				baseDetailsFragment.saveCurrentReport();
			} catch (Exception e) {

			}
		}
	}

	public void refreshAllTabs() {
		for (BaseDetailFragment baseDetailsFragment : tabPages) {
			// TODO: treatments throws exceptions. check why
			try {
				baseDetailsFragment.refreshDataWithCurrentReport();
			} catch (Exception e) {

			}
		}
	}

	@Override
	public Fragment getItem(int position) {
		return tabPages.get(position);
	}

	@Override
	public int getCount() {
		return tabPages.size();
	}

}
