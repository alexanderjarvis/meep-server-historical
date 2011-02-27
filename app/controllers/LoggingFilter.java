package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	public static void logBefore() {
		if (Play.mode == Play.Mode.DEV) {
			Logger.debug("Request: " + request.toString());
			
			StringBuilder sb = new StringBuilder();
	    	BufferedReader buf = new BufferedReader(new InputStreamReader(request.body));
	    	String line;
	    	try {
				while((line = buf.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				Logger.error(e.getLocalizedMessage());
			}
			String body = sb.toString();
			if (body.length() > 0) {
				Logger.debug("Request body: " + body);
			}
		}
	}

}
