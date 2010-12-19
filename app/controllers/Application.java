package controllers;

import java.util.HashMap;
import java.util.Map;

import play.Play;
import play.mvc.Finally;
import play.mvc.Http;
import controllers.oauth2.AccessTokenFilter;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Application extends AccessTokenFilter {
	
    public static void index() {
        render();
    }

}