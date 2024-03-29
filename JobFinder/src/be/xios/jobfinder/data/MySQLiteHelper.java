package be.xios.jobfinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public MySQLiteHelper(Context context) {
		super(context, JobFinderDB.DB_NAME, null, JobFinderDB.DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlCreate = "CREATE TABLE "
				+ JobFinderDB.SavedSearches.TABLE_NAME + "("
				+ JobFinderDB.SavedSearches.COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ JobFinderDB.SavedSearches.COL_KEYWORDS + " TEXT,"
				+ JobFinderDB.SavedSearches.COL_JOBTITLE + " TEXT,"
				+ JobFinderDB.SavedSearches.COL_COUNTRYCODE + " TEXT,"
				+ JobFinderDB.SavedSearches.COL_POSTALCODE + " TEXT,"
				+ JobFinderDB.SavedSearches.COL_DISTANCE + " INTEGER,"
				+ JobFinderDB.SavedSearches.COL_INDUSTRY + " TEXT,"
				+ JobFinderDB.SavedSearches.COL_JOBFUNCTION + " TEXT)";

		try {
			db.execSQL(sqlCreate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sqlCreate = "CREATE TABLE " + JobFinderDB.JobFavorites.TABLE_NAME + "("
				+ JobFinderDB.JobFavorites.COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ JobFinderDB.JobFavorites.COL_LI_ID + " TEXT,"
				+ JobFinderDB.JobFavorites.COL_POSITION_TITLE + " TEXT,"
				+ JobFinderDB.JobFavorites.COL_COMPANY_NAME + " TEXT,"
				+ JobFinderDB.JobFavorites.COL_LOCATION + " TEXT,"
				+ JobFinderDB.JobFavorites.COL_POSTING_DATE + " INTEGER)";

		try {
			db.execSQL(sqlCreate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "
				+ JobFinderDB.SavedSearches.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "
				+ JobFinderDB.JobFavorites.TABLE_NAME);
		onCreate(db);

	}

}