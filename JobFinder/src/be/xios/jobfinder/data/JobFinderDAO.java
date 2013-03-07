package be.xios.jobfinder.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.model.SearchBuilder;

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

	private String[] allFavColumns = { 
			JobFinderDB.JobFavorites.COL_ID,
			JobFinderDB.JobFavorites.COL_LI_ID,
			JobFinderDB.JobFavorites.COL_POSITION_TITLE,
			JobFinderDB.JobFavorites.COL_COMPANY_NAME,
			JobFinderDB.JobFavorites.COL_LOCATION,
			JobFinderDB.JobFavorites.COL_POSTING_DATE };

	public JobFinderDAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createSavedSearch(SearchBuilder currentSearch) {
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
		return insertId;

		// Cursor cursor = database.query(JobFinderDB.SavedSearches.TABLE_NAME,
		// allSavedSearchColumns, JobFinderDB.SavedSearches.COL_ID + " = "
		// + insertId, null, null, null, null);
		// cursor.moveToFirst();
		// SearchBuilder newSavedSearch = cursorToSearchBuilder(cursor);
		// cursor.close();
		// return newSavedSearch;
	}

	public long createFavoriteJob(LinkedInJob currentJob) {
		long insertId = -1;
		//kijk of deze id al bij favorieten staat!		
		Cursor cursor = database.query(JobFinderDB.JobFavorites.TABLE_NAME, allFavColumns, 
                JobFinderDB.JobFavorites.COL_LI_ID + " = " + currentJob.getId(), null, null, null, null);
		if (cursor.getCount() > 0) {
			ContentValues values = new ContentValues();
			values.put(JobFinderDB.JobFavorites.COL_LI_ID, currentJob.getId());
			values.put(JobFinderDB.JobFavorites.COL_POSITION_TITLE,
					currentJob.getPositionTitle());
			values.put(JobFinderDB.JobFavorites.COL_COMPANY_NAME,
					currentJob.getCompanyName());
			values.put(JobFinderDB.JobFavorites.COL_LOCATION,
					currentJob.getLocation());
			values.put(JobFinderDB.JobFavorites.COL_POSTING_DATE, currentJob
					.getPostingDate().toString());
			
			insertId = database.insert(JobFinderDB.JobFavorites.TABLE_NAME,
					null, values);
		}
		return insertId;

		// Cursor cursor = database.query(JobFinderDB.JobFavorites.TABLE_NAME,
		// allFavColumns, JobFinderDB.JobFavorites.COL_ID + " = "
		// + insertId, null, null, null, null);
		// cursor.moveToFirst();
		// LinkedInJob newFavJob = cursorToLinkedInJob(cursor);
		// cursor.close();
		// return newFavJob;
	}

	public void deleteSavedSearch(SearchBuilder savedSearch) {
		long id = savedSearch.getDbId();
		database.delete(JobFinderDB.SavedSearches.TABLE_NAME,
				JobFinderDB.SavedSearches.COL_ID + " = " + id, null);
		Log.d("JobFinderDAO", "Saved search deleted with id: " + id);
	}

	public void deleteFavJob(LinkedInJob favJob) {
		long id = favJob.getDbId();
		database.delete(JobFinderDB.SavedSearches.TABLE_NAME,
				JobFinderDB.SavedSearches.COL_ID + " = " + id, null);
		Log.d("JobFinderDAO", "Favorite job deleted with id: " + id);
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

	public List<LinkedInJob> getAllFavJobs() {
		List<LinkedInJob> favJobs = new ArrayList<LinkedInJob>();

		Cursor cursor = database.query(JobFinderDB.JobFavorites.TABLE_NAME,
				allFavColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			LinkedInJob favJob = cursorToLinkedInJob(cursor);
			favJobs.add(favJob);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return favJobs;
	}

	private SearchBuilder cursorToSearchBuilder(Cursor cursor) {
		SearchBuilder savedsearch = new SearchBuilder();

		savedsearch.setDbId(cursor.getLong(0));
		savedsearch.setKeywords(cursor.getString(1));
		savedsearch.setJobTitle(cursor.getString(2));
		savedsearch.setCountryCode(cursor.getString(3));
		savedsearch.setPostalCode(cursor.getString(4));
		savedsearch.setDistance(cursor.getInt(5));
		savedsearch.setIndustry(cursor.getString(6));
		savedsearch.setJobFunction(cursor.getString(7));

		return savedsearch;
	}

	private LinkedInJob cursorToLinkedInJob(Cursor cursor) {
		LinkedInJob favJob = new LinkedInJob();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD",
				Locale.getDefault());

		favJob.setDbId(cursor.getLong(0));
		favJob.setId(cursor.getInt(1)); // = linkedinjobID
		favJob.setPositionTitle(cursor.getString(2));
		favJob.setCompanyName(cursor.getString(3));
		favJob.setLocation(cursor.getString(4));
		try {
			favJob.setPostingDate(format.parse(cursor.getString(5)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return favJob;
	}
}
