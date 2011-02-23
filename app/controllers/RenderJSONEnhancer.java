package controllers;

import com.google.gson.JsonSerializer;

import play.mvc.Controller;
import results.RenderCustomJson;

public class RenderJSONEnhancer extends Controller {

	protected static void renderJSON(String jsonString) {
		throw new RenderCustomJson(jsonString);
	}

	protected static void renderJSON(Object o) {
		throw new RenderCustomJson(o);
	}

	protected static void renderJSON(Object o, JsonSerializer adapters[]) {
		throw new RenderCustomJson(o, adapters);
	}

}
