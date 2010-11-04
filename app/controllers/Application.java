package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

/**
 *
 */
public class Application extends Controller {
	
	//TODO: fill out method.
	@Before
	static void authentication() {
		//if(session.get("user") == null) {
		//	Logger.debug("No");
		//}
	}
	
	@Finally
    static void log() {
        Logger.info("Response contains:\n" + response.out);
    }

    public static void index() {
        render();
    }

}