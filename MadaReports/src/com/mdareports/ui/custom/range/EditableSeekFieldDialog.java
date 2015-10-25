package com.mdareports.ui.custom.range;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.mdareports.R;
import com.mdareports.utils.Logger;

public class EditableSeekFieldDialog extends RelativeLayout {
	private String TAG = Logger.makeLogTag(getClass());

	private SeekBar seekBar;
	private EditText editText;
	private int commonValue;

	public EditableSeekFieldDialog(Context context, AttributeSet attrs) {
		super(context, attrs);

		// inflate the layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.dialog_seekbar, this, true);

		// set the views members
		seekBar = (SeekBar) findViewById(R.id.dialogSeekBarView);
		editText = (EditText) findViewById(R.id.dialogEditText);

		// change the edit-text value each time the seek-bar is changed
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				if (fromUser) {
					Logger.LOGE(TAG, "onProgressChanged: " + progress);
					commonValue = progress;
					editText.setText(String.valueOf(commonValue));
				}
			}
		});

		// change the seek-bar value each time the edit-text is changed
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Logger.LOGE(TAG, "afterTextChanged: " + s.toString());
				try {
					commonValue = Integer.parseInt(s.toString());
					if (commonValue > seekBar.getMax())
						commonValue = seekBar.getMax();

					seekBar.setProgress(commonValue);
				} catch (Exception e) {
					Logger.LOGE(TAG, e.getMessage());
				}
				s.toString();

			}
		});

	}

	public void setSeekbarMax(int maxValue) {
		seekBar.setMax(maxValue);
	}

	public void setValue(int value) {
		editText.setText(String.valueOf(value)); // seek bar is auto-changed
	}

	public int getValue() {
		return commonValue;
	}

	public void openDialog(Context context, String title, int resStrIdPositiveButton, int resStrIdNegativeButton, DialogInterface.OnClickListener onPositiveClick ) {
		new AlertDialog.Builder(context)
		.setView(this)
		.setTitle(title)				
		.setPositiveButton(resStrIdPositiveButton, onPositiveClick)
		.setNegativeButton(resStrIdNegativeButton, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}

}
