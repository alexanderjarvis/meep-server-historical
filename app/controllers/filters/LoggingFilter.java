package controllers.filters;

import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LoggingFilter extends Controller {
	
	@Before
	public static void logBefore(String body) {

		Logger.info("[" + request.remoteAddress + "]" + " " + request.toString());
		
		if (Play.mode == Play.Mode.DEV) {
			if (body != null && body.length() > 0) {
				Logger.debug("Request body: \n" + body);
			}
		}
		
	}

}
