package be.xios.jobfinder.service;

import be.xios.jobfinder.main.SearchResultActivity;
import be.xios.jobfinder.main.SearchResultFragment;
import be.xios.jobfinder.main.SettingsFragment;
import be.xios.jobfinder.model.SearchBuilder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

public class JobSearchService extends Service {

	private PowerManager.WakeLock wl;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SearchBuilder savedSearch = intent.getParcelableExtra(SettingsFragment.SELECTED_SAVED_SEARCH);
		
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP,
				"wakeup");
		wl.acquire();
		
		Bundle arguments = new Bundle();
		arguments.putParcelable(SearchResultFragment.SEARCH_PARAMS, savedSearch);
		
		Intent jobSearchIntent = new Intent(this, SearchResultActivity.class);
		jobSearchIntent.putExtras(arguments);
		jobSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(jobSearchIntent);
		
		wl.release();
		return super.onStartCommand(jobSearchIntent, flags, startId);
	}
}