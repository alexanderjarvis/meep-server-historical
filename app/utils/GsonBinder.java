package utils;

import java.lang.annotation.Annotation;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This custom Gson Binder is an example taken from:
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
 * @author Guilliame Bort
 */

@Global
public class GsonBinder implements TypeBinder<JsonObject> {

    public Object bind(String name, Annotation[] antns, String value, Class type) throws Exception {
        return new JsonParser().parse(value);
    }
}