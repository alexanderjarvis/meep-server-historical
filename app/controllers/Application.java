package controllers;

import play.mvc.Controller;

/**
 * The default Application controller provided by Play and mounted on root "/".
 * 
 * In this application, the root will just show a simple information page.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Application extends Controller {
	
    public static void index() {
        render();
    }

}