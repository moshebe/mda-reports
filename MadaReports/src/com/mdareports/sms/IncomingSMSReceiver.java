package com.mdareports.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.models.Report;
import com.mdareports.db.reports.ReportAnalyzer;
import com.mdareports.utils.ApplicationUtils;
import com.mdareports.utils.MdaAnalytics;
import com.mdareports.utils.NotificationsManager;
import com.mdareports.utils.SettingsManager;

public class IncomingSMSReceiver extends BroadcastReceiver {
//	static StringBuilder bufferedMsg = new StringBuilder();
	static boolean isFinishReceiving = true;

	private void raiseMessage(String smsMessageBody, long timestampMillies, Context context) {
		// Create new Report object and insert to DB
		Report report = new Report(context, smsMessageBody, timestampMillies);
		
		DatabaseWrapper dbWrpr = DatabaseWrapper.getInstance(context);
		dbWrpr.createReport(report);
		
		// Create SMS received for Google Analytics
		MdaAnalytics.smsReceivedEvent(context);
		
		// If the application is in Foreground, we don't need to notify to the user about the report
		if (ApplicationUtils.isApplicationInForeground(context)) {
			return;
		}

		// Get count of unread reports
		int countUnreadReports = dbWrpr.countUnreadReports();

		// Init the formatted string
		String formattedString = context.getResources().getQuantityString(R.plurals.notification_d_new_messages, countUnreadReports, countUnreadReports);

		// Notify the user
		NotificationsManager.getInstance(context).raiseSmsReceivedNotification(formattedString,
		                                                                       report.getDescription());
	}

	/**
	 * Check if the message came from the relevant sender
	 * 
	 * @param smsMsg
	 *            - the message to be checked
	 * @return True if the message is relevant and should be treated, False
	 *         otherwise
	 */
	boolean isRelevantSms(String smsMessgeBody) {
		// TODO: check by the message structure. should be from private number
		// (check if it could be detected) with specific scheme.
		// return true;
		return ReportAnalyzer.isRelevantMessage(smsMessgeBody);
	}

	/**
	 * Receiving the incoming message, filter the relevant SMS ones and raise
	 * them for treatment
	 */
	public void onReceive(Context context, Intent intent) {
		SettingsManager settingsManager = SettingsManager.getInstance(context);
		StringBuilder bufferedMsg = new StringBuilder(settingsManager.getBufferedMessage());
		
		// check if the incoming message is SMS
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			isFinishReceiving = true;
			
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");

				// reassemble the PDUs into array of SMS messages
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

				StringBuilder messageBodyBuilder = new StringBuilder();

				// iterate the SMS messages that was received
				int i;
				for (i = 0; i < messages.length; i++) {
					String msgBody = messages[i].getMessageBody();

					if (isMultipartMessage(msgBody)) {
						bufferedMsg.append(getMsgWithoutMultipartPrefix(msgBody));

						if (isEndMultipartMessage(msgBody)) {
							messageBodyBuilder.append(bufferedMsg);
							bufferedMsg = new StringBuilder();
							isFinishReceiving = true;
						} else {
							isFinishReceiving = false;
						}
					} else {
						if (i > 0 && bufferedMsg.length() > 0) {
							//messageBodyBuilder.append(bufferedMsg);
							break;
						} else {
							messageBodyBuilder.append(msgBody);
						}
					}
				}

				// treat the case where the first message is multipart and the
				// other fragments are not
				if (i != messages.length) {
					for (i = 1; i < messages.length; i++) {
						bufferedMsg.append(messages[i].getMessageBody());
						//messageBodyBuilder.append(messages[i].getMessageBody());
					}
					//messageBodyBuilder = new  StringBuilder(bufferedMsg);
				}

				if (isFinishReceiving) {
					bufferedMsg = new StringBuilder();
					String messageBody = messageBodyBuilder.toString();
					// check if the message is relevant and pass it on
					if (isRelevantSms(messageBody)) {
						raiseMessage(messageBody, messages[0].getTimestampMillis(),
						             context);

						if (SettingsManager.getInstance(context).getAbortBroadcast()) {
							abortBroadcast();
						}
					}
				}
			}
		}
		
		settingsManager.setBufferedMessage(bufferedMsg.toString());
	}

	/**
	 * Strip out the multipart prefix from an inputed message
	 * 
	 * @param msg
	 *            - the message to get the information from
	 * @return copy of the message without the multipart prefix
	 */
	private static String getMsgWithoutMultipartPrefix(String msg) {
		int indexOfFirstSpace = msg.indexOf(' ');
		return msg.substring(indexOfFirstSpace + 1, msg.length());
	}

	/**
	 * Check if the message is the last one in the multi-part sequence. Based on
	 * the x/y strtcture the x should be equal to y in the last message
	 * 
	 * @param msg
	 *            - the message to be checked. must contains "x/y " at the
	 *            beginning.
	 * @return True if it is the last message, False otherwise
	 */
	private static boolean isEndMultipartMessage(String msg) {
		int indexOfSlash = msg.indexOf('/');
		return msg.substring(0, indexOfSlash).equals(msg.substring(indexOfSlash + 1,
		                                                           msg.indexOf(' ')));
	}

	/**
	 * Checks if the message is multi-part. The detection depends on the
	 * structure "x/y ". so we will pass to this function sub string of the
	 * message's body from the beginning until the first space/
	 * 
	 * @param msgUntilFirstSpace
	 *            the prefix of the message body (until the first space)
	 * @return True if the message is multi-part, False otherwise
	 */
	private static boolean isMultipartMessage(String msgUntilFirstSpace) {
		// should start with int/int (space)
		return msgUntilFirstSpace.matches("\\d/\\d .+");
	}

}
