package com.mdareports.utils;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

public class FontTypeFaceManager {
	public enum CustomFonts {
		RobotoLight, RobotoRegular, RobotoThin, 
		YoavRegular, YoavBold
	};

	private HashMap<CustomFonts, Typeface> fontsMap;
	private static FontTypeFaceManager instance;
	private Typeface defaultFont;

	private FontTypeFaceManager(Context context) {
		fontsMap = new HashMap<FontTypeFaceManager.CustomFonts, Typeface>();

		fontsMap.put(CustomFonts.RobotoLight, Typeface.createFromAsset(
				context.getAssets(), "fonts/Roboto-Light.ttf"));
		fontsMap.put(CustomFonts.RobotoRegular, Typeface.createFromAsset(
				context.getAssets(), "fonts/Roboto-Regular.ttf"));
		fontsMap.put(CustomFonts.RobotoThin, Typeface.createFromAsset(					
				context.getAssets(), "fonts/Roboto-Thin.ttf"));
		
		// hebrew fonts
		fontsMap.put(CustomFonts.YoavBold, Typeface.createFromAsset(
				context.getAssets(), "fonts/heb/YOAV_BOLD.TTF"));
		fontsMap.put(CustomFonts.YoavRegular, Typeface.createFromAsset(
				context.getAssets(), "fonts/heb/YOAV_REGULAR.TTF"));


		defaultFont = fontsMap.get(CustomFonts.RobotoRegular);
	}

	public static FontTypeFaceManager getInstance(Context context) {
		if (instance == null)
			instance = new FontTypeFaceManager(context);
		return instance;
	}

	/**
	 * Set the default font to the inputed text views
	 * 
	 * @param ids
	 *            - the resource ids of the text views that should be applied
	 *            with this font type face
	 */
	public void setDetaultFont(Activity activity, int... ids) {
		TextView[] tvs = new TextView[ids.length];

		// convert the ids to text views
		for (int i = 0; i < ids.length; i++) {
			tvs[i] = (TextView) activity.findViewById(ids[i]);
		}

		setDetaultFont(tvs);
	}

	/**
	 * Set the default font to the inputed text views
	 * 
	 * @param tvs
	 *            - the text views that should be applied with this font type
	 *            face
	 */
	public void setDetaultFont(TextView... tvs) {
		for (TextView tv : tvs) {
			tv.setTypeface(defaultFont);
		}
	}

	/**
	 * Change the text-view typface to the inputed font
	 * 
	 * @param tv
	 *            - the text-view to be changed
	 * @param font
	 *            - one of the custom fonts that was added to the application
	 */
	public void setFont(TextView tv, CustomFonts font) {
		tv.setTypeface(fontsMap.get(font));
	}
	public void setFont(Button btn, CustomFonts font) {
		btn.setTypeface(fontsMap.get(font));
	}
	

}
