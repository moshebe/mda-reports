package com.mdareports.utils.billing;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

public class BillingUtils {

	private Activity activity;
	private IInAppBillingService mService;
	private final int API_VERSION = 3;
	private String packageName;

	public BillingUtils(Activity activity) {
		this.activity = activity;
		this.packageName = activity.getPackageName();
	}

	/**
	 * Should be called onCreate
	 */
	public void bind() {
		activity.bindService(new Intent(
				"com.android.vending.billing.InAppBillingService.BIND"),
				mServiceConn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * Should be called onDestroy
	 */
	public void unbind() {
		if (mService != null) {
			activity.unbindService(mServiceConn);
		}
	}

	/**
	 * Should be called onActivityResult
	 * 
	 * @param data
	 *            - the returned intent to the activity.
	 */
	public void consumePurchasedItems(final Intent data) {
		new Thread() {
			@Override
			public void run() {
				try {
					JSONObject json = new JSONObject(
							data.getStringExtra("INAPP_PURCHASE_DATA"));
					mService.consumePurchase(API_VERSION, packageName,
							json.getString("purchaseToken"));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	/**
	 * Perform the actual buying (moving to the Google-Play purchase screen)
	 * 
	 * @param item
	 *            - the item to be bought, corresponding to the name in the
	 *            developer console.
	 */
	public void buy(String item) {
		try {
			Bundle buyIntentBundle = mService
					.getBuyIntent(
							API_VERSION,
							packageName,
							item,
							"inapp",
							// TODO: maybe save encrypted
							// or
							// calculate it at runtime (google security
							// recommendation)
							"XXXXX-HERE-THE-PK-XXXXX");
			PendingIntent pendingIntent = buyIntentBundle
					.getParcelable("BUY_INTENT");
			if (pendingIntent != null) {
				activity.startIntentSenderForResult(
						pendingIntent.getIntentSender(), 1001, new Intent(), 0,
						0, 0);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SendIntentException e) {
			e.printStackTrace();
		}
	}

	public void buy(int itemNumber) {
		buy("donate" + itemNumber);
	}

	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);

			new Thread() {

				@Override
				public void run() {
					try {
						Bundle ownedItems = mService.getPurchases(API_VERSION,
								packageName, "inapp", null);

						int response = ownedItems.getInt("RESPONSE_CODE");
						if (response == 0) {
							ArrayList<String> purchaseDataList = ownedItems
									.getStringArrayList("INAPP_PURCHASE_DATA_LIST");

							if (purchaseDataList != null) {
								for (String purchaseData : purchaseDataList) {
									JSONObject json = new JSONObject(
											purchaseData);
									mService.consumePurchase(API_VERSION,
											packageName,
											json.getString("purchaseToken"));
								}
							}
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	};

}
