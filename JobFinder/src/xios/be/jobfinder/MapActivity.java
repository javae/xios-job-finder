package xios.be.jobfinder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(true);
		criteria.setSpeedRequired(false);
		criteria.setBearingRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		provider = locationManager.getBestProvider(criteria, true);
		Toast.makeText(getApplicationContext(), provider.toString(),
				Toast.LENGTH_LONG).show();
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			onLocationChanged(location);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
		map.addMarker(new MarkerOptions().position(ll).title("You're here."));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));

		// Geocoder gc = new Geocoder(getApplicationContext(),
		// Locale.getDefault());
		//
		// try {
		// List<Address> addresses = gc.getFromLocationName(
		// "Lazarijstraat 141 Hasselt", 1);
		//
		// if (addresses.size() > 0) {
		// LatLng position = new LatLng(addresses.get(0).getLatitude(),
		// addresses.get(0).getLongitude());
		// map.addMarker(new MarkerOptions().position(position).title("Home"));
		// map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
		// // mapView.invalidate();
		//
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		
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

}
