package be.xios.jobfinder.main;

import java.util.ArrayList;
import java.util.List;

import be.xios.jobfinder.JobDetailActivity;
import be.xios.jobfinder.R;
import be.xios.jobfinder.data.JobFinderDAO;
import be.xios.jobfinder.data.JobListAdapter;
import be.xios.jobfinder.model.LinkedInJob;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FavoritesFragment extends ListFragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "saved_search_fragment";
	
	private JobFinderDAO datasource;
	private JobListAdapter jobListAdapter;
	
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

		ArrayAdapter<LinkedInJob> adapter = new ArrayAdapter<LinkedInJob>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
//		getActivity().registerForContextMenu(getListView());
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
		Bundle arguments = new Bundle();
		arguments.putParcelable("selectedJob", job);
		
		if (getActivity().findViewById(R.id.menuitem_detail_container) != null) {
			JobDetailFragment fragment = new JobDetailFragment();
			fragment.setArguments(arguments);
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.menuitem_detail_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			Intent jobDetailIntent = new Intent(v.getContext(), JobDetailActivity.class);
			jobDetailIntent.putExtras(arguments);
			startActivity(jobDetailIntent);
		}
	}
}