package controllers.filters;

import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * Adds logging information for each request.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LoggingFilter extends Controller {
	
	@Before
	public static void logBefore(String body) {
		
		if (Play.mode.isProd()) {
			Logger.info("[" + request.remoteAddress + "] " + request.toString());
			
		} else if (Play.mode.isDev()) {
			Logger.info(request.toString());
			if (body != null && body.length() > 0) {
				Logger.debug("Request body: \n" + body);
			}
		}
	}
}
