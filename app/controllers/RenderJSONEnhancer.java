package controllers;

import com.google.gson.JsonSerializer;

import play.mvc.Controller;
import results.RenderCustomJson;

/**
 * This class overrides the renderJSON methods found in play.mvc.Controller
 * substituting their call to play.mvc.results.RenderJson with RenderCustomJson.
 * 
 * This allows the Gson object to be configured before use.
 * 
 * This class is intended to be extended for all Play controllers that require the
 * use of the RenderCustomJson class.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class RenderJSONEnhancer extends Controller {
	
	/**
     * Render a 200 OK application/json response
     * @param jsonString The JSON string
     */
	protected static void renderJSON(String jsonString) {
		throw new RenderCustomJson(jsonString);
	}
	
	/**
     * Render a 200 OK application/json response
     * @param o The Java object to serialize
     */
	protected static void renderJSON(Object o) {
		throw new RenderCustomJson(o);
	}
	
	/**
     * Render a 200 OK application/json response.
     * @param o The Java object to serialize
     * @param adapters A set of GSON serializers/deserializers/instance creator to use
     */
	protected static void renderJSON(Object o, JsonSerializer<?>... adapters) {
		throw new RenderCustomJson(o, adapters);
	}

}
