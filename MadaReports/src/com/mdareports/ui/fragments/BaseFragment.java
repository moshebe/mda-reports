package com.mdareports.ui.fragments;


import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

	protected void changeTitle(int resIdTitle){
		getActivity().setTitle(resIdTitle);
	}
	
}
