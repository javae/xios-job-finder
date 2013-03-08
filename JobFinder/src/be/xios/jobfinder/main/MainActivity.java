package be.xios.jobfinder.main;

import be.xios.jobfinder.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * An activity representing a list of MenuItems. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MenuItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MenuFragment} and the item details (if present) is a
 * {@link MenuItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MenuFragment.Callbacks} interface to listen for item
 * selections.
 */
public class MainActivity extends Activity implements MenuFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menuitem_list);

		if (findViewById(R.id.menuitem_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MenuFragment) getFragmentManager()
					.findFragmentById(R.id.menuitem_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link MenuFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			
			if (MenuFragment.NEW_SEARCH.equals(id)) {
				arguments.putString(SearchBuilderFragment.ARG_ITEM_ID, id);
				SearchBuilderFragment fragment = new SearchBuilderFragment();
				fragment.setArguments(arguments);
				getFragmentManager().beginTransaction()
				.replace(R.id.menuitem_detail_container, fragment).commit();
				
			} else if (MenuFragment.SAVED_SEARCH.equals(id)) {
				arguments.putString(SavedSearchFragment.ARG_ITEM_ID, id);
				SavedSearchFragment fragment = new SavedSearchFragment();
				fragment.setArguments(arguments);
				getFragmentManager().beginTransaction()
				.replace(R.id.menuitem_detail_container, fragment).commit();
				
			} else if (MenuFragment.FAVORITES.equals(id)) {
				arguments.putString(SavedSearchFragment.ARG_ITEM_ID, id);
				FavoritesFragment fragment = new FavoritesFragment();
				fragment.setArguments(arguments);
				getFragmentManager().beginTransaction()
				.replace(R.id.menuitem_detail_container, fragment).commit();
				
			} else if (MenuFragment.SETTINGS.equals(id)) {
				arguments.putString(SearchBuilderFragment.ARG_ITEM_ID, id);
				SettingsFragment fragment = new SettingsFragment();
				fragment.setArguments(arguments);
				getFragmentManager().beginTransaction()
				.replace(R.id.menuitem_detail_container, fragment).commit();
				
			}

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent;
			if (MenuFragment.NEW_SEARCH.equals(id)) {
				detailIntent = new Intent(this, SearchBuilderActivity.class);
				detailIntent.putExtra(SearchBuilderFragment.ARG_ITEM_ID, id);
				startActivity(detailIntent);
				
			} else if (MenuFragment.SAVED_SEARCH.equals(id)) {
				detailIntent = new Intent(this, SavedSearchActivity.class);
				detailIntent.putExtra(SavedSearchFragment.ARG_ITEM_ID, id);
				startActivity(detailIntent);
				
			} else if (MenuFragment.FAVORITES.equals(id)) {
				detailIntent = new Intent(this, FavoritesActivity.class);
				detailIntent.putExtra(SavedSearchFragment.ARG_ITEM_ID, id);
				startActivity(detailIntent);
				
			} else if (MenuFragment.SETTINGS.equals(id)) {
				detailIntent = new Intent(this, SettingsActivity.class);
				detailIntent.putExtra(SettingsFragment.ARG_ITEM_ID, id);
				startActivity(detailIntent);
				
			}
		}
	}
}
