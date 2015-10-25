package com.mdareports.ui.activities.details;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

public class MadaTabListener implements TabListener {
	private ViewPager viewPager;

    public MadaTabListener(ViewPager viewPager) {
    	this.viewPager = viewPager;
    }

    /* The following are each of the ActionBar.TabListener callbacks */
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}
