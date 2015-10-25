package com.mdareports.utils;

import android.app.Activity;
import android.view.View;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.targets.ActionViewTarget;
import com.espian.showcaseview.targets.Target;
import com.espian.showcaseview.targets.ViewTarget;

public class HelpUtils {

	/**
	 * Wrapper methods for showing single showcase
	 */
	public static void showHelp(View view, int resIdTitle,
			int resIdDescription, Activity activity) {
		showHelp(new ViewTarget(view), resIdTitle, resIdDescription, activity);
	}

	public static void showHelp(int resIdView, int resIdTitle,
			int resIdDescription, Activity activity) {
		View view = activity.findViewById(resIdView);
		if (view != null)
			showHelp(view, resIdTitle, resIdDescription, activity);
	}

	public static void showHelp(Target target, int resIdTitle,
			int resIdDescription, Activity activity) {

		SettingsManager sm = SettingsManager.getInstance(activity);
		Class<?> cls = activity.getClass();			
		
		if (!sm.hasSeenHelp(cls)) {
			ShowcaseView.ConfigOptions mOptions = new ShowcaseView.ConfigOptions();
			mOptions.block = false;

			ShowcaseView sv = ShowcaseView.insertShowcaseView(target, activity,
					resIdTitle, resIdDescription, mOptions);
			sv.setShowcase(target, true);

			// set as seen
			sm.setSeenHelp(cls);
		}

	}

	public static void showHelpOnHomeAction(int resIdTitle,
			int resIdDescription, Activity activity) {
		showHelp(new ActionViewTarget(activity, ActionViewTarget.Type.HOME),
				resIdTitle, resIdDescription, activity);
	}

	public static void showHelpOnOverflowAction(int resIdTitle,
			int resIdDescription, Activity activity) {
		showHelp(
				new ActionViewTarget(activity, ActionViewTarget.Type.OVERFLOW),
				resIdTitle, resIdDescription, activity);
	}

	public static void showHelpOnSpinnerAction(int resIdTitle,
			int resIdDescription, Activity activity) {
		showHelp(new ActionViewTarget(activity, ActionViewTarget.Type.SPINNER),
				resIdTitle, resIdDescription, activity);
	}

	public static void showHelpOnTitle(int resIdTitle, int resIdDescription,
			Activity activity) {
		showHelp(new ActionViewTarget(activity, ActionViewTarget.Type.TITLE),
				resIdTitle, resIdDescription, activity);
	}

}
