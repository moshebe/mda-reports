package com.mdareports.ui.activities;

import com.mdareports.R;
import com.mdareports.db.codetables.ICodeTable;
import com.mdareports.db.models.Treatment;

public class TreatmentsActivity extends CodeTableBaseActivity {

	public TreatmentsActivity() {
		super.resIdDialogTitle = R.string.code_table_dialog_treatment_title;
	}

	@Override
	protected ICodeTable generateRecord(String content) {
		return new Treatment(content);
	}

}
