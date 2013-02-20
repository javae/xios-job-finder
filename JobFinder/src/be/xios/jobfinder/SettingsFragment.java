package be.xios.jobfinder;

import be.xios.jobfinder.R;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		ListPreference savedSearchPreference = (ListPreference) findPreference("saved_search");
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

	}

}