package com.mdareports.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.billing.BillingUtils;

@SuppressLint("NewApi")
public class DonateUsFragment extends BaseFragment {
	private RatingBar ratingBarDonation;
	private TextView tvDonateSum;
	private ImageView imgGoodRate;
	private ImageView imgBadRate;
	private int[] donationSums = new int[] { 1, 2, 4, 5, 10 };
	private int previousDonationSum;
	private BillingUtils billing;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		changeTitle(R.string.title_donate_us);
		
		final View rootView = inflater.inflate(R.layout.fragment_donate_us,
				container, false);

		imgGoodRate = (ImageView) rootView.findViewById(R.id.imgGoodRate);
		imgBadRate = (ImageView) rootView.findViewById(R.id.imgBadRate);
		tvDonateSum = (TextView) rootView.findViewById(R.id.tvDonateSum);
		ratingBarDonation = (RatingBar) rootView
				.findViewById(R.id.ratingBarDonation);

		previousDonationSum = (int) ratingBarDonation.getRating();

		ratingBarDonation
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {

						ImageView animatedImg = (previousDonationSum > rating) ? imgBadRate
								: imgGoodRate;

						previousDonationSum = (int) rating;										
						
						tvDonateSum.setText(donationSums[(int) rating - 1]
								+ "$");

						tvDonateSum.startAnimation(AnimationUtils
								.loadAnimation(getActivity(), R.anim.bounce));
						animatedImg.startAnimation(AnimationUtils
								.loadAnimation(getActivity(),
										R.anim.abc_fade_out));
					}
				});

		ratingBarDonation.setRating(ratingBarDonation.getMax());
		

		rootView.findViewById(R.id.btnDonate).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (ratingBarDonation != null){
					int rating = (int)ratingBarDonation.getRating();
					if (rating >= 1 && rating <= 5){
						billing.buy(rating);
					}
				}
			}
		});
		
		
		// set the rating bar correct direction
		if (DeviceInfoUtils.isCurrentLanguageHebrew(rootView.getContext())){
			ratingBarDonation.setScaleX(-1);
		}
				
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		
		Activity activity = getActivity();
		if (resultCode == Activity.RESULT_OK) {
			Toast.makeText(activity, R.string.donate_us_thank_you, Toast.LENGTH_LONG).show();

			if (billing != null)
				billing.consumePurchasedItems(data);
		}
		
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
		
		billing = new BillingUtils(getActivity());
		billing.bind();
	}
	
	

}
