package be.xios.jobfinder;

import java.io.IOException;
import java.io.InputStream;

import org.scribe.model.Verb;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import be.xios.jobfinder.connector.LinkedInConnector;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.json.LinkedInCompanyParser;
import be.xios.jobfinder.json.LinkedInJobDetailParser;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.model.LinkedInJobDetail;

public class JobDetailActivity extends Activity {

	public static final String JOB_ID = "job_id";

	private TextView jobDescription;
	private TextView skills;
	private TextView companyDescription;
	private LinkedInJob currentJob;
	private JobFinderDAO datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_detail);

		Bundle bundle = getIntent().getExtras();
		currentJob = bundle.getParcelable("selectedJob");

		int jobId = currentJob.getId();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_favorite:

			datasource = new JobFinderDAO(getApplicationContext());
			datasource.open();
			long newID = datasource.createFavoriteJob(currentJob);
			if (newID > 0) {
				Toast.makeText(getApplicationContext(),
						"Favoriet toegevoegd met ID " + newID,
						Toast.LENGTH_LONG).show();
			} else if (newID == -1) {
				Toast.makeText(getApplicationContext(),
						"Favoriet bestaat reeds.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Favoriet toevoegen mislukt.", Toast.LENGTH_LONG)
						.show();
			}

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
				jobDetail.setCompanyDescription(companyParser
						.readJobDetailStream(in));
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