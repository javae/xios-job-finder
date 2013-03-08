package be.xios.jobfinder.main;

import java.util.List;

import be.xios.jobfinder.R;
import be.xios.jobfinder.main.SearchResultActivity;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.model.SearchBuilder;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SavedSearchFragment extends ListFragment {

	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "saved_search_fragment";

	private static final int myContextmenu_id = 1;
	private static final int openMenuItem_Id = 2;
	private static final int deleteMenuItem_Id = 3;
	private JobFinderDAO datasource;
	private ArrayAdapter<SearchBuilder> adapter;

	public SavedSearchFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		datasource = new JobFinderDAO(getActivity().getApplicationContext());
		datasource.open();

		List<SearchBuilder> values = datasource.getAllSavedSearches();
		adapter = new ArrayAdapter<SearchBuilder>(getActivity(),
				android.R.layout.simple_list_item_1, values);
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
		openSavedSearch(sb);

	}

	private void openSavedSearch(SearchBuilder sb) {
		Bundle arguments = new Bundle();
		arguments.putParcelable(SearchResultFragment.SEARCH_PARAMS, sb);

		if (getActivity().findViewById(R.id.menuitem_detail_container) != null) {
			SearchResultFragment fragment = new SearchResultFragment();
			fragment.setArguments(arguments);

			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.menuitem_detail_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			Intent searchInt = new Intent(
					getActivity().getApplicationContext(),
					SearchResultActivity.class);
			searchInt.putExtras(arguments);
			startActivity(searchInt);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		registerForContextMenu(getListView());
		super.onActivityCreated(savedInstanceState);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		// int position = info.position;
		menu.setHeaderTitle("Kies een actie");
		menu.add(myContextmenu_id, openMenuItem_Id, 1, "Open");
		menu.add(myContextmenu_id, deleteMenuItem_Id, 2, "Verwijder");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		SearchBuilder selectedSearch;

		switch (item.getItemId()) {
		case openMenuItem_Id:
			selectedSearch = (SearchBuilder) getListView().getAdapter()
					.getItem(info.position);
			openSavedSearch(selectedSearch);
			break;

		case deleteMenuItem_Id:
			selectedSearch = (SearchBuilder) getListView().getAdapter()
					.getItem(info.position);
			datasource.deleteSavedSearch(selectedSearch);
			adapter.notifyDataSetChanged();
			// TODO refresh werkt nog niet?
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

}