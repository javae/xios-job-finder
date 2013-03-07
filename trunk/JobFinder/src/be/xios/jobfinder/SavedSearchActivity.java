package be.xios.jobfinder;

import java.util.List;

import be.xios.jobfinder.R;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.model.SearchBuilder;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class SavedSearchActivity extends ListActivity {
	private JobFinderDAO datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_search);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		datasource = new JobFinderDAO(getApplicationContext());
		datasource.open();

		List<SearchBuilder> values = datasource.getAllSavedSearches();
		ArrayAdapter<SearchBuilder> adapter = new ArrayAdapter<SearchBuilder>(
				this, android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_saved_search, menu);
		return true;
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
		
		Intent searchInt = new Intent(getApplicationContext(),
				SearchResultActivity.class);

		SearchBuilder sb = (SearchBuilder) l.getAdapter().getItem(position);
		
		Bundle b = new Bundle();
		b.putParcelable(SearchResultActivity.SEARCH_PARAMS, sb);
		searchInt.putExtras(b);

		startActivity(searchInt);
	}
}
