package com.mdareports.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import com.mdareports.R;

/**
 * Wrapper for the SharedPreferenceAdaper for easily get the saved values.
 * Implements the Single-tone design pattern.
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SettingsManager {
	private static SettingsManager instance;
	private Context cntx;

	/**
	 * Get instance of the class. implements the singleton design pattern.
	 * 
	 * @return instance of the class to work with
	 */
	public static SettingsManager getInstance(Context context) {
		if (instance == null) {
			instance = new SettingsManager(context);
		}
		return instance;
	}

	/**
	 * Constructs the session manager object with context. The context should be
	 * the activity that holds the operations.
	 */
	private SettingsManager(Context context) {
		this.cntx = context;

		PreferenceManager.setDefaultValues(cntx, R.xml.preferences_settings,
				false);
	}

	/**
	 * Get instance of the SharedPreferenceAdapter
	 */
	private SharedPreferenceAdapter getAdapterInstance() {
		return SharedPreferenceAdapter.getInstance(cntx);
	}

	/**
	 * Getting the URI of the chosen ringtone. if no ringtone has been chosen
	 * the ALARM_ALERT is returned
	 */
	public android.net.Uri getRingtoneUri() {
		String defaultRingtone = RingtoneManager.getDefaultUri(
				RingtoneManager.TYPE_ALARM).toString();

		String valueFromPref = getAdapterInstance().readString(
				R.string.prefrences_key_ringtone_uri, defaultRingtone);
		return Uri.parse(valueFromPref);
	}

	public void setRingtoneUri(Uri ringtoneUri) {
		getAdapterInstance().writeString(R.string.prefrences_key_ringtone_uri,
				ringtoneUri.toString());
	}

	/**
	 * Whether to perform vibration when the time is up
	 */
	public boolean isVibrateEnabled() {
		return getAdapterInstance().readBoolean(
				R.string.prefrences_key_vibrate_enabled);
	}

	/**
	 * Whether to play sound when the time is up. in case that this option
	 * disabled the getRingtoneUri() if irrelevant
	 */
	public boolean isSoundEnabled() {
		return getAdapterInstance().readBoolean(
				R.string.prefrences_key_sound_enabled);
	}

	/**
	 * Save the information of the reports sender (m.d.a specific number)
	 * 
	 * @param displayName
	 *            - The name of the reports sender in the the contacts
	 * @param numbers
	 *            - The numbers of the sender separated by ';'. should be only
	 *            one number but this was made for treating the case of multiple
	 *            report senders
	 */
	public void setReportsSender(String displayName, String numbers) {
		SharedPreferenceAdapter spAdapter = getAdapterInstance();
		spAdapter.writeString(R.string.prefrences_key_sender_name, displayName);
		spAdapter.writeString(R.string.prefrences_key_sender_numbers,
				displayName);
	}

	/**
	 * Get the display name of the reports sender
	 */
	public String getReportsSenderName() {
		return getAdapterInstance().readString(
				R.string.prefrences_key_sender_name);
	}

	/**
	 * Check if the inputed number is included in the reports-sender phone
	 * numbers
	 * 
	 * @param number
	 *            - the number to be checked
	 * @return True if the reports-sender own this numbers, False otherwise
	 */
	public boolean isReportsSenderNumber(String number) {
		String[] numbers = getAdapterInstance().readString(
				R.string.prefrences_key_sender_numbers).split(";");
		if (numbers.length > 0) {
			for (String phoneNumber : numbers) {
				if (android.telephony.PhoneNumberUtils.compare(phoneNumber,
						number))
					return true;
			}
		}
		return false;
	}

	/**
	 * Check if the application should abort the broadcast when the phone
	 * getting SMS
	 * 
	 * @return true if should abort, otherwise false
	 */
	public boolean getAbortBroadcast() {
		return getAdapterInstance().readBoolean(
				R.string.prefrences_key_abort_broadcast, false);
	}

	/**
	 * Get the message that was received until now. Treating the situation where
	 * there is fragmentation of messages and the SMS's received as 1/2, 2/2
	 * etc.
	 * 
	 * @return The messages that was received as one string.
	 */
	public String getBufferedMessage() {
		String readString = getAdapterInstance().readString("bufferedMessage",
				"");
		// TODO: remove this line - unnecessary. but check a lot before publish
		// !
		readString = readString == null ? "" : readString;
		return readString;
	}

	/**
	 * Save the combination of messages that was received. Treating the
	 * situation where there is fragmentation of messages and the SMS's received
	 * as 1/2, 2/2 etc.
	 * 
	 * @param value
	 *            - Combined string of all the messages contents.
	 */
	public void setBufferedMessage(String value) {
		getAdapterInstance().writeString("bufferedMessage", value);
	}

	/**
	 * Get the volunteer signature contains its details for exporting (share
	 * etc.)
	 * 
	 * @return If any of the details was supplied - the volunteer signature,
	 *         Otherwise an empty string
	 */
	public String getVolunteerSignature() {
		String vlntrId = getVolunteerId();
		String vlntrName = getVolunteerName();
		String vlntrMirs = getVolunteerMirs();

		String resultSignature = "";

		if (!(vlntrId.isEmpty() && vlntrName.isEmpty() && vlntrMirs.isEmpty())) {
			// at least one of the details is not empty
			Resources resources = cntx.getResources();
			resultSignature += "\n\n";
			if (!vlntrId.isEmpty())
				resultSignature += resources
						.getString(R.string.prefrences_title_volunteer_id)
						+ " : " + vlntrId + "\n";

			if (!vlntrName.isEmpty())
				resultSignature += resources
						.getString(R.string.prefrences_title_volunteer_name)
						+ " : " + vlntrName + "\n";

			if (!vlntrMirs.isEmpty())
				resultSignature += resources
						.getString(R.string.prefrences_title_volunteer_mirs)
						+ " : " + vlntrMirs + "\n";

		}

		Logger.LOGE("getVolunteerSignature", resultSignature);
		return resultSignature;

	}

	/** Volunteer ID */
	public String getVolunteerId() {
		return getAdapterInstance().readString(
				R.string.prefrences_key_volunteer_id, "");
	}

	public void setVolunteerId(String volunteerId) {
		getAdapterInstance().writeString(R.string.prefrences_key_volunteer_id,
				volunteerId);
	}

	/** Volunteer Name */
	public String getVolunteerName() {
		return getAdapterInstance().readString(
				R.string.prefrences_key_volunteer_name, "");
	}

	public void setVolunteerName(String volunteerName) {
		getAdapterInstance().writeString(
				R.string.prefrences_key_volunteer_name, volunteerName);
	}

	/** Volunteer MIRS */
	public String getVolunteerMirs() {
		return getAdapterInstance().readString(
				R.string.prefrences_key_volunteer_mirs, "");
	}

	public void setVolunteerMirs(String volunteerMirs) {
		getAdapterInstance().writeString(
				R.string.prefrences_key_volunteer_mirs, volunteerMirs);
	}

	/** Spatial Telephony Center */
	public String getSpatialTelephonyCenterNumber() {
		return getAdapterInstance().readString(
				R.string.prefrences_key_spatial_telephony_center_number, "");
	}

	public void setSpatialTelephonyCenterNumber(String number) {
		getAdapterInstance()
				.writeString(
						R.string.prefrences_key_spatial_telephony_center_number,
						number);
	}

	/** Help */
	private final String HAS_SEEN_BASE_KEY ="HAS_SEEN_";
	
	public boolean hasSeenHelp(Class<?> cls) {			
		return getAdapterInstance().readBoolean(HAS_SEEN_BASE_KEY + cls.getName(), false);
	}
	
	public void setSeenHelp(Class<?> cls){
		getAdapterInstance().writeBoolean(HAS_SEEN_BASE_KEY + cls.getName(), true);
	}
	
		

}