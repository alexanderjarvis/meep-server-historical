package utils;

import java.lang.annotation.Annotation;

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

    public Object bind(String name, Annotation[] antns, String value, Class type) throws Exception {
        return new JsonParser().parse(value);
    }
}