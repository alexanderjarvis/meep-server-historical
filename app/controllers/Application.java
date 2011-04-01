package controllers;

import play.mvc.Controller;

/**
 * The default Application controller provided by Play and mounted on root "/".
 * 
 * In this application, the root will just show a simple welcome page as the application is intended
 * to be accessed using the mobile client.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Application extends Controller {
	
    public static void index() {
        render();
    }

}