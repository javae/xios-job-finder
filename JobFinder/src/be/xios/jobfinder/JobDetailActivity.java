package be.xios.jobfinder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class JobDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_detail);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_job_detail, menu);
		return true;
	}

}
