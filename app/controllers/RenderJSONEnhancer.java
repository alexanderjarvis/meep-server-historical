package controllers;

import com.google.gson.JsonSerializer;

import play.mvc.Controller;
import results.RenderCustomJson;

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
