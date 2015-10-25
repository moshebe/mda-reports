package com.mdareports.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mdareports.R;

public class AboutUsFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		changeTitle(R.string.title_about_us);
		
		final View rootView = inflater.inflate(R.layout.fragment_about_us,
				container, false);

		rootView.findViewById(R.id.imgEmailUs).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// send email to our account
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("message/rfc822");
						i.putExtra(
								Intent.EXTRA_EMAIL,
								new String[] { getResources().getString(
										R.string.contact_email_address) });
						try {
							startActivity(Intent
									.createChooser(
											i,
											getResources()
													.getString(
															R.string.about_us_sending_email_dialog)));
						} catch (android.content.ActivityNotFoundException ex) {
							Toast.makeText(
									getActivity(),
									R.string.about_us_no_email_clients_installed,
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		return rootView;
	}

}
