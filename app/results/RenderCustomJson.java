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
import utils.GsonFactory;

/**
 * A modified version of Play's RenderJson class which obtains the GsonBuilder from the
 * GsonFactory instead so that other attributes can be set e.g. pretty printing.
 * 
 * @see play.mvc.results.RenderJson
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class RenderCustomJson extends Result {	
	
	String json;

	public RenderCustomJson(Object o) {
		json = GsonFactory.gsonBuilder().create().toJson(o);
	}
    
    public RenderCustomJson(Object o, JsonSerializer<?>... adapters) {
        GsonBuilder gson = GsonFactory.gsonBuilder();
        for(Object adapter : adapters) {
            Type t = getMethod(adapter.getClass(), "serialize").getParameterTypes()[0];
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
       
    static Method getMethod(Class<?> clazz, String name) {
        for(Method m : clazz.getDeclaredMethods()) {
            if(m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

}
