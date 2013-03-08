package be.xios.jobfinder.main;

import java.util.List;

import be.xios.jobfinder.R;
import be.xios.jobfinder.SearchResultActivity;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.model.SearchBuilder;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SavedSearchFragment extends ListFragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "saved_search_fragment";
	
	private JobFinderDAO datasource;
	
	public SavedSearchFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		datasource = new JobFinderDAO(getActivity().getApplicationContext());
		datasource.open();

		List<SearchBuilder> values = datasource.getAllSavedSearches();
		ArrayAdapter<SearchBuilder> adapter = new ArrayAdapter<SearchBuilder>(
				getActivity(), android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		SearchBuilder sb = (SearchBuilder) l.getAdapter().getItem(position);
		Bundle arguments = new Bundle();
		arguments.putParcelable(SearchResultActivity.SEARCH_PARAMS, sb);
		
		if (getActivity().findViewById(R.id.menuitem_detail_container) != null) {
			SearchResultFragment fragment = new SearchResultFragment();
			fragment.setArguments(arguments);
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.menuitem_detail_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			Intent searchInt = new Intent(getActivity().getApplicationContext(), SearchResultActivity.class);
			searchInt.putExtras(arguments);
			startActivity(searchInt);
		}
		
		
	}
	
}