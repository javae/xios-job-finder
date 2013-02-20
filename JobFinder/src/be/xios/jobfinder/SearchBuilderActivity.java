package be.xios.jobfinder;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SearchBuilderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_params);
		addItemsToCountrySpinner();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_params, menu);
		return true;
	}

	private void addItemsToCountrySpinner() {
		 
		Spinner spCountry = (Spinner) findViewById(R.id.spCountry);
		List<Country> list = CountryData.getCountryList();
		ArrayAdapter<Country> dataAdapter = new ArrayAdapter<Country>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCountry.setAdapter(dataAdapter);
	  }
}
