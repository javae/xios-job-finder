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
	private SearchBuilder searchData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// retrieve SearchBuilder object from SearchBuilderActivity through intents
		
		Bundle bundle = getIntent().getExtras();
		// SearchBuilder searchBuilder = bundle.getParcelable("searchdata");

		SearchBuilder searchBuilder = new SearchBuilder();
		searchBuilder.setKeywords("java");
		//				searchBuilder.setJobTitle("programmer");
		//				searchBuilder.setCountryCode("be");
		//				searchBuilder.setPostalCode("3000");
		//				searchBuilder.setDistance(10);
		//				searchBuilder.setIndustry("Banking");
		//				searchBuilder.setJobFunction("Information Technology");

		/*
				JSONObject jobObject = new JSONObject();
				try {
					jobObject.put("keywords", searchBuilder.getKeywords());
					jobObject.put("job-title", searchBuilder.getJobTitle());
					jobObject.put("country-code", searchBuilder.getCountryCode());
					jobObject.put("postal-code", searchBuilder.getPostalCode());
					jobObject.put("distance", 0);//searchBuilder.getDistance());
					if (searchBuilder.getIndustry() != null && searchBuilder.getIndustry().length() > 0)
						jobObject.put("facet", "industry," + searchBuilder.getIndustry() );
					if (searchBuilder.getJobFunction() != null && searchBuilder.getJobFunction().length() > 0)
						jobObject.put("facet", "job-function," + searchBuilder.getJobFunction());
				} catch (JSONException jsone) {
					jsone.printStackTrace();
				}
		 */

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
	
	public List<LinkedInJob> createTestData() throws ParseException {
		List<LinkedInJob> testData = new ArrayList<LinkedInJob>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD", Locale.getDefault());
		
		int id = 1;
		String positionTitle = "position1";
		String companyName = "company1";
		String location = "Brussel";
		Date postingDate = format.parse("2013-02-13");
		LinkedInJob job1 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 2;
		positionTitle = "position2";
		companyName = "company2";
		location = "Leuven";
		postingDate = format.parse("2013-02-14");
		LinkedInJob job2 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 3;
		positionTitle = "position3";
		companyName = "company3";
		location = "Diestesteenweg 304, Kessel-Lo";
		postingDate = format.parse("2013-02-15");
		LinkedInJob job3 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 4;
		positionTitle = "position4";
		companyName = "company4";
		location = "Aarschot";
		postingDate = format.parse("2013-02-16");
		LinkedInJob job4 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 5;
		positionTitle = "position5";
		companyName = "company5";
		location = "Brussels Area";
		postingDate = format.parse("2013-02-17");
		LinkedInJob job5 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		testData.add(job1);
		testData.add(job2);
		testData.add(job3);
		testData.add(job4);
		testData.add(job5);
		
		return testData;
	}

}
