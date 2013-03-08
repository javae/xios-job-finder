package be.xios.jobfinder.main;

import java.util.List;
import java.util.Locale;

import be.xios.jobfinder.R;
import be.xios.jobfinder.main.SearchResultActivity;
import be.xios.jobfinder.data.Country;
import be.xios.jobfinder.data.CountryData;
import be.xios.jobfinder.model.SearchBuilder;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SearchBuilderFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "search_fragment";

	private EditText keywords;
	private EditText jobTitle;
	private Spinner country;
	private EditText postalCode;
	private SeekBar distance;
	private Spinner functions;
	private Spinner industries;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchBuilderFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_search_params,
				container, false);

		Button btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new ButtonHandler());
		//btnSearch.setOnLongClickListener(new LongClickHandler());
		ImageButton btnGetPC = (ImageButton) rootView.findViewById(R.id.btnGetLocation);
		btnGetPC.setOnClickListener(new ButtonHandler());
		
		keywords = (EditText) rootView.findViewById(R.id.etKeywords);
		jobTitle = (EditText) rootView.findViewById(R.id.etJobTitle);
		country = (Spinner) rootView.findViewById(R.id.spCountry);
		postalCode = (EditText) rootView.findViewById(R.id.etPostalCode);
		distance = (SeekBar) rootView.findViewById(R.id.sbDistance);
		functions = (Spinner) rootView.findViewById(R.id.spFunctions);
		industries = (Spinner) rootView.findViewById(R.id.spIndustry);
		
		addItemsToCountrySpinner();
		distance.setOnSeekBarChangeListener(new SeekBarHandler());
		
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_search_params, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void setLocationFields() {
		String countryCode = null;

		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(false);
		criteria.setSpeedRequired(false);
		criteria.setBearingRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);

		Geocoder gc = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
		try {
			List<Address> addresses = gc.getFromLocation(
					location.getLatitude(), location.getLongitude(), 1);

			if (addresses.size() > 0) {
				postalCode.setText(addresses.get(0).getPostalCode());
				
				//messy.. but it works
				countryCode = addresses.get(0).getCountryName();
				List<Country> list = CountryData.getCountryList();
				Country[] array = list.toArray(new Country[list.size()]);
				for (int i = 0; i < array.length; i++) {
					if (countryCode.equalsIgnoreCase(array[i].toString())) {
						country.setSelection(i);
						break;
					}
				}
			}
		} catch (Exception e) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Locatie kon niet worden bepaald.", Toast.LENGTH_SHORT)
					.show();
			//e.printStackTrace();
		}
	}

	private void addItemsToCountrySpinner() {
		List<Country> list = CountryData.getCountryList();
		ArrayAdapter<Country> dataAdapter = new ArrayAdapter<Country>(getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		country.setAdapter(dataAdapter);
	}

	private class ButtonHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnSearch:
				Intent searchInt = new Intent(getActivity().getApplicationContext(), SearchResultActivity.class);
				SearchBuilder sb = createSearchBuilderFromForm();
				
				Bundle arguments = new Bundle();
				arguments.putParcelable(SearchResultFragment.SEARCH_PARAMS, sb);
				
				if (getActivity().findViewById(R.id.menuitem_detail_container) != null) {
					SearchResultFragment fragment = new SearchResultFragment();
					fragment.setArguments(arguments);
					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.menuitem_detail_container, fragment);
					transaction.addToBackStack(null);
					transaction.commit();
				} else {
					searchInt.putExtras(arguments);
					startActivity(searchInt);
				}
				
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

		String keywordsValue = keywords.getText().toString();
		String jobtitleValue = jobTitle.getText().toString();
		String postalcodeValue = postalCode.getText().toString();
		String countryValue = CountryData.getCountryList().get(country.getSelectedItemPosition()).getIso2();
		
		// distance from kilometer to miles
		// * 5 so it is the same as in the label
		int dist = (int) ((distance.getProgress() * 5) * 0.621371);
		String function = getResources().getStringArray(R.array.job_function_codes)[functions.getSelectedItemPosition()];
		String industry = getResources().getStringArray(R.array.industry_codes)[industries.getSelectedItemPosition()];

		sb.setKeywords(keywordsValue);
		sb.setJobTitle(jobtitleValue);
		sb.setPostalCode(postalcodeValue);
		sb.setCountryCode(countryValue);
		sb.setDistance(dist);
		sb.setJobFunction(function);
		sb.setIndustry(industry);

		return sb;
	}
	
	private class SeekBarHandler implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			TextView tvDistance = (TextView) getActivity().findViewById(R.id.tvDistValue);

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