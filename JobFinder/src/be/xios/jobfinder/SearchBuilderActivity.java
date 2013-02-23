package be.xios.jobfinder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
import android.os.Parcelable;
import android.provider.SyncStateContract.Constants;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import be.xios.jobfinder.data.Country;
import be.xios.jobfinder.data.CountryData;
import be.xios.jobfinder.model.LinkedInJob;

public class SearchBuilderActivity extends Activity {

	// private Button btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_params);
		addItemsToCountrySpinner();

		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new ButtonHandler());
		btnSearch.setOnLongClickListener(new LongClickHandler());
		Button btnGetPC = (Button) findViewById(R.id.btnGetLocation);
		btnGetPC.setOnClickListener(new ButtonHandler());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_params, menu);
		return true;
	}

	private String getPostalCodeFromCurrentLocation() {
		String postalCode = "Not found";

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
				postalCode = addresses.get(0).getPostalCode();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postalCode;

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
						JobSearchActivity.class);
				startActivity(searchInt);
				break;
			case R.id.btnGetLocation:
				EditText etPostalCode = (EditText) findViewById(R.id.etPostalCode);
				etPostalCode.setText(getPostalCodeFromCurrentLocation());
				break;
			default:
				break;
			}

		}
	}
	
	private List<LinkedInJob> createTestData() throws ParseException {
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
	
	private class LongClickHandler implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			List<LinkedInJob> jobListings = null;
			Bundle b = new Bundle();
			Intent mapInt = new Intent(getApplicationContext(), MapActivity.class);
			try {
				jobListings = createTestData();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			b.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) jobListings);
			mapInt.putExtras(b);
			startActivity(mapInt);
			return false;
		}
		
	}
}
