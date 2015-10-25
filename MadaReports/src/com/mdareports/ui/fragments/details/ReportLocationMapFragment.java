package com.mdareports.ui.fragments.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mdareports.R;
import com.mdareports.db.models.Report;
import com.mdareports.ui.activities.ReportLocationActivity;
import com.mdareports.ui.fragments.BaseFragment;

public class ReportLocationMapFragment extends BaseFragment {
	private GoogleMap map;
	private Marker currentMarker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_report_location_map,
				container, false);

		map = ((SupportMapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		map.setMyLocationEnabled(true);

		map.setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				markerReportLocation(point);
				((ReportLocationActivity)getActivity()).onLocationMarkered();
			}
		});

		return rootView;
	}

	public void removeMarker() {
		// if we will support more than one marker
		// consider using map.clear()
		if (currentMarker != null){
			currentMarker.remove();
		}
		currentMarker = null;
	}

	public void markerReportLocation(LatLng location) {
		// clear previous marker if exists
		if (currentMarker != null) {
			currentMarker.remove();
		}

		if (location != null) {
			// add the new marker with the report's location
			currentMarker = map.addMarker(new MarkerOptions()
					.position(location).title(getActivity().getString(R.string.report_location_marker_title))
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
	}

	public void markerReportLocation(Report report) {
		markerReportLocation(report.getLocation());
	}

	public void zoomIntoLocation(LatLng location) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomIn());
		// Zoom out to zoom level 10, animating with a duration of 2 seconds.
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}

	// zoom the camera into the marker location
	public void zoomIntoCurrentLocation() {
		if (currentMarker != null) {
			zoomIntoLocation(currentMarker.getPosition());
		}
	}

	public LatLng getCurrentLocation() {
		if (currentMarker != null) {
			return currentMarker.getPosition();
		}
		return null;
	}

}
