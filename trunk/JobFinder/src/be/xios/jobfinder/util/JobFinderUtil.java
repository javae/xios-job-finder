package be.xios.jobfinder.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class JobFinderUtil {

	public static String formatDate(long l, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
		return dateFormat.format(l);
	}
	
}