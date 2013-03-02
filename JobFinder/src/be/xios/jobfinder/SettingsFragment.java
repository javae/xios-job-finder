package be.xios.jobfinder;

import java.text.ParseException;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import be.xios.jobfinder.preference.TimePreference;
import be.xios.jobfinder.service.JobSearchService;

public class SettingsFragment extends PreferenceFragment {

	public static final String SELECTED_SAVED_SEARCH = "selected_saved_search";
	private Resources resources;
	
	private SwitchPreference switchAlarm;
	private ListPreference savedSearchPreference;
	private TimePreference timeTaskPreference;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resources = getResources();
		
		addPreferencesFromResource(R.xml.preferences);

		switchAlarm = (SwitchPreference) findPreference(resources.getString(R.string.key_display_alarm));
		switchAlarm.setOnPreferenceChangeListener(new JobSearchHandler());
		
		savedSearchPreference = (ListPreference) findPreference(resources.getString(R.string.key_saved_search));
		if (savedSearchPreference != null) {
			savedSearchPreference.setSummary(savedSearchPreference.getValue());
			savedSearchPreference.setOnPreferenceChangeListener(
					new OnPreferenceChangeListener() {
						@Override
						public boolean onPreferenceChange(Preference preference, Object newValue) {
							ListPreference configIdPreference = (ListPreference) preference;
							configIdPreference.setSummary((String) newValue);
							return true;
						}
					});
		}
		savedSearchPreference.setOnPreferenceChangeListener(new JobSearchHandler());
		
		timeTaskPreference = (TimePreference) findPreference(resources.getString(R.string.key_time_task));
//		timeTaskPreference.setOnPreferenceChangeListener(new JobSearchHandler());

	}
	
	public class JobSearchHandler implements OnPreferenceChangeListener {

		private PendingIntent pendingIntent;
		
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Activity.ALARM_SERVICE);
			
			if (preference.getKey().equals(resources.getString(R.string.key_display_alarm))) {
				Boolean autoSearch = (Boolean) newValue;
				
				if (!autoSearch && pendingIntent != null) {
					alarmManager.cancel(pendingIntent);
				} else if (autoSearch){
					try {
						setAlarm(alarmManager);
					} catch (ParseException pe) {
						Log.d(SettingsFragment.class.toString(), "Parsing of the selected time failed.", pe);
					}
				}
			} else {
				try {
					setAlarm(alarmManager);
				} catch (ParseException pe) {
					Log.d(SettingsFragment.class.toString(), "Parsing of the selected time failed.", pe);
				}
			}
			
			return true;
		}
		
		private void setAlarm(AlarmManager alarmManager) throws ParseException {
			Intent jobSearchIntent = new Intent(getActivity().getApplicationContext(), JobSearchService.class);
			jobSearchIntent.putExtra(SELECTED_SAVED_SEARCH, savedSearchPreference.getValue());
			pendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, jobSearchIntent, 0);
			
			long day = 1000 * 24*3600;
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.add(Calendar.SECOND, 5);
			
			Calendar selectedTime = Calendar.getInstance();
			selectedTime.setTimeInMillis(timeTaskPreference.getTime().getTime());
	        
			calendar.set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE));
			calendar.getTime();
	        
	        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), day, pendingIntent);
		}
	}
}