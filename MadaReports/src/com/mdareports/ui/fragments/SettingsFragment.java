package com.mdareports.ui.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import com.mdareports.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);	
		
		getActivity().setTitle(R.string.title_settings);
		
		 addPreferencesFromResource(R.xml.preferences_settings);
	}
	
}
