package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * This class is another JSON binder but for when the JSON is an Array and not encapsulated by an object.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@Global
public class GsonArrayBinder implements TypeBinder<JsonArray> {

	@Override
	public Object bind(String arg0, Annotation[] arg1, String arg2, Class arg3, Type arg4) throws Exception {
		return new JsonParser().parse(arg2);
	}
}