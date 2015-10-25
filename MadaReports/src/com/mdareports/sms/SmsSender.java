package com.mdareports.sms;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.mdareports.utils.SettingsManager;

public class SmsSender {

	public static void send(Context context, String msg){
		send(context, SettingsManager.getInstance(context)
				.getSpatialTelephonyCenterNumber(), msg);		
	}
	
	public static void send(Context context, String to, String msg){
		// send the report in the native SMS application
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.setData(Uri.parse("sms:" + to));
		sendIntent.putExtra("sms_body", msg);

		context.startActivity(sendIntent);
	}
	
	
	
}
