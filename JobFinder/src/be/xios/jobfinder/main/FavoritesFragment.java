package be.xios.jobfinder.main;

import java.util.ArrayList;
import java.util.List;

import be.xios.jobfinder.main.JobDetailActivity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import be.xios.jobfinder.R;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.model.LinkedInJob;

public class FavoritesFragment extends ListFragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "saved_search_fragment";
	
	private static final int myContextmenu_id = 1;
	private static final int openMenuItem_Id = 2;
	private static final int deleteMenuItem_Id = 3;
	
	private JobFinderDAO datasource;
	private JobListAdapter jobListAdapter;
	private ArrayAdapter<LinkedInJob> adapter;
	
	public FavoritesFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActivity().setTitle(R.string.title_activity_favorites);

		datasource = new JobFinderDAO(getActivity().getApplicationContext());
		datasource.open();

		List<LinkedInJob> values = datasource.getAllFavJobs();

		jobListAdapter = new JobListAdapter(getActivity().getApplicationContext(),
				new ArrayList<LinkedInJob>());
		jobListAdapter.addAll(values);
		setListAdapter(jobListAdapter);

		adapter = new ArrayAdapter<LinkedInJob>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
//		getActivity().registerForContextMenu(getListView());
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		registerForContextMenu(getListView());
		super.onActivityCreated(savedInstanceState);
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		//int position = info.position;
		menu.setHeaderTitle("Kies een actie");
		menu.add(myContextmenu_id, openMenuItem_Id, 1, "Open");
		menu.add(myContextmenu_id, deleteMenuItem_Id, 2, "Verwijder");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		LinkedInJob selectedJob;

		switch (item.getItemId()) {
		case openMenuItem_Id:
			selectedJob = (LinkedInJob) getListView().getAdapter().getItem(
					info.position);
			
			openJobDetail(selectedJob);
			break;
		case deleteMenuItem_Id:
			selectedJob = (LinkedInJob) getListView().getAdapter().getItem(
					info.position);
			datasource.deleteFavJob(selectedJob);
			adapter.notifyDataSetChanged();
			//TODO refresh werkt nog niet?
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
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
		
		LinkedInJob job = (LinkedInJob) l.getAdapter().getItem(position);
		openJobDetail(job);
	}

	private void openJobDetail(LinkedInJob job) {
		Bundle arguments = new Bundle();
		arguments.putParcelable(JobDetailFragment.JOB_SELECTED, job);
		
		if (getActivity().findViewById(R.id.menuitem_detail_container) != null) {
			JobDetailFragment fragment = new JobDetailFragment();
			fragment.setArguments(arguments);
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.menuitem_detail_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			Intent jobDetailIntent = new Intent(getActivity().getApplicationContext(), JobDetailActivity.class);
			jobDetailIntent.putExtras(arguments);
			startActivity(jobDetailIntent);
		}
	}
}