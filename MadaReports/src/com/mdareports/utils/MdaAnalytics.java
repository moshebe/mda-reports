package com.mdareports.utils;

import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class MdaAnalytics {
	
	public static final String EVENT_CATEGORY_SMS = "Sms";
	public static final String EVENT_CATEGORY_REPORT = "Report";
	
	/**
	 * Get an instance of the EasyTracker
	 * @param context
	 * @return
	 */
	private static EasyTracker getEasyTrackerInstance(Context context) {
		return EasyTracker.getInstance(context);
	}
	
	/**
	 * Create an event of Google Analytics
	 * @param context
	 * @param eventCategory
	 * @param eventAction
	 * @param eventLabel
	 * @param eventValue
	 */
	public static void createEvent(Context context, String eventCategory, String eventAction, String eventLabel, Long eventValue) {
		// Create the map of parameters for the event
		Map<String, String> eventParams = MapBuilder.createEvent(eventCategory,
				eventAction, eventLabel, eventValue).build();
		
		// Send the Event
		getEasyTrackerInstance(context).send(eventParams);
	}
	
	/**
	 * Activity start event
	 * @param activity
	 */
	public static void activityStart(Activity activity) {
		getEasyTrackerInstance(activity).activityStart(activity);
	}
	
	/**
	 * Activity stop event
	 * @param activity
	 */
	public static void activityStop(Activity activity) {
		getEasyTrackerInstance(activity).activityStop(activity);
	}
	
	public static void smsReceivedEvent(Context context) {
		createEvent(context, EVENT_CATEGORY_SMS, "Received", "Raise notification", null);
	}
	
	public static void reportSaved(Context context) {
		createEvent(context, EVENT_CATEGORY_REPORT, "Saved", "Save Button", null);
	}
	
	public static void reportRollback(Context context) {
		createEvent(context, EVENT_CATEGORY_REPORT, "Rollback", "Save Button", null);
	}
	
}