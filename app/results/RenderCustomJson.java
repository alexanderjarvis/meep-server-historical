package results;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

import play.exceptions.UnexpectedException;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.RenderJson;
import play.mvc.results.Result;

/**
 * A modified version of play.mvc.results.RenderJson which sets the date format
 * to the ISO 8601 standard (as used by XML).
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class RenderCustomJson extends Result {	
	
	public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	String json;

	public RenderCustomJson(Object o) {
		GsonBuilder gson = new GsonBuilder();
		gson.setDateFormat(ISO8601_DATE_FORMAT);
		json = gson.create().toJson(o);
	}
    
    public RenderCustomJson(Object o, JsonSerializer<?>... adapters) {
        GsonBuilder gson = new GsonBuilder();
        gson.setDateFormat(ISO8601_DATE_FORMAT);
        for(Object adapter : adapters) {
            Type t = getMethod(adapter.getClass(), "serialize").getParameterTypes()[0];;
            gson.registerTypeAdapter(t, adapter);
        }
        json = gson.create().toJson(o);
    }
    
    public RenderCustomJson(String jsonString) {
        json = jsonString;
    }

    public void apply(Request request, Response response) {
        try {
            setContentTypeIfNotSet(response, "application/json; charset=utf-8");
            response.out.write(json.getBytes("utf-8"));
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }
    
    //
    
    static Method getMethod(Class<?> clazz, String name) {
        for(Method m : clazz.getDeclaredMethods()) {
            if(m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

}
