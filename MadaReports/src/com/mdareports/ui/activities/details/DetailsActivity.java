package com.mdareports.ui.activities.details;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.models.Report;
import com.mdareports.ui.activities.BaseActivity;
import com.mdareports.ui.fragments.details.BaseDetailFragment;
import com.mdareports.utils.MdaAnalytics;
import com.mdareports.utils.SettingsManager;

public class DetailsActivity extends BaseActivity {

	public static final String REPORT_ID_EXTRA = "REPORT_ID_EXTRA";

	// Instance of the report of this DetailActivity
	private Report sentReport;
	private MdaPagerAdapter mdaPagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		// Get the sent report from the intent
		loadCurrentReportFromDb(getIntent().getExtras().getInt(REPORT_ID_EXTRA));		

		// Set read on the report and update it
		sentReport.setRead(true);
		DatabaseWrapper.getInstance(this).updateReport(sentReport);

		// Get the action bar and set it up
		final ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// init the ViewPager and the Adapter
		mdaPagerAdapter = new MdaPagerAdapter(getSupportFragmentManager(), this);
		viewPager = (ViewPager) findViewById(R.id.detailsPager);
		viewPager.setAdapter(mdaPagerAdapter);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						supportActionBar.setSelectedNavigationItem(position);
					}
				});

		// add the tabs to the action bar
		mdaPagerAdapter.initActionBar(supportActionBar, viewPager);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * Getter to the instance of the report in this activity
	 * 
	 * @return The report of this {@link DetailsActivity}
	 */
	public Report getCurrentReport() {
		return sentReport;
	}

	/**
	 * Setter to the instance of the report in this activity. <br>
	 * Only the {@link DetailsActivity} can use this setter
	 * 
	 * @param report
	 */
	private void setCurrentReport(Report report) {
		sentReport = report;
	}

	public void loadCurrentReportFromDb(int reportId) {
		setCurrentReport(DatabaseWrapper.getInstance(this).getReportById(reportId));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.details, menu);

		return true;
	}

	private Intent getShareIntent() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		String shareString = getCurrentReport().toShareString(this);
		shareString += SettingsManager.getInstance(this)
				.getVolunteerSignature();

		shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
		shareIntent.setType("text/plain");

		return shareIntent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:			
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.menu_save:
			// Save the data of the report from all the tab's fragments to the
			// instance of the currentReport of this activity
			saveCurrentReport();

			// Update the saved currentReport in the DataBase
			DatabaseWrapper.getInstance(this).updateReport(getCurrentReport());

			// Make Toast to the user
			Toast.makeText(this,
					getString(R.string.detail_activity_report_saved),
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.menu_sync:
			// Get the unchanged Report from the DataBase
			Report currentReportFromDataBase = DatabaseWrapper
					.getInstance(this)
					.getReportById(getCurrentReport().getId());

			// Set the found Report from the DataBase as the currentReport
			setCurrentReport(currentReportFromDataBase);

			// Refresh all the tabs in the activity
			rollbackCurrentReport();
			return true;

		case R.id.menu_delete:
			handleDelete(this);
			return true;

		case R.id.menu_share:
			// The ShareActionProvider of sherlock is not working well on
			// Android 2.3.5 (i checked it on Galaxy 2)
			// So here we will use the default ShareIntent
			Intent sendIntent = getShareIntent();
			startActivity(Intent.createChooser(sendIntent,
					getString(R.string.menu_share_message)));
			return true;

		case R.id.menu_show_original_message:
			showOriginalMessageDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void handleDelete(final Context context) {
		// show the options in a dialog
		new AlertDialog.Builder(this)
				.setMessage(R.string.detail_activity_dialog_delete_are_you_sure)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (item == DialogInterface.BUTTON_POSITIVE) {
									DatabaseWrapper databaseWrapper = DatabaseWrapper
											.getInstance(context);
									// perform the delete
									if (DatabaseWrapper.getInstance(context)
											.deleteReport(getCurrentReport())) {
										databaseWrapper
												.deleteTreatmentsToReportByReportId(getCurrentReport()
														.getId());
										writeShortTimeMessage(R.string.deleted_successfuly);
										finish();
									}
								}
								dialog.dismiss();
							}
						}).setNegativeButton(android.R.string.cancel, null).show();
	}

	public void showOriginalMessageDialog() {
		// build the dialog layout
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View originalMessageDialogView = inflater.inflate(
				R.layout.dialog_original_message, null, false);

		TextView originalMessageTextView = (TextView) originalMessageDialogView
				.findViewById(R.id.originalMessageTextView);
		originalMessageTextView
				.setText(getCurrentReport().getOriginalMessage());

		// the alert dialog
		new AlertDialog.Builder(this)
				.setView(originalMessageDialogView)
				.setTitle(
						getString(R.string.fragment_general_info_title_dialog_original_message))
				.show();
	}

	/**
	 * Get all the information from all the fragments in this
	 * {@link DetailsActivity} by calling the method: "saveCurrentReport" in
	 * each fragment</br> !!IMPORTANT: When adding a tab that use the
	 * currentReport of this activity, </br></br> please extend from the
	 * {@link BaseDetailFragment} abstract class.
	 */
	public void saveCurrentReport() {
		// Save report event on Google Analytics
		MdaAnalytics.reportSaved(this);

		mdaPagerAdapter.saveAllTabs();

	}

	/**
	 * Rollback the data in the fragments in this DetailActivity.</br></br>
	 * !!IMPORTANT: When adding a tab that use the currentReport of this
	 * activity, </br> please extend from the {@link BaseDetailFragment}
	 * abstract class.
	 */
	public void rollbackCurrentReport() {
		// Rollback report event on Google Analytics
		MdaAnalytics.reportRollback(this);

		mdaPagerAdapter.refreshAllTabs();
	}

}