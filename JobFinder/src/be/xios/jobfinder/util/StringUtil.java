package be.xios.jobfinder.util;

public class StringUtil {
	
	public static boolean isBlank(String text) {
		return text == null || text.isEmpty();
	}
	
	public static boolean isNotBlank(String text) {
		return !isBlank(text);
	}
}