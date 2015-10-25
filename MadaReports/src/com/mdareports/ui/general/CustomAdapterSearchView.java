package com.mdareports.ui.general;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class CustomAdapterSearchView extends SearchView {

	private SearchView.SearchAutoComplete mSearchAutoComplete;	

	public CustomAdapterSearchView(Context context) {
		super(context);
		initialize();
	}

	public CustomAdapterSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public void initialize() {
		mSearchAutoComplete = (SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
		this.setAdapter(null);
		this.setOnItemClickListener(null);		
		
		mSearchAutoComplete.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					mSearchAutoComplete.showDropDown();					
				}				
			}
		});
	}

	@Override
	public void setSuggestionsAdapter(CursorAdapter adapter) {
		// don't let anyone touch this
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mSearchAutoComplete.setOnItemClickListener(listener);
	}

	public void setAdapter(ArrayAdapter<?> adapter) {
		mSearchAutoComplete.setAdapter(adapter);
	}

	public void setText(String text){
		mSearchAutoComplete.setText(text);
	}

	
	
}