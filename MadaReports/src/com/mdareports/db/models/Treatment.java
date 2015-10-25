package com.mdareports.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mdareports.db.codetables.ICodeTable;

@DatabaseTable(tableName = Treatment.TABLE_NAME)
public class Treatment implements ICodeTable {
	
	public static final String TABLE_NAME = "treatments";
	public static final String TREATMENT_ID_COLUMN_NAME = "id";
	
	@DatabaseField(generatedId = true, columnName = TREATMENT_ID_COLUMN_NAME)
	private int id;
	@DatabaseField
	private String treatment;

	public Treatment() {

	}
	
	public Treatment(String treatment) {
		setTreatment(treatment);
	}
	
	public Treatment(int treatmentId) {
		setId(treatmentId);
	}

	// Setters & Getters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTreatment() {
		return this.treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	/**
	 * String representation for the list-view display
	 */
	@Override
	public String toString() {
		return getContent();
	}

	public String getContent() {
		return getTreatment();
	}

	@Override
	public void setContent(String content) {
		setTreatment(content);
	}

}
