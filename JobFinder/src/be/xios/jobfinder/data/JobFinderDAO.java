package be.xios.jobfinder.data;

import java.util.ArrayList;
import java.util.List;

import be.xios.jobfinder.model.SearchBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class JobFinderDAO {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allSavedSearchColumns = {
			JobFinderDB.SavedSearches.COL_ID,
			JobFinderDB.SavedSearches.COL_KEYWORDS,
			JobFinderDB.SavedSearches.COL_JOBTITLE,
			JobFinderDB.SavedSearches.COL_COUNTRYCODE,
			JobFinderDB.SavedSearches.COL_POSTALCODE,
			JobFinderDB.SavedSearches.COL_DISTANCE,
			JobFinderDB.SavedSearches.COL_INDUSTRY,
			JobFinderDB.SavedSearches.COL_JOBFUNCTION };

	public JobFinderDAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public SearchBuilder createSavedSearch(SearchBuilder currentSearch) {
		ContentValues values = new ContentValues();
		values.put(JobFinderDB.SavedSearches.COL_KEYWORDS,
				currentSearch.getKeywords());
		values.put(JobFinderDB.SavedSearches.COL_JOBTITLE,
				currentSearch.getJobTitle());
		values.put(JobFinderDB.SavedSearches.COL_COUNTRYCODE,
				currentSearch.getCountryCode());
		values.put(JobFinderDB.SavedSearches.COL_POSTALCODE,
				currentSearch.getPostalCode());
		values.put(JobFinderDB.SavedSearches.COL_DISTANCE,
				currentSearch.getDistance());
		values.put(JobFinderDB.SavedSearches.COL_INDUSTRY,
				currentSearch.getIndustry());
		values.put(JobFinderDB.SavedSearches.COL_JOBFUNCTION,
				currentSearch.getJobFunction());

		long insertId = database.insert(JobFinderDB.SavedSearches.TABLE_NAME,
				null, values);
		Cursor cursor = database.query(JobFinderDB.SavedSearches.TABLE_NAME,
				allSavedSearchColumns, JobFinderDB.SavedSearches.COL_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		SearchBuilder newSavedSearch = cursorToSearchBuilder(cursor);
		cursor.close();
		return newSavedSearch;
	}

	public void deleteComment(SearchBuilder savedSearch) {
		long id = savedSearch.getId();
		System.out.println("Saved search deleted with id: " + id);
		database.delete(JobFinderDB.SavedSearches.TABLE_NAME,
				JobFinderDB.SavedSearches.COL_ID + " = " + id, null);
	}

	public List<SearchBuilder> getAllSavedSearches() {
		List<SearchBuilder> savedSearches = new ArrayList<SearchBuilder>();

		Cursor cursor = database.query(JobFinderDB.SavedSearches.TABLE_NAME,
				allSavedSearchColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SearchBuilder savedSearch = cursorToSearchBuilder(cursor);
			savedSearches.add(savedSearch);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return savedSearches;
	}

	private SearchBuilder cursorToSearchBuilder(Cursor cursor) {
		SearchBuilder savedsearch = new SearchBuilder();
		savedsearch.setId(cursor.getLong(0));
		savedsearch.setKeywords(cursor.getString(1));
		savedsearch.setJobTitle(cursor.getString(2));
		savedsearch.setCountryCode(cursor.getString(3));
		savedsearch.setPostalCode(cursor.getString(4));
		savedsearch.setDistance(cursor.getInt(5));
		savedsearch.setIndustry(cursor.getString(6));
		savedsearch.setJobFunction(cursor.getString(7));

		return savedsearch;
	}
}
