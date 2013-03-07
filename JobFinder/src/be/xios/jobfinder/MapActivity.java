package be.xios.jobfinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import be.xios.jobfinder.model.LinkedInJob;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements LocationListener, OnMarkerClickListener {

	private LocationManager locationManager;
	private String provider;
	private GoogleMap map;
	public static final String JOB_LIST = "joblisting";
	private Builder bounds = new LatLngBounds.Builder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		// .getMap();
		Intent i = getIntent();
		ArrayList<LinkedInJob> jobListing = new ArrayList<LinkedInJob>();
		jobListing = i.getParcelableArrayListExtra(JOB_LIST);
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

		map.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				// Move camera.
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(
						bounds.build(), 30));
				// Remove listener to prevent position reset on camera move.
				map.setOnCameraChangeListener(null);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void addMarkersToMap(List<LinkedInJob> jobs) {
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());

		int i = 0;

		for (LinkedInJob job : jobs) {
			try {
				List<Address> addresses = gc.getFromLocationName(
						job.getLocation(), 1);

				if (addresses.size() > 0) {
					LatLng ll = new LatLng(addresses.get(0).getLatitude(),
							addresses.get(0).getLongitude());
					bounds.include(ll);
					map.addMarker(new MarkerOptions().position(ll).title(
							job.getPositionTitle()));
				} else {
					i++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (i > 0) {
			Toast.makeText(getApplicationContext(),
					i + " locaties konden niet worden gevonden.",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onMarkerClick(Marker myMarker) {
		// TODO object of dergelijke meegeven in een marker?
		Toast.makeText(getApplicationContext(), myMarker.getId().toString() + " clicked", Toast.LENGTH_LONG).show();
		return false;
	}
}
