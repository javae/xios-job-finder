package be.xios.jobfinder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import be.xios.jobfinder.R;

import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.model.LinkedInJob;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;

public class JobSearchActivity extends ListActivity {

	private JobListAdapter jobListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			jobListAdapter = new JobListAdapter(this, createTestData());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		setListAdapter(jobListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_job_search, menu);
		return true;
	}
	
	public List<LinkedInJob> createTestData() throws ParseException {
		List<LinkedInJob> testData = new ArrayList<LinkedInJob>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD", Locale.getDefault());
		
		int id = 1;
		String positionTitle = "position1";
		String companyName = "company1";
		String location = "location1";
		GregorianCalendar postingDate = new GregorianCalendar();
		postingDate.setTime(format.parse("2013-02-13"));
		LinkedInJob job1 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 2;
		positionTitle = "position2";
		companyName = "company2";
		location = "location2";
		postingDate = new GregorianCalendar();
		postingDate.setTime(format.parse("2013-02-14"));
		LinkedInJob job2 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 3;
		positionTitle = "position3";
		companyName = "company3";
		location = "location3";
		postingDate = new GregorianCalendar();
		postingDate.setTime(format.parse("2013-02-15"));
		LinkedInJob job3 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 4;
		positionTitle = "position4";
		companyName = "company4";
		location = "location4";
		postingDate = new GregorianCalendar();
		postingDate.setTime(format.parse("2013-02-16"));
		LinkedInJob job4 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		id = 5;
		positionTitle = "position5";
		companyName = "company5";
		location = "location5";
		postingDate = new GregorianCalendar();
		postingDate.setTime(format.parse("2013-02-17"));
		LinkedInJob job5 = new LinkedInJob(id, positionTitle, companyName, location, postingDate);
		
		testData.add(job1);
		testData.add(job2);
		testData.add(job3);
		testData.add(job4);
		testData.add(job5);
		
		return testData;
	}

}
