package com.mdareports.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

public class DeviceInfoUtils {

    public static boolean isGoogleTV(Context context) {
    	return context.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }
    
    /**
     * Get the Screen Densitity</br>
     * Check with {@link DisplayMetrics} constants: DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH.
     * @param context
     * @return Screen Density of the device
     */
    public static float getScreenDensity(Context context) {
    	DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    	return metrics.densityDpi;
    }
	
    /**
     * Get the screen size of the device. </br>
     * Check with {@link Configuration} constants: SCREENLAYOUT_SIZE_LARGE, SCREENLAYOUT_SIZE_NORMAL or SCREENLAYOUT_SIZE_SMALL
     * @param context
     * @return Screen size of the device
     */
    public static int getScreenSize(Context context) {
    	int screenSize = context.getResources().getConfiguration().screenLayout &
    	        Configuration.SCREENLAYOUT_SIZE_MASK;
    	
    	return screenSize;
    }
    
    /**
     * Get the language of the device
     * @param context
     * @return 
     */
	public static String getDeviceLanguage(Context context) {
		String displayName = context.getResources().getConfiguration().locale.getLanguage();

		return displayName;
	}
	
	/*
	 * Checks if the current language running on the device is hebrew
	 */
	public static boolean isCurrentLanguageHebrew(Context context){
		return getDeviceLanguage(context).equals("iw");
	}
    
    // TODO
    /*
     * write methods to check about having camera, vibration option, isOnline, has external sd card etc. 
     */
}
