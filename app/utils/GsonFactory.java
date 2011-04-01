package utils;

import java.lang.reflect.Type;

import org.joda.time.DateTime;

import play.Play;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Creates GsonBuilder objects that are setup with specific type adapters and attributes
 * for this application.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class GsonFactory {
	
	/**
	 * Returns a GsonBuilder object with custom type adapters for Joda's DateTime class
	 * and also pretty printing (if set in the configuration).
	 * @return
	 */
	public static GsonBuilder gsonBuilder() {
		GsonBuilder gson = new GsonBuilder();
		if (Boolean.parseBoolean(Play.configuration.getProperty("json.prettyprinting"))) {
			gson.setPrettyPrinting();
		}
		gson.registerTypeAdapter(DateTime.class, new GsonFactory.DateTimeSerializer());
		gson.registerTypeAdapter(DateTime.class, new GsonFactory.DateTimeDeserializer());
		return gson;
	}
	
	/**
	 * Serializes Joda's DateTime object into an ISO8601 string.
	 * 
	 * @author Alex Jarvis axj7@aber.ac.uk
	 */
	public static class DateTimeSerializer implements JsonSerializer<DateTime> {
		public JsonElement serialize(DateTime src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}
	}
	
	/**
	 * Deserializes an ISO8601 date string into a Joda DateTime object.
	 * 
	 * @author Alex Jarvis axj7@aber.ac.uk
	 */
	private static class DateTimeDeserializer implements JsonDeserializer<DateTime> {
		public DateTime deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			// TODO: remove this quickfix
			String dateTimeString = json.getAsJsonPrimitive().getAsString();
			if (dateTimeString.endsWith("+100")) {
				dateTimeString.replace("+100", "+0100");
			}
			return new DateTime(dateTimeString);
		}
	}
}
