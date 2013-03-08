package be.xios.jobfinder.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.scribe.model.Verb;

import be.xios.jobfinder.JobDetailActivity;
import be.xios.jobfinder.MapActivity;
import be.xios.jobfinder.R;
import be.xios.jobfinder.connector.LinkedInConnector;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.json.LinkedInJobParser;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.model.SearchBuilder;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class SearchResultFragment extends ListFragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "search_result_fragment";

	public static final String SEARCH_PARAMS = "search_parameters";  
	private JobListAdapter jobListAdapter;
	private SearchBuilder searchData;
	private JobFinderDAO datasource;
	private List<LinkedInJob> jobs;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchResultFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		searchData = bundle.getParcelable(SEARCH_PARAMS);

		jobListAdapter = new JobListAdapter(getActivity().getApplicationContext(),
				new ArrayList<LinkedInJob>());
		LinkedInJobSearch search = new LinkedInJobSearch(jobListAdapter);
		search.execute(searchData);

		setListAdapter(jobListAdapter);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_search_result, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save_searchparams:

			datasource = new JobFinderDAO(getActivity().getApplicationContext());
			datasource.open();
			long newID = datasource.createSavedSearch(searchData);
			if (newID > 0) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Favoriet toegevoegd met ID " + newID,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Favoriet toevoegen mislukt", Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.menu_mapview:
			Bundle b = new Bundle();
			Intent mapInt = new Intent(getActivity().getApplicationContext(),
					MapActivity.class);

			b.putParcelableArrayList(MapActivity.JOB_LIST,
					(ArrayList<? extends Parcelable>) jobs);
			mapInt.putExtras(b);
			startActivity(mapInt);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		LinkedInJob job = (LinkedInJob) l.getAdapter().getItem(position);
		Bundle arguments = new Bundle();
		arguments.putParcelable("selectedJob", job);
		
		if (getActivity().findViewById(R.id.menuitem_detail_container) != null) {
			JobDetailFragment fragment = new JobDetailFragment();
			fragment.setArguments(arguments);
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.menuitem_detail_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			Intent jobDetailIntent = new Intent(getActivity().getApplicationContext(), JobDetailActivity.class);
			jobDetailIntent.putExtras(arguments);
			startActivity(jobDetailIntent);
		}
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