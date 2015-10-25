package com.mdareports.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = TreatmentsToReports.TABLE_NAME)
public class TreatmentsToReports {
	
	public static final String TABLE_NAME = "treatmentsToReports";
	public static final String ID_COLUMN_NAME = "id";
	public static final String REPORT_ID_COLUMN_NAME = "reportId";
	public static final String TREATMENT_ID_COLUMN_NAME = "treatmentId";
	
	@DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)	
	private int id;
	@DatabaseField(foreign=true, columnName = REPORT_ID_COLUMN_NAME)
	private Report report;
	@DatabaseField(foreign=true, columnName = TREATMENT_ID_COLUMN_NAME)
	private Treatment treatment;
	
	public TreatmentsToReports() {
		
	}
	
	public TreatmentsToReports(Report report, Treatment treatment) {
		this.report = report;
		this.treatment = treatment;
	}
	
	// Setters and Getters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
}
