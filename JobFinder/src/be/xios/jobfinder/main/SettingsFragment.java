package be.xios.jobfinder.main;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import be.xios.jobfinder.R;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.model.SearchBuilder;
import be.xios.jobfinder.preference.TimePreference;
import be.xios.jobfinder.service.JobSearchService;
import be.xios.jobfinder.util.StringUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment {
	
	public static final String ARG_ITEM_ID = "settings_fragment";
	public static final String SELECTED_SAVED_SEARCH = "selected_saved_search";
	
	private Resources resources;
	private JobFinderDAO datasource;
	
	private SwitchPreference switchAlarm;
	private ListPreference savedSearchPreference;
	private TimePreference timeTaskPreference;
	
	private List<SearchBuilder> savedSearches;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SettingsFragment() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resources = getResources();
		
		addPreferencesFromResource(R.xml.preferences);

		switchAlarm = (SwitchPreference) findPreference(resources.getString(R.string.key_display_alarm));
		switchAlarm.setOnPreferenceChangeListener(new JobSearchHandler());
		
		datasource = new JobFinderDAO(getActivity().getApplicationContext());
		datasource.open();
		savedSearches = datasource.getAllSavedSearches();
		
		savedSearchPreference = (ListPreference) findPreference(resources.getString(R.string.key_saved_search));
		if (savedSearchPreference != null)
			savedSearchPreference.setSummary(savedSearchPreference.getValue());
		savedSearchPreference.setOnPreferenceChangeListener(new JobSearchHandler());
		
		CharSequence[] entries = new CharSequence[savedSearches.size()];
		CharSequence[] entryValues = new CharSequence[savedSearches.size()];
		int i = 0;
		for (SearchBuilder savedSearch : savedSearches) {
			entries[i] = savedSearch.getJobTitle();
			entryValues[i] = savedSearch.getDbId() + "";
			i++;
		}
		savedSearchPreference.setEntries(entries);
		savedSearchPreference.setEntryValues(entryValues);
		
		timeTaskPreference = (TimePreference) findPreference(resources.getString(R.string.key_time_task));
		timeTaskPreference.setOnPreferenceChangeListener(new JobSearchHandler());
	}

	public class JobSearchHandler implements OnPreferenceChangeListener {

		private PendingIntent pendingIntent;
		
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Activity.ALARM_SERVICE);
			
			if (preference.getKey().equals(resources.getString(R.string.key_saved_search))) {
				ListPreference configIdPreference = (ListPreference) preference;
				configIdPreference.setSummary((String) newValue);
			}
			
			if (preference.getKey().equals(resources.getString(R.string.key_display_alarm))) {
				Boolean autoSearch = (Boolean) newValue;
				
				if (!autoSearch && pendingIntent != null) {
					alarmManager.cancel(pendingIntent);
				} else if (autoSearch && StringUtil.isNotBlank(savedSearchPreference.getValue())){
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
//			jobSearchIntent.putExtra(SELECTED_SAVED_SEARCH, savedSearches.get(savedSearchPreference.findIndexOfValue(savedSearchPreference.getValue())));
			pendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, jobSearchIntent, 0);
			
			long day = 1000 * 24*3600;
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			
			Calendar selectedTime = Calendar.getInstance();
			selectedTime.setTimeInMillis(timeTaskPreference.getTime().getTime());
	        
//			calendar.set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY));
//			calendar.set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE));
			calendar.add(Calendar.SECOND, 5);
			calendar.getTime();
	        
	        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), day, pendingIntent);
	        Toast.makeText(getActivity(), "Alarm set", Toast.LENGTH_LONG).show();
		}
	}
}