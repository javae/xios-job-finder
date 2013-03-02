package be.xios.jobfinder;

import java.io.IOException;
import java.io.InputStream;

import org.scribe.model.Verb;

import be.xios.jobfinder.connector.LinkedInConnector;
import be.xios.jobfinder.json.LinkedInCompanyParser;
import be.xios.jobfinder.json.LinkedInJobDetailParser;
import be.xios.jobfinder.model.LinkedInJobDetail;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class JobDetailActivity extends Activity {

	public static final String JOB_ID = "job_id";
	
	private TextView jobDescription;
	private TextView skills;
	private TextView companyDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_detail);
		
		Intent jobDetailIntent = getIntent();
		int jobId = jobDetailIntent.getIntExtra(JOB_ID, 0);
		JobDetailLookup jobDetailLookup = new JobDetailLookup(jobId);
		jobDetailLookup.execute();
		
		jobDescription = (TextView) findViewById(R.id.tvJobDesc_content);
		skills = (TextView) findViewById(R.id.tvSkills_content);
		companyDescription = (TextView) findViewById(R.id.tvCompanyDesc_content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_job_detail, menu);
		return true;
	}

	private class JobDetailLookup extends AsyncTask<Void, Void, Void> {

		private String url;
		private String jobBaseUrl = "http://api.linkedin.com/v1/jobs/";
		private String jobFieldsUrl = ":(id,description,skills-and-experience,company:(id))";
		private String companyBaseUrl = "http://api.linkedin.com/v1/companies/";
		private String companyFieldsUrl = ":(description)";
		
		private LinkedInJobDetail jobDetail;
		
		public JobDetailLookup(int id) {
			url = jobBaseUrl + id + jobFieldsUrl;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			LinkedInConnector connector = new LinkedInConnector();
			InputStream in = connector.sendRequest(Verb.GET, url);
			
			LinkedInJobDetailParser jobDetailParser = new LinkedInJobDetailParser();
			try {
				jobDetail = jobDetailParser.readJobDetailStream(in);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			url = companyBaseUrl + jobDetail.getCompanyId() + companyFieldsUrl;
			in = connector.sendRequest(Verb.GET, url);
			LinkedInCompanyParser companyParser = new LinkedInCompanyParser();
			try {
				jobDetail.setCompanyDescription(companyParser.readJobDetailStream(in));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			jobDescription.setText(jobDetail.getJobDescription());
			skills.setText(jobDetail.getSkills());
			companyDescription.setText(jobDetail.getCompanyDescription());
		}
	}
}