package be.xios.jobfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import be.xios.jobfinder.data.Country;
import be.xios.jobfinder.data.CountryData;
import be.xios.jobfinder.model.SearchBuilder;

public class SearchBuilderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_params);
		addItemsToCountrySpinner();

		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new ButtonHandler());
		ImageButton btnGetPC = (ImageButton) findViewById(R.id.btnGetLocation);
		btnGetPC.setOnClickListener(new ButtonHandler());

		SeekBar sbDistance = (SeekBar) findViewById(R.id.sbDistance);
		sbDistance.setOnSeekBarChangeListener(new SeekBarHandler());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_params, menu);
		return true;
	}

	private void setLocationFields() {
		String postalCode = null;
		String countryCode = null;

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(false);
		criteria.setSpeedRequired(false);
		criteria.setBearingRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);

		Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
		try {
			List<Address> addresses = gc.getFromLocation(
					location.getLatitude(), location.getLongitude(), 1);

			if (addresses.size() > 0) {
				EditText et = (EditText) findViewById(R.id.etPostalCode);
				et.setText(addresses.get(0).getPostalCode());
				
				//messy.. but it works
				countryCode = addresses.get(0).getCountryName();
				Spinner sp = (Spinner) findViewById(R.id.spCountry);
				List<Country> list = CountryData.getCountryList();
				Country[] array = list.toArray(new Country[list.size()]);
				for (int i = 0; i < array.length; i++) {
					if (countryCode.equalsIgnoreCase(array[i].toString())) {
						sp.setSelection(i);
						break;
					}
				}
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Locatie kon niet worden bepaald.", Toast.LENGTH_SHORT)
					.show();
			// e.printStackTrace();
		}

	}

	private void addItemsToCountrySpinner() {
		Spinner spCountry = (Spinner) findViewById(R.id.spCountry);
		List<Country> list = CountryData.getCountryList();
		ArrayAdapter<Country> dataAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCountry.setAdapter(dataAdapter);
	}

	private class ButtonHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnSearch:
				Intent searchInt = new Intent(getApplicationContext(),
						SearchResultActivity.class);

				SearchBuilder sb = createSearchBuilderFromForm();
				Bundle b = new Bundle();
				b.putParcelable(SearchResultActivity.SEARCH_PARAMS, sb);
				searchInt.putExtras(b);

				startActivity(searchInt);
				break;
			case R.id.btnGetLocation:
				setLocationFields();
				break;
			default:
				break;
			}

		}
	}

	private SearchBuilder createSearchBuilderFromForm() {
		SearchBuilder sb = new SearchBuilder();

		EditText et = (EditText) findViewById(R.id.etKeywords);
		String keywords = et.getText().toString();
		et = (EditText) findViewById(R.id.etJobTitle);
		String jobtitle = et.getText().toString();
		Spinner sp = (Spinner) findViewById(R.id.spCountry);
		String country = CountryData.getCountryList()
				.get(sp.getSelectedItemPosition()).getIso2();

		SeekBar sbDist = (SeekBar) findViewById(R.id.sbDistance);
		// distance from kilometer to miles
		// * 5 so it is the same as in the label
		int dist = (int) ((sbDist.getProgress() * 5) * 0.621371);
		sp = (Spinner) findViewById(R.id.spFunctions);
		String function = getResources().getStringArray(
				R.array.job_function_codes)[sp.getSelectedItemPosition()];
		sp = (Spinner) findViewById(R.id.spIndustry);
		String industry = getResources().getStringArray(R.array.industry_codes)[sp
				.getSelectedItemPosition()];

		sb.setKeywords(keywords);
		sb.setJobTitle(jobtitle);
		sb.setCountryCode(country);
		sb.setDistance(dist);
		sb.setJobFunction(function);
		sb.setIndustry(industry);

		return sb;
	}

	private class SeekBarHandler implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			TextView tvDistance = (TextView) findViewById(R.id.tvDistValue);

			if (progress == 0) {
				tvDistance.setText("");
			} else {
				int distance = progress * 5;
				tvDistance.setText(" " + distance + " km");
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

	}

}
