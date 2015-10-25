package com.mdareports.utils;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.TypedValue;

public class ApplicationUtils {
	
	/**
	 * Check if the current application is in the foreground
	 * @param appContext
	 * @return
	 */
	public static boolean isApplicationInForeground(Context appContext) {
		ActivityManager activityManager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> services = activityManager
	            .getRunningTasks(Integer.MAX_VALUE);
	    boolean isActivityFound = false;

	    if (services.get(0).topActivity.getPackageName().toString()
	            .equalsIgnoreCase(appContext.getPackageName().toString())) {
	        isActivityFound = true;
	    }

	    return isActivityFound;
	}

	/**
	 * Get a String object and return an empty string if the String==null
	 * @param string The string to check
	 * @return Empty string if the String object is null. otherwise return the original string 
	 */
	public static String NVL(String string) {
		if(string == null) {
			return "";
		}
		
		return string;
	}
	
	/**
	 * Get "New Task" flags. Used by the {@link NotificationsManager}
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getNewTaskFlags() {
		if (DeviceInfoUtils.hasHoneycomb()) {
			return Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK;
		}
		return Intent.FLAG_ACTIVITY_NEW_TASK;
	}
	
	/**
	 * Get dpi value from pixels.
	 * @param pixels 
	 * @param context
	 * @return
	 */
	public static int getDpiFromInteger(int pixels, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, context.getResources().getDisplayMetrics());		
	}
}
