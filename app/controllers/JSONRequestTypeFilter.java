package controllers;

import play.mvc.Before;
import play.mvc.Controller;

/**
 * Sets the default format of all requests to accept JSON
 * responses. This frees the client from having to specify
 * the Accept header.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class JSONRequestTypeFilter extends Controller {
	
	/**
	 * The Before filter that sets the request format to json,
	 * regardless of what it actually is.
	 */
	@Before
	public static void format() {
		request.format = "json";
	}

}
