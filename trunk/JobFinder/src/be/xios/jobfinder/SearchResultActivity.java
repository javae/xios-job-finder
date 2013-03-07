package be.xios.jobfinder;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.scribe.model.Verb;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import be.xios.jobfinder.connector.LinkedInConnector;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.json.LinkedInJobParser;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.model.SearchBuilder;

public class SearchResultActivity extends ListActivity {

	public static final String SEARCH_PARAMS = "search_parameters";
	private JobListAdapter jobListAdapter;
	private SearchBuilder searchData;
	private JobFinderDAO datasource;
	private List<LinkedInJob> jobs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		searchData = bundle.getParcelable(SEARCH_PARAMS);

		// TODO: searchdata wordt hier goed binnengehaald, ook vanuit de saved
		// searches maar blijkbaar loopt er iets mis hieronder?
		jobListAdapter = new JobListAdapter(getApplicationContext(),
				new ArrayList<LinkedInJob>());
		LinkedInJobSearch search = new LinkedInJobSearch(jobListAdapter);
		search.execute(searchData);

		setListAdapter(jobListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_result, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save_searchparams:

			datasource = new JobFinderDAO(getApplicationContext());
			datasource.open();
			long newID = datasource.createSavedSearch(searchData);
			if (newID > 0) {
				Toast.makeText(getApplicationContext(),
						"Favoriet toegevoegd met ID " + newID,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Favoriet toevoegen mislukt", Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.menu_mapview:
			Bundle b = new Bundle();
			Intent mapInt = new Intent(getApplicationContext(),
					MapActivity.class);

			b.putParcelableArrayList(MapActivity.JOB_LIST,
					(ArrayList<? extends Parcelable>) (jobs));
			mapInt.putExtras(b);
			startActivity(mapInt);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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

	private class LinkedInJobSearch extends
			AsyncTask<SearchBuilder, Void, List<LinkedInJob>> {

		String url = "http://api.linkedin.com/v1/job-search:(jobs:(id,posting-timestamp,company:(name),position:(title,location)))";
		JobListAdapter jobListAdapter;

		public LinkedInJobSearch(JobListAdapter jobListAdapter) {
			this.jobListAdapter = jobListAdapter;
		}

		@Override
		protected List<LinkedInJob> doInBackground(SearchBuilder... params) {
			jobs = new ArrayList<LinkedInJob>();

			LinkedInConnector connector = new LinkedInConnector();
			InputStream in = connector.sendRequest(Verb.GET, url, params[0]);

			LinkedInJobParser parser = new LinkedInJobParser();
			try {
				jobs = parser.readJsonStream(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return jobs;
		}

		@Override
		protected void onPostExecute(List<LinkedInJob> result) {
			super.onPostExecute(result);

			jobListAdapter.addAll(result);
			jobListAdapter.notifyDataSetChanged();
		}
	}

}
