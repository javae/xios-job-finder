package be.xios.jobfinder.main;

import java.io.IOException;
import java.io.InputStream;

import org.scribe.model.Verb;

import be.xios.jobfinder.R;
import be.xios.jobfinder.connector.LinkedInConnector;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.json.LinkedInCompanyParser;
import be.xios.jobfinder.json.LinkedInJobDetailParser;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.model.LinkedInJobDetail;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class JobDetailFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "search_result_fragment";
	
	public static final String JOB_ID = "job_id";
	public static final String JOB_SELECTED = "selected_job";
	
	private TextView jobDescription;
	private TextView skills;
	private TextView companyDescription;
	private LinkedInJob currentJob;
	private JobFinderDAO datasource;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public JobDetailFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_job_detail, container, false);
		
		Bundle bundle = getArguments();
		currentJob = bundle.getParcelable(JOB_SELECTED);
		
		TextView header = (TextView)rootView.findViewById(R.id.tvJobHeader);
		String headerText = currentJob.getPositionTitle() + " @ " + currentJob.getCompanyName();
		header.setText(headerText);
		
		int jobId = currentJob.getId();
		JobDetailLookup jobDetailLookup = new JobDetailLookup(jobId);
		jobDetailLookup.execute();
		
		jobDescription = (TextView) rootView.findViewById(R.id.tvJobDesc_content);
		skills = (TextView) rootView.findViewById(R.id.tvSkills_content);
		companyDescription = (TextView) rootView.findViewById(R.id.tvCompanyDesc_content);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_job_detail, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_favorite:
			
			datasource = new JobFinderDAO(getActivity().getApplicationContext());
			datasource.open();
			long newID = datasource.createFavoriteJob(currentJob);
			if (newID > 0) {
				Toast.makeText(getActivity().getApplicationContext(), "Favoriet toegevoegd met ID " + newID, Toast.LENGTH_LONG).show();
			}  if (newID == -1) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Favoriet bestaat reeds.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
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