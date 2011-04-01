package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This custom Gson Binder is an example from:
 * 
 * http://www.playframework.org/community/snippets/6
 * 
 * It allows you to bind the request body into your own domain object in the controller
 * e.g. 
 * 
 *   public static void handleJsonAsObject(JsonObject body) {
 *       User u = new Gson().fromJson(body, User.class);
 *       renderText(u);
 *   }
 *   
 * Since the snippet however the signature of the bind method has been updated in Play 1.2
 * 
 * @author Guilliame Bort
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@Global
public class GsonObjectBinder implements TypeBinder<JsonObject> {

	@Override
	public Object bind(String arg0, Annotation[] arg1, String arg2, Class arg3, Type arg4) throws Exception {
		return new JsonParser().parse(arg2);
	}
}