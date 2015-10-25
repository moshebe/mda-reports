package com.mdareports.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.mdareports.R;
import com.mdareports.utils.MdaAnalytics;

public class OldSettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_settings);
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		MdaAnalytics.activityStart(this);
	}
	
	@Override
	protected void onStop() {	
		super.onStop();
		MdaAnalytics.activityStop(this);
	}
	
	
		



}