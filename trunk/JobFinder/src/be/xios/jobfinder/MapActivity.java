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
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import be.xios.jobfinder.model.LinkedInJob;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements LocationListener {

	private GoogleMap map;
	public static final String JOB_LIST = "joblisting";
	private Builder bounds = new LatLngBounds.Builder();

	// TODO someday - hashmap met markerID en jobID, en een functie GetJobByID
	// die de linkedinjob kan ophalen
	
	// private HashMap<Marker, LinkedInJob> markerMap = new HashMap<Marker,
	// LinkedInJob>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		Intent i = getIntent();
		ArrayList<LinkedInJob> jobListing = new ArrayList<LinkedInJob>();
		jobListing = i.getParcelableArrayListExtra(JOB_LIST);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		addMarkersToMap(jobListing, map);

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

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				Toast.makeText(getApplicationContext(),
						marker.getId().toString() + " clicked",
						Toast.LENGTH_LONG).show();
				// om detail te openen vanuit marker - werkt te vertragend
				// if (markerMap.containsKey(marker)) {
				// LinkedInJob selectedJob = markerMap.get(marker);
				// openJobDetail(selectedJob);
				// }

			}
		});

	}

	// private void openJobDetail(LinkedInJob job) {
	// Bundle arguments = new Bundle();
	// arguments.putParcelable(JobDetailFragment.JOB_SELECTED, job);
	//
	// if (getActivity().findViewById(R.id.menuitem_detail_container) != null) {
	// JobDetailFragment fragment = new JobDetailFragment();
	// fragment.setArguments(arguments);
	//
	// FragmentTransaction transaction =
	// getFragmentManager().beginTransaction();
	// transaction.replace(R.id.menuitem_detail_container, fragment);
	// transaction.addToBackStack(null);
	// transaction.commit();
	// } else {
	// Intent jobDetailIntent = new Intent(getApplicationContext(),
	// JobDetailActivity.class);
	// jobDetailIntent.putExtras(arguments);
	// startActivity(jobDetailIntent);
	// }

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

	public void addMarkersToMap(List<LinkedInJob> jobs, GoogleMap map) {

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

					// If the marker isn't already being displayed
					// if (!markerMap.containsKey(job.getId())) {
					// markerMap.put(map
					// .addMarker(new MarkerOptions().position(ll)
					// .title(job.getPositionTitle())), job);
					// }
					map.addMarker(new MarkerOptions().position(ll).title(
							job.getPositionTitle()));

				} else {
					i++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (i > 0) {
			Toast.makeText(getApplicationContext(),
					i + " locaties konden niet worden gevonden.",
					Toast.LENGTH_LONG).show();
		}

	}
}
