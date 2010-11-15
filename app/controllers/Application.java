package controllers;

import java.util.HashMap;
import java.util.Map;

import play.mvc.Finally;
import play.mvc.Http;
import controllers.oauth2.AccessTokenFilter;

public class Application extends AccessTokenFilter {
	
	private static final Map<String, Http.Cookie> cookies = new HashMap<String, Http.Cookie>(0);

    public static void index() {
        render();
    }
    
    @Finally
    public static void removeCookies() {
    	response.cookies = cookies;
    }

}