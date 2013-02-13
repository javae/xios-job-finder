package be.xios.jobfinder.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JobFinderUtil {

	public static String formatDate(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
		return dateFormat.format(date);
	}
	
}