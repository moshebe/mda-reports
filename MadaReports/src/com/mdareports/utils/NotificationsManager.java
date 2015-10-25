package com.mdareports.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.mdareports.R;
import com.mdareports.ui.activities.ReportsListActivity;

public class NotificationsManager {
	
	private static final Integer SMS_NOTIFICATION_ID = 0x1111;
	private Context context;
	private static NotificationsManager instance;

	/**
	 * Get instance of the class. implements the singleton design pattern.
	 * 
	 * @return instance of the class to work with
	 */
	public static NotificationsManager getInstance(Context context) {
		if (instance == null) {
			instance = new NotificationsManager(context);
		}
		return instance;
	}

	/**
	 * Constructs the session manager object with context. The context should be
	 * the activity that holds the operations.
	 */
	private NotificationsManager(Context context) {
		this.context = context;
	}

	private Notification createNotification(boolean nonRemovable, String title, String content, int iconId, Class<?> moveToactvty) {

		// Build notification
		NotificationCompat.Builder ncBuilder = new NotificationCompat.Builder(context);
		ncBuilder.setContentTitle(title);
		ncBuilder.setContentText(content);
		ncBuilder.setSmallIcon(iconId);
		ncBuilder.setOngoing(nonRemovable); // no removable notification = true

		if (moveToactvty != null) {
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, moveToactvty);
			// creating a pendingIntent
			// the flags mean that the current task will be cleared and the task will be started again
			resultIntent.setFlags(ApplicationUtils.getNewTaskFlags());
			PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
			                                                              0,
			                                                              resultIntent,
			                                                              PendingIntent.FLAG_UPDATE_CURRENT);

			ncBuilder.setContentIntent(resultPendingIntent);
		}

		return ncBuilder.build();
	}

	private void performNotification(Notification notification, int notificationId) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(notificationId, notification);
	}

	/**
	 * Raising notification that indicates that SMS from MDA is received
	 */
	public void raiseSmsReceivedNotification(String title, String description) {

		Notification notification = createNotification(true, title, description,
		                                               R.drawable.ic_launcher,
		                                               ReportsListActivity.class);

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;


		// treat the sound and vibration
		SettingsManager settingsManager = SettingsManager.getInstance(context);
		if (settingsManager.isSoundEnabled()) {
			notification.sound = settingsManager.getRingtoneUri();
		}
		
		if (settingsManager.isVibrateEnabled()) {
			 android.os.Vibrator vibrator = (android.os.Vibrator) context
	                    .getSystemService(Context.VIBRATOR_SERVICE);
	            vibrator.vibrate(500);
		}
		 
		performNotification(notification, SMS_NOTIFICATION_ID);

	}

	/**
	 * Removing the permanent notification that represents the running of the
	 * application
	 */	
	public void removeSmsReceivedNotification() {
		NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nManager.cancel(SMS_NOTIFICATION_ID);
	}

}
