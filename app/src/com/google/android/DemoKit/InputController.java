package com.google.android.DemoKit;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

public class InputController extends AccessoryController {
	private TextView mLightView;
	private TextView mLightRawView;
	ArrayList<SwitchDisplayer> mSwitchDisplayers;
	private final DecimalFormat mLightValueFormatter = new DecimalFormat("##.#");
	private final DecimalFormat mTemperatureFormatter = new DecimalFormat(
			"###" + (char)0x00B0);

	InputController(DemoKitActivity hostActivity) {
		super(hostActivity);
		mLightView = (TextView) findViewById(R.id.lightPercentValue);
		mLightRawView = (TextView) findViewById(R.id.lightRawValue);
	}

	protected void onAccesssoryAttached() {
		mSwitchDisplayers = new ArrayList<SwitchDisplayer>();
		for (int i = 0; i < 4; ++i) {
			SwitchDisplayer sd = new SwitchDisplayer(i);
			mSwitchDisplayers.add(sd);
		}
	}

	public void setLightValue(int lightValueFromArduino) {
		mLightRawView.setText(String.valueOf(lightValueFromArduino));
		mLightView.setText(mLightValueFormatter
				.format((100.0 * (double) lightValueFromArduino / 1024.0)));
	}

	public void switchStateChanged(int switchIndex, boolean switchState) {
		if (switchIndex >= 0 && switchIndex < mSwitchDisplayers.size()) {
			SwitchDisplayer sd = mSwitchDisplayers.get(switchIndex);
			sd.onSwitchStateChange(switchState);
		}
	}

	public void onLightChange(int lightValue) {
		setLightValue(lightValue);
	}

	public void onSwitchStateChange(int switchIndex, Boolean switchState) {
		switchStateChanged(switchIndex, switchState);
	}

	class SwitchDisplayer {
		private final ImageView mTargetView;
		private final Drawable mOnImage;
		private final Drawable mOffImage;

		SwitchDisplayer(int switchIndex) {
			int viewId, onImageId, offImageId;
			switch (switchIndex) {
			default:
				viewId = R.id.Button1;
				onImageId = R.drawable.indicator_button1_on_noglow;
				offImageId = R.drawable.indicator_button1_off_noglow;
				break;
			case 1:
				viewId = R.id.Button2;
				onImageId = R.drawable.indicator_button2_on_noglow;
				offImageId = R.drawable.indicator_button2_off_noglow;
				break;
			case 2:
				viewId = R.id.Button3;
				onImageId = R.drawable.indicator_button3_on_noglow;
				offImageId = R.drawable.indicator_button3_off_noglow;
				break;
			case 3:
				viewId = R.id.Button4;
				onImageId = R.drawable.indicator_button_capacitive_on_noglow;
				offImageId = R.drawable.indicator_button_capacitive_off_noglow;
				break;
			}
			mTargetView = (ImageView) findViewById(viewId);
			mOffImage = mHostActivity.getResources().getDrawable(offImageId);
			mOnImage = mHostActivity.getResources().getDrawable(onImageId);
		}

		public void onSwitchStateChange(Boolean switchState) {
			Drawable currentImage;
			if (!switchState) {
				currentImage = mOffImage;
			} else {
				currentImage = mOnImage;
			}
			mTargetView.setImageDrawable(currentImage);
		}

	}
}
