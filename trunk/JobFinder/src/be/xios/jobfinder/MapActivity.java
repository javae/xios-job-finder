package be.xios.jobfinder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import be.xios.jobfinder.R;
import be.xios.jobfinder.model.LinkedInJob;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.Toast;

public class MapActivity extends Activity implements LocationListener {

	private LocationManager locationManager;
	private String provider;
	private GoogleMap map;
	public static final String JOB_LIST = "joblisting";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		// .getMap();

		Bundle b = getIntent().getExtras();
		List<LinkedInJob> jobListing = b.getParcelableArrayList("JOB_LIST");

		addMarkersToMap(jobListing);

		// // Get the location manager
		// locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		//
		// // Define the criteria how to select the location provider -> use
		// // default
		// Criteria criteria = new Criteria();
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// criteria.setAltitudeRequired(false);
		// criteria.setCostAllowed(true);
		// criteria.setSpeedRequired(false);
		// criteria.setBearingRequired(false);
		// criteria.setPowerRequirement(Criteria.POWER_HIGH);
		// provider = locationManager.getBestProvider(criteria, true);
		// Toast.makeText(getApplicationContext(), provider.toString(),
		// Toast.LENGTH_LONG).show();
		// Location location = locationManager.getLastKnownLocation(provider);
		//
		// // Initialize the location fields
		// if (location != null) {
		// onLocationChanged(location);
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// LatLng ll = new LatLng(location.getLatitude(),
		// location.getLongitude());
		// map.addMarker(new
		// MarkerOptions().position(ll).title("You're here."));
		// map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void addMarkersToMap(List<LinkedInJob> jobs) {
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());

		for (LinkedInJob job : jobs) {
			try {
				List<Address> addresses = gc.getFromLocationName(job.getLocation(), 1);

				if (addresses.size() > 0) {
					LatLng ll = new LatLng(addresses.get(0).getLatitude(),
							addresses.get(0).getLongitude());
					map.addMarker(new MarkerOptions().position(ll).title(
							job.getPositionTitle()));
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
