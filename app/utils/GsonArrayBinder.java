package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * This class is another JSON binder for Gson where the JSON is an array and not an object.
 * 
 * A JSON array starts with square brackets and a JSON object with curly braces.
 * 
 * e.g. [1,2,3] or [{firstName:"Alex",lastName:"Jarvis"},{firstName:"John",lastName:"Smith"}]
 * 
 * instead of:
 * 
 * {firstName:"Alex",lastName:"Jarvis"}
 * 
 * @see GsonObjectBinder
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@Global
public class GsonArrayBinder implements TypeBinder<JsonArray> {

	@Override
	public Object bind(String arg0, Annotation[] arg1, String arg2, Class arg3, Type arg4) throws Exception {
		return new JsonParser().parse(arg2);
	}
}