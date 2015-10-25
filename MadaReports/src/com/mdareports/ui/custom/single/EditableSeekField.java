package com.mdareports.ui.custom.single;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mdareports.R;

/**
 * Represents regular field in the technical tab. Centralizes seek-bar in
 * read-only mode with edit button that supplies edit dialog using seek-bar or
 * edit-text
 */
public class EditableSeekField extends RelativeLayout {

	private TextView tvLabel;
	private SeekBar seekBarValue;
	private TextView tvValue;
	private ImageView imgEdit;

	/**
	 * Inflate the field layout, set the binding between the related views and
	 * initialize using the attributes from the XML layout
	 * 
	 * @param context
	 *            - the current context
	 * @param attrs
	 *            - the attributes defined in the layout
	 */
	public EditableSeekField(Context context, AttributeSet attrs) {
		super(context, attrs);

		// inflate the layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.field_technical_editable_seek, this, true);

		// set the views members
		tvLabel = (TextView) findViewById(R.id.tvLabelEditSeekField);
		seekBarValue = (SeekBar) findViewById(R.id.seekEditSeekField);
		tvValue = (TextView) findViewById(R.id.tvValueEditSeekField);
		imgEdit = (ImageView) findViewById(R.id.imgEditSeekField);

		// bind the text-view to change on each seek-bar change
		seekBarValue.setEnabled(false);
		seekBarValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvValue.setText(String.valueOf(progress));
			}
		});

		// initialize using information from the attributes
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.EditableSeekField, 0, 0);
		try {
			initialize(a.getString(R.styleable.EditableSeekField_fieldLabel),
					a.getInteger(R.styleable.EditableSeekField_maxValue, 0),
					a.getResourceId(
							R.styleable.EditableSeekField_positiveButtonText,
							android.R.string.ok), a.getResourceId(
							R.styleable.EditableSeekField_negativeButtonText,
							android.R.string.cancel)); // TODO: maybe this option can be
												// removed and set the OK and
												// CANCEL for all of the fields
		} finally {
			a.recycle();
		}

	}

	/**
	 * Initialize the variables of the class
	 * 
	 * @param label
	 *            - the name of the field
	 * @param maxProgress
	 *            - the maximum value of the seek-bar
	 * @param resStrIdPositiveButton
	 *            - the string to appear on the positive button, the default is
	 *            R.string.ok
	 * @param resStrIdNegativeButton
	 *            - the string to appear on the negative button, the default is
	 *            android.R.string.cancel
	 */
	public void initialize(final String label, int maxProgress,
			final int resStrIdPositiveButton, final int resStrIdNegativeButton) {
		// set the views according to the inputed parameters
		tvLabel.setText(label);
		seekBarValue.setMax(maxProgress);

		// set the edit image click handler
		imgEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditableSeekFieldDialog myLayout = new EditableSeekFieldDialog(
						getContext(), null);

				myLayout.setValue(seekBarValue.getProgress());
				myLayout.setSeekbarMax(seekBarValue.getMax());

				myLayout.openDialog(getContext(), label,
						resStrIdPositiveButton, resStrIdNegativeButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								seekBarValue.setProgress(myLayout.getValue());
							}
						});
			}
		});
	}

	/**
	 * Set the value of the field
	 * 
	 * @param value
	 *            - the value to be set
	 */
	public void setValue(int value) {
		seekBarValue.setProgress(value); // the edit-text will be updated
											// automatically
	}

	/**
	 * Get the current value of the field
	 * 
	 * @return the field's value
	 */
	public int getValue() {
		return seekBarValue.getProgress();
	}
}
