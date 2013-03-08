package be.xios.jobfinder.service;

import be.xios.jobfinder.SearchResultActivity;
import be.xios.jobfinder.SettingsFragment;
import be.xios.jobfinder.model.SearchBuilder;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class JobSearchService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		SearchBuilder savedSearch = intent.getParcelableExtra(SettingsFragment.SELECTED_SAVED_SEARCH);
		SearchBuilder savedSearch = new SearchBuilder();
		savedSearch.setKeywords("java");
//		Toast.makeText(this, savedSearch, Toast.LENGTH_LONG).show();
		
		Intent jobSearchIntent = new Intent(getApplicationContext(), SearchResultActivity.class);
		Bundle arguments = new Bundle();
		arguments.putParcelable(SearchResultActivity.SEARCH_PARAMS, savedSearch);
		jobSearchIntent.putExtras(arguments);
		
		return super.onStartCommand(jobSearchIntent, flags, startId);
	}
}