package controllers;

import play.mvc.Before;
import play.mvc.Controller;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class JSONRequestTypeFilter extends Controller {
	
	/**
	 * Sets the default format of all requests to accept JSON
	 * responses. This frees the client from having to specify
	 * the Accept header.
	 */
	@Before
	public static void format() {
		request.format = "json";
	}

}
