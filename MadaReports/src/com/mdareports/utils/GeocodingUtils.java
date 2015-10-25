package com.mdareports.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

public class GeocodingUtils {

	public static Locale getLocale(boolean useCurrentLocale) {
		return (useCurrentLocale ? Locale.getDefault() : new Locale("he"));
	}

	public static Address getSingleAddreesByLocation(Context context,
			LatLng location, boolean useCurrentLocale) {
		List<Address> addresses = getAddresses(context, location,
				useCurrentLocale);
		if (addresses != null && addresses.size() > 0) {
			return addresses.get(0);
		} else {
			return null;
		}
	}

	public static List<Address> getAddresses(Context context, LatLng location,
			boolean useCurrentLocale) {
		return getAddresses(context, location, useCurrentLocale, 1);
	}

	public static List<Address> getAddresses(Context context, LatLng location,
			boolean useCurrentLocale, int maxNumOfResults) {

		try {
			return new Geocoder(context, getLocale(useCurrentLocale))
					.getFromLocation(location.latitude, location.longitude,
							maxNumOfResults);
		} catch (IOException e) {
			Logger.LOGE("GeocodingUtils", e.getMessage());
			return null;
		}
	}

	public static List<Address> getAddresses(Context context, String input,
			boolean useCurrentLocale, int maxNumOfResults) {
		try {
			return new Geocoder(context, getLocale(useCurrentLocale))
					.getFromLocationName(input, maxNumOfResults);
		} catch (IOException e) {
			Logger.LOGE("GeocodingUtils", e.getMessage());
			return null;
		}
	}
}
