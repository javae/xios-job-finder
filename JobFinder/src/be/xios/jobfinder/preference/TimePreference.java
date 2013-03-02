package be.xios.jobfinder.preference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import be.xios.jobfinder.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {

	private TimePicker timePicker;

	public TimePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected View onCreateView(ViewGroup parent) {
		SharedPreferences preferences = getSharedPreferences();
		int hour = preferences.getInt(getKey() + ".hour", 8);
		int minute = preferences.getInt(getKey() + ".minute", 30);
		
		try {
			setSummary(getFormattedTime(hour, minute));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return super.onCreateView(parent);
	}
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		timePicker = (TimePicker) view.findViewById(R.id.pref_time_picker);
		timePicker.setCurrentHour(getSharedPreferences().getInt(getKey() + ".hour", 8));
		timePicker.setCurrentMinute(getSharedPreferences().getInt(getKey() + ".minute", 30));
		timePicker.setIs24HourView(DateFormat.is24HourFormat(timePicker.getContext()));
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			timePicker.clearFocus();
			
			int hour = timePicker.getCurrentHour();
			int minute = timePicker.getCurrentMinute();
			
			try {
				setSummary(getFormattedTime(hour, minute));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			SharedPreferences.Editor editor = getEditor();
			editor.putInt(getKey() + ".hour", hour);
			editor.putInt(getKey() + ".minute", minute);
			editor.commit();
			
			try {
				callChangeListener(getFormattedTime(hour, minute));
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
		}
	}
	
	private String getFormattedTime(int hour, int minute) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
		return DateFormat.getTimeFormat(getContext()).format(format.parse(hour + ":" + minute));
	}
	
	public Date getTime() throws ParseException {
		SharedPreferences preferences = getSharedPreferences();
		int hour = preferences.getInt(getKey() + ".hour", 8);
		int minute = preferences.getInt(getKey() + ".minute", 30);
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
		return format.parse(hour + ":" + minute);
	}
	
}