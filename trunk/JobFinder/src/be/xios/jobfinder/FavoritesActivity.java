package be.xios.jobfinder;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.model.LinkedInJob;

public class FavoritesActivity extends ListActivity {

	private JobFinderDAO datasource;
	private JobListAdapter jobListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		setTitle(R.string.title_activity_favorites);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		datasource = new JobFinderDAO(getApplicationContext());
		datasource.open();

		List<LinkedInJob> values = datasource.getAllFavJobs();

		jobListAdapter = new JobListAdapter(getApplicationContext(),
				new ArrayList<LinkedInJob>());
		jobListAdapter.addAll(values);
		setListAdapter(jobListAdapter);

		ArrayAdapter<LinkedInJob> adapter = new ArrayAdapter<LinkedInJob>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_favorites, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent jobDetailIntent = new Intent(getApplicationContext(),
				JobDetailActivity.class);
		LinkedInJob job = (LinkedInJob) l.getAdapter().getItem(position);

		Bundle b = new Bundle();
		b.putParcelable("selectedJob", job);

		jobDetailIntent.putExtras(b);
		startActivity(jobDetailIntent);
	}
}
