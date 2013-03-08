package be.xios.jobfinder;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.model.LinkedInJob;

public class FavoritesActivity extends ListActivity {

	private static final int myContextmenu_id = 1;
	private static final int openMenuItem_Id = 2;
	private static final int deleteMenuItem_Id = 3;
	private JobFinderDAO datasource;
	private JobListAdapter jobListAdapter;
	private ArrayAdapter<LinkedInJob> adapter;

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

//		jobListAdapter = new JobListAdapter(getApplicationContext(),
//				new ArrayList<LinkedInJob>());
//		jobListAdapter.addAll(values);
//		setListAdapter(jobListAdapter);

		adapter = new ArrayAdapter<LinkedInJob>(this,
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		//int position = info.position;
		menu.setHeaderTitle("Kies een actie");
		menu.add(myContextmenu_id, openMenuItem_Id, 1, "Open");
		menu.add(myContextmenu_id, deleteMenuItem_Id, 2, "Verwijder");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		LinkedInJob selectedJob;

		switch (item.getItemId()) {
		case openMenuItem_Id:
			selectedJob = (LinkedInJob) getListView().getAdapter().getItem(
					info.position);
			Intent jobDetailIntent = new Intent(getApplicationContext(),
					JobDetailActivity.class);
			Bundle b = new Bundle();
			b.putParcelable(JobDetailActivity.JOB_SELECTED, selectedJob);

			jobDetailIntent.putExtras(b);
			startActivity(jobDetailIntent);
			break;
		case deleteMenuItem_Id:
			selectedJob = (LinkedInJob) getListView().getAdapter().getItem(
					info.position);
			datasource.deleteFavJob(selectedJob);
			adapter.notifyDataSetChanged();
			//TODO refresh werkt nog niet?
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
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
		b.putParcelable(JobDetailActivity.JOB_SELECTED, job);

		jobDetailIntent.putExtras(b);
		startActivity(jobDetailIntent);
	}
}
