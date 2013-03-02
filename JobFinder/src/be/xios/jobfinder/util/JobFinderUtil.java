package be.xios.jobfinder.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class JobFinderUtil {

	private static final String EMPTY_STRING = "";
	private static final String NULL_STRING = "null";

	public static String formatDate(long l, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format,
				Locale.getDefault());
		return dateFormat.format(l);
	}

	public static String getDisplayString(Object obj) {
		if (obj == null) {
			return EMPTY_STRING;
		}
		return nullSafeToString(obj);
	}

	public static String nullSafeToString(Object obj) {
		if (obj == null) {
			return NULL_STRING;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj instanceof Object[]) {
			return nullSafeToString((Object[]) obj);
		}
		if (obj instanceof boolean[]) {
			return nullSafeToString((boolean[]) obj);
		}
		if (obj instanceof char[]) {
			return nullSafeToString((char[]) obj);
		}
		if (obj instanceof int[]) {
			return nullSafeToString((int[]) obj);
		}
		if (obj instanceof long[]) {
			return nullSafeToString((long[]) obj);
		}
		String str = obj.toString();
		return (str != null ? str : EMPTY_STRING);
	}

}