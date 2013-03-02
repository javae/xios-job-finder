package be.xios.jobfinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.scribe.model.Verb;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import be.xios.jobfinder.connector.LinkedInConnector;
import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.json.LinkedInJobParser;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.model.SearchBuilder;

public class SearchResultActivity extends ListActivity {

	private JobListAdapter jobListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		SearchBuilder searchBuilder = bundle.getParcelable("searchdata");

		jobListAdapter = new JobListAdapter(getApplicationContext(), new ArrayList<LinkedInJob>());
		LinkedInJobSearch search = new LinkedInJobSearch(jobListAdapter);
		search.execute(searchBuilder);

		setListAdapter(jobListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_job_search, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		LinkedInJob job = (LinkedInJob) l.getAdapter().getItem(position);
		Intent jobDetailIntent = new Intent(getApplicationContext(), JobDetailActivity.class);
		jobDetailIntent.putExtra(JobDetailActivity.JOB_ID, job.getId());
		startActivity(jobDetailIntent);
	}
	
	private class LinkedInJobSearch extends AsyncTask<SearchBuilder, Void, List<LinkedInJob>> {

		String url = "http://api.linkedin.com/v1/job-search:(jobs:(id,posting-timestamp,company:(name),position:(title,location)))";
		JobListAdapter jobListAdapter;
		
		public LinkedInJobSearch(JobListAdapter jobListAdapter) {
			this.jobListAdapter = jobListAdapter;
		}
		
		@Override
		protected List<LinkedInJob> doInBackground(SearchBuilder... params) {
			List<LinkedInJob> jobs = new ArrayList<LinkedInJob>();
			
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