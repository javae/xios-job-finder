package be.xios.jobfinder.service;

import be.xios.jobfinder.SettingsFragment;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class JobSearchService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String savedSearch = intent.getStringExtra(SettingsFragment.SELECTED_SAVED_SEARCH);
		Toast.makeText(this, savedSearch, Toast.LENGTH_LONG).show();
		return super.onStartCommand(intent, flags, startId);
	}
}