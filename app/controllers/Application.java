package controllers;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.With;

/**
 *
 */
public class Application extends Controller {
	
	//TODO: fill out method.
	@Before
	static void authenticationCheck() {
		// Check that HTTPS is being used.
		if (request.secure) {
			//continue
		} else {
			
		}
	}
	
	@Finally
    static void log() {
        //Logger.info("Response contains:\n" + response.out);
    }

    public static void index() {
        render();
    }

}