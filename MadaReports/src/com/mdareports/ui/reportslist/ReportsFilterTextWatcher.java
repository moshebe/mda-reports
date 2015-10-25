package com.mdareports.ui.reportslist;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Implies the 'real time' filtering on the edit-text
 */
public class ReportsFilterTextWatcher implements TextWatcher {
	private ReportsListCardAdapter adapter;

	public ReportsFilterTextWatcher(ReportsListCardAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onTextChanged(CharSequence cs, int start, int before, int count) {

		if (count < before) {
			// We're deleting char so we need to reset the adapter data
			adapter.resetData();
		}
		// When user changed the Text
		adapter.getFilter().filter(cs);
	}

	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

}
