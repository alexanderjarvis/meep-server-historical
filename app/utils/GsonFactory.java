package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class GsonFactory {
	
	private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static GsonBuilder gsonBuilder() {
		GsonBuilder gson = new GsonBuilder();
		gson.setDateFormat(ISO8601_DATE_FORMAT);
		return gson;
	}
}
