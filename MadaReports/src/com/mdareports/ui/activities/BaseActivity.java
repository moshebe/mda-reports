package com.mdareports.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mdareports.utils.MdaAnalytics;

public class BaseActivity extends ActionBarActivity {

	// /////////////////////////////////////////
	// --- Navigate between activities ---
	// /////////////////////////////////////////

	/**
	 * Create intent for the move to methods
	 * 
	 * @param actvtyClass
	 *            the class of the activity to move to it
	 * @param intentFlags
	 *            the flags that determine the intent behavior
	 * @return intent that can be started
	 */
	private Intent createIntent(Class<?> actvtyClass, Integer flags) {
		Intent intent = new Intent(this, actvtyClass);
		flags = (flags == null) ? 0 : flags;
		intent.setFlags(flags);
		return intent;
	}

	/**
	 * move to other activity this method
	 * 
	 * @param actvtyClass
	 *            the class of the activity to move to it
	 * @param intentFlags
	 *            the flags that determine the intent behavior
	 */
	protected void MoveTo(Class<?> actvtyClass, Integer flags) {
		startActivity(createIntent(actvtyClass, flags));
	}

	protected void MoveTo(Class<?> actvtyClass) {
		this.MoveTo(actvtyClass, null);
	}

	/**
	 * Get on click listener that holds the move-to method on its on-click
	 * event.
	 * 
	 * @param actvtyClass
	 *            the class of the activity to move to it
	 * @param intentFlags
	 *            the flags that determine the intent behavior
	 * @return listener that can be assigned to ui widgets
	 */
	protected OnClickListener getMoveToClickListener(
			final Class<?> actvtyClass, final Integer flags) {
		return new OnClickListener() {
			public void onClick(View v) {
				MoveTo(actvtyClass, flags);
			}
		};
	}

	protected OnClickListener getMoveToClickListener(final Class<?> actvtyClass) {
		return getMoveToClickListener(actvtyClass, null);
	}

	/**
	 * Set the button as navigator to the inputed activity
	 * 
	 * @param buttonId
	 *            the resource id of the button to be set
	 * @param actvtyClassToMoveTo
	 *            the class of the activity to move to it
	 */
	protected void setNavigationButton(int buttonId,
			Class<?> actvtyClassToMoveTo, Integer flags) {
		Button button = getButton(buttonId);
		button.setOnClickListener(getMoveToClickListener(actvtyClassToMoveTo,
				flags));

	}

	protected void setNavigationButton(int buttonId,
			Class<?> actvtyClassToMoveTo) {
		this.setNavigationButton(buttonId, actvtyClassToMoveTo, null);
	}

	// /////////////////////////////////////////
	// --- End of Navigate between activities ---
	// /////////////////////////////////////////

	// /////////////////////////////////////////
	// --- Get Controls without casting ---
	// /////////////////////////////////////////
	protected TextView getTextView(int id) {
		return ((TextView) findViewById(id));
	}

	protected Spinner getSpinner(int id) {
		return ((Spinner) findViewById(id));
	}

	protected EditText getEditText(int id) {
		return ((EditText) findViewById(id));
	}

	protected CheckBox getCheckBox(int id) {
		return ((CheckBox) findViewById(id));
	}

	protected Button getButton(int id) {
		return ((Button) findViewById(id));
	}

	protected ListView getListView(int id) {
		return ((ListView) findViewById(id));
	}

	protected GridView getGridView(int id) {
		return ((GridView) findViewById(id));
	}

	protected WebView getWebView(int id) {
		return ((WebView) findViewById(id));
	}

	protected ImageView getImageView(int id) {
		return ((ImageView) findViewById(id));
	}

	protected ImageButton getImageButton(int id) {
		return ((ImageButton) findViewById(id));
	}

	protected SeekBar getSeekBar(int id) {
		return ((SeekBar) findViewById(id));
	}

	// /////////////////////////////////////////
	// --- End of Get Controls without casting ---
	// /////////////////////////////////////////

	/**
	 * create a Toast on the screen the Toast is disabled for <b>Short</b> time
	 * 
	 * @param message
	 *            the text to write on the screen
	 */
	public void writeShortTimeMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create a Toast on the screen the Toast is disabled for <b>Long</b> time
	 * 
	 * @param message
	 *            the text to write on the screen
	 */
	public void writeLongTimeMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	

	/**
	 * create a Toast on the screen the Toast is disabled for <b>Short</b> time
	 * 
	 * @param messageResId
	 *            the resource id of the string to be displayed
	 */
	public void writeShortTimeMessage(int messageResId) {
		writeShortTimeMessage(getResources().getString(messageResId));
	}

	/**
	 * create a Toast on the screen the Toast is disabled for <b>Short</b> time
	 * 
	 * @param messageResId
	 *            the resource id of the string to be displayed
	 */
	public void writeLongTimeMessage(int messageResId) {
		writeLongTimeMessage(getResources().getString(messageResId));
	}

	
	// /////////////////////////////////////////
	// --- Google Analytics Tracking ---
	// /////////////////////////////////////////
	
	@Override
	protected void onStart() {	
		super.onStart();
		MdaAnalytics.activityStart(this);
	}
	
	@Override
	protected void onStop() {	
		super.onStop();
		MdaAnalytics.activityStop(this);
	}
	
	// /////////////////////////////////////////
	// --- End of Google Analytics Tracking ---
	// /////////////////////////////////////////
}