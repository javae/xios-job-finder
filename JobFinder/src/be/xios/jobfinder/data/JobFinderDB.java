package be.xios.jobfinder.data;

import android.provider.BaseColumns;

public class JobFinderDB {
	public static final String DB_NAME = "jobfinder.db";
	public static final int DB_VERSION = 1;

	public static final class SavedSearches implements BaseColumns {

		public static final String TABLE_NAME = "savedsearches";
		public static final String COL_ID = "_id";
		public static final String COL_KEYWORDS = "keywords";
		public static final String COL_JOBTITLE = "jobTitle";
		public static final String COL_COUNTRYCODE = "countryCode";
		public static final String COL_POSTALCODE = "postalCode";
		public static final String COL_DISTANCE = "distance";
		public static final String COL_INDUSTRY = "industry";
		public static final String COL_JOBFUNCTION = "jobFunction";

	}

	public static final class JobFavorites implements BaseColumns {

		// TODO: define columns for jobs, what to save?
	}

}
