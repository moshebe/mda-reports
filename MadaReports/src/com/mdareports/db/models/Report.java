package com.mdareports.db.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.reports.ReportAnalyzer;
import com.mdareports.utils.ApplicationUtils;

@DatabaseTable(tableName = Report.TABLE_NAME)
public class Report {

	public static final String TABLE_NAME = "reports";
	public static final String ID_COLUMN_NAME = "id";
	public static final String IS_READ_COLUMN_NAME = "isRead";
	public static final String IS_REPORTED_COLUMN_NAME = "isReported";

	@DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)
	private int id;
	@DatabaseField
	private int reportId;
	@DatabaseField
	private String address;
	@DatabaseField
	private String description;
	@DatabaseField
	private int pulse;
	@DatabaseField
	private int minBloodPressure;
	@DatabaseField
	private int maxBloodPressure;
	@DatabaseField
	private int breath;
	@DatabaseField
	private int sugar;
	@DatabaseField
	private String notes;
	@DatabaseField
	private boolean isReported; // if the user report this record on the website
	@DatabaseField
	private boolean isRead; // if the user saw this report. NOTE: Dont change
							// the variable name. the countNewReports() rely
							// on this name right now.
	@DatabaseField
	private Date receivedAt;
	@DatabaseField
	private String location; // latitue;longtitude
	@DatabaseField
	private String originalMessage;

	public Report(Context ctx, String messageBody, long timesptamp) {
		// Simulate the description manually. for debug uses
		// MessageBody = new ReportIllustrator().getFakeReport();
		ReportAnalyzer rprtAnlzr = new ReportAnalyzer(messageBody);

		// set info of the message in the Report object
		setReportId(rprtAnlzr.getId());
		setDescription(rprtAnlzr.getDescription());
		setAddress(rprtAnlzr.getAddress());
		setOriginalMessage(messageBody);

		// set random things for debugging
		setReceivedAt(new Date(timesptamp));
		setPulse(0);
		setBreath(0);
		setSugar(0);
		setMinBloodPressure(0);
		setMaxBloodPressure(50);
		setRead(false);
		setLocation("");
	}

	public Report() {
		super();
	}

	/**
	 * Get string representation for sharing
	 * 
	 * @param context
	 *            - context for getting the string resources
	 * @return string represents the report for the share description
	 */
	public String toShareString(Context context) {
		StringBuilder shareMessage = new StringBuilder();

		// get the resources for the strings
		Resources resources = context.getResources();

		// general details
		appendLine(shareMessage,
				resources.getString(R.string.share_title_general_details)
						+ " #" + getReportId(), "", resources);
		appendLine(shareMessage, R.string.fragment_general_info_address,
				getAddress(), resources);
		appendLine(shareMessage, R.string.fragment_general_info_description,
				getDescription(), resources);
		// shareMessage.append(new
		// SimpleDateFormat("E dd-MM-yyyy hh:mm").format(getReceivedAt()).toString()
		// + "\n");
		shareMessage.append(SimpleDateFormat.getDateTimeInstance()
				.format(getReceivedAt()).toString()
				+ "\n");

		// Separator
		shareMessage.append("\n");

		// technical details
		appendLine(shareMessage, R.string.share_title_technical_details, "",
				resources);
		appendLine(shareMessage, R.string.fragment_tech_info_pulse, getPulse(),
				resources);
		appendLine(shareMessage, R.string.fragment_tech_info_sugar, getSugar(),
				resources);
		appendLine(shareMessage, R.string.fragment_tech_info_breath,
				getBreath(), resources);
		appendLine(shareMessage, R.string.fragment_tech_info_blood_pressure,
				getMinBloodPressure() + " \\ " + getMaxBloodPressure(),
				resources);

		// Separator
		shareMessage.append("\n");

		// treatment
		List<Treatment> treatments = DatabaseWrapper.getInstance(context)
				.getTreatmentsByReportId(id);
		appendLine(shareMessage, R.string.share_title_treatments, "",
				resources);
		for (Treatment treatment : treatments) {
			shareMessage.append(treatment + "\n");
		}

		// Separator
		shareMessage.append("\n");

		appendLine(shareMessage, R.string.fragment_general_info_notes,
				getNotes(), resources);

		return shareMessage.toString();
	}

	private void appendLine(StringBuilder sb, int keyResourceId, int value,
			Resources resources) {
		appendLine(sb, keyResourceId, value + "", resources);
	}

	private void appendLine(StringBuilder sb, int keyResourceId, String value,
			Resources resources) {
		sb.append(resources.getString(keyResourceId) + ": " + value + "\n");
	}

	private void appendLine(StringBuilder sb, String key, String value,
			Resources resources) {
		sb.append(key + ": " + value + "\n");
	}

	// Setters & Getters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getAddress() {
		return ApplicationUtils.NVL(address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return ApplicationUtils.NVL(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public int getMinBloodPressure() {
		return minBloodPressure;
	}

	public void setMinBloodPressure(int minBloodPressure) {
		this.minBloodPressure = minBloodPressure;
	}

	public int getMaxBloodPressure() {
		return maxBloodPressure;
	}

	public void setMaxBloodPressure(int maxBloodPressure) {
		this.maxBloodPressure = maxBloodPressure;
	}

	public int getBreath() {
		return breath;
	}

	public void setBreath(int breath) {
		this.breath = breath;
	}

	public int getSugar() {
		return sugar;
	}

	public void setSugar(int sugar) {
		this.sugar = sugar;
	}

	public String getNotes() {
		return ApplicationUtils.NVL(notes);
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isReported() {
		return isReported;
	}

	public void setReported(boolean isReported) {
		this.isReported = isReported;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Date getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Date receivedAt) {
		this.receivedAt = receivedAt;
	}

	public String getOriginalMessage() {
		return ApplicationUtils.NVL(originalMessage);
	}

	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}
	
	public LatLng getLocation() {
		String strLocation = ApplicationUtils.NVL(location);
		String[] parts = strLocation.split(";");
		try {
			return new LatLng(Double.parseDouble(parts[0]),
					Double.parseDouble(parts[1]));
		} catch (Exception e) {
			return null;
		}
	}

	public boolean hasLocation(){
		return (getLocation() != null);
	}
	
	public void setLocation(String loc) {
		location = loc;
	}

	public void setLocation(LatLng loc) {
		if (loc != null) {
			location = "" + loc.latitude + ";" + loc.longitude;
		} else {
			location = "";
		}
	}

}
