package be.xios.jobfinder.data;

import android.provider.BaseColumns;

public class JobFinderDB {
	public static final String DB_NAME = "jobfinder.db";
	public static final int DB_VERSION = 1;

	public static final class SavedSearches implements BaseColumns {

		public static final String TABLE_NAME = "savedsearches";
		public static final String COL_ID = "_id";
		public static final String COL_KEYWORDS = "keywords";
		public static final String COL_JOBTITLE = "jobtitle";
		public static final String COL_COUNTRYCODE = "countrycode";
		public static final String COL_POSTALCODE = "postalcode";
		public static final String COL_DISTANCE = "distance";
		public static final String COL_INDUSTRY = "industry";
		public static final String COL_JOBFUNCTION = "jobfunction";
	}

	public static final class JobFavorites implements BaseColumns {

		public static final String TABLE_NAME = "favoritejobs";
		public static final String COL_ID = "_id";
		public static final String COL_LI_ID = "linkedinID";
		public static final String COL_POSITION_TITLE = "positiontitle";
		public static final String COL_COMPANY_NAME = "companyname";
		public static final String COL_LOCATION = "location";
		public static final String COL_POSTING_DATE = "postingdate";
	}

}
