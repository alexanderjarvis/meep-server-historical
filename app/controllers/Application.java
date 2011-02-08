package controllers;

import play.mvc.Before;
import play.mvc.With;
import controllers.oauth2.AccessTokenFilter;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@With(RequestTypeFilter.class)
public class Application extends AccessTokenFilter {
	
    public static void index() {
        render();
    }

}