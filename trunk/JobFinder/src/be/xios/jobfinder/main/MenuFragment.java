package be.xios.jobfinder.main;

import be.xios.jobfinder.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * A list fragment representing a list of MenuItems. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link MenuItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MenuFragment extends Fragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	
	public static final String NEW_SEARCH = "new_search";
	public static final String SAVED_SEARCH = "saved_search";
	public static final String FAVORITES = "favorites";
	public static final String SETTINGS = "settings";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	private Button newSearch;
	private Button savedSearches;
	private Button favorites;
	private Button settings;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MenuFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);
		
		newSearch = (Button) view.findViewById(R.id.btn_new_search);
        savedSearches = (Button) view.findViewById(R.id.btn_saved_searches);
        favorites = (Button) view.findViewById(R.id.btn_favorites);
        settings = (Button) view.findViewById(R.id.btn_settings);
        
        newSearch.setOnClickListener(new NewSearchHandler());
        savedSearches.setOnClickListener(new SavedSearchesHandler());
        favorites.setOnClickListener(new FavoritesHandler());
        settings.setOnClickListener(new SettingsHandler());
		
	    return view;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
//		getListView().setChoiceMode(
//				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
//						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
//		if (position == ListView.INVALID_POSITION) {
//			getListView().setItemChecked(mActivatedPosition, false);
//		} else {
//			getListView().setItemChecked(position, true);
//		}
//
		mActivatedPosition = position;
	}
	
	
	/**
	 * Notify the active callbacks interface (the activity, if the 
	 * fragment is attached to one) that an item has been selected.
	 * @author tom
	 *
	 */
	private class NewSearchHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			newSearch.setActivated(true);
			savedSearches.setActivated(false);
			favorites.setActivated(false);
			settings.setActivated(false);
			mCallbacks.onItemSelected(NEW_SEARCH);
		}
    }
    
    private class SavedSearchesHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			newSearch.setActivated(false);
			savedSearches.setActivated(true);
			favorites.setActivated(false);
			settings.setActivated(false);
			mCallbacks.onItemSelected(SAVED_SEARCH);
		}
    }
    
    private class FavoritesHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			newSearch.setActivated(false);
			savedSearches.setActivated(false);
			favorites.setActivated(true);
			settings.setActivated(false);
			mCallbacks.onItemSelected(FAVORITES);
		}
    }
    
    private class SettingsHandler implements OnClickListener {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			newSearch.setActivated(false);
			savedSearches.setActivated(false);
			favorites.setActivated(false);
			settings.setAllCaps(true);
			mCallbacks.onItemSelected(SETTINGS);
		}
    }
}
