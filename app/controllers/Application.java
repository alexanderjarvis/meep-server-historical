package controllers;

import java.util.HashMap;
import java.util.Map;

import play.Play;
import play.mvc.Finally;
import play.mvc.Http;
import controllers.oauth2.AccessTokenFilter;

public class Application extends AccessTokenFilter {
	
	/**
	 * An empty cookie map to replace any cookies in the response.
	 */
	private static final Map<String, Http.Cookie> cookies = new HashMap<String, Http.Cookie>(0);

    public static void index() {
        render();
    }
    
    /**
     * When the configuration property 'cookies.enabled' equals false,
     * this filter will replace the cookies in the response with an empty Map.
     * 
     * This is because cookies are not required in stateless webservice and
     * we don't want to send any unnecessary information to the client.
     */
    @Finally
    public static void removeCookies() {
    	boolean cookiesEnabled = Boolean.parseBoolean(Play.configuration.getProperty("cookies.enabled"));
    	if (!cookiesEnabled) {
    		response.cookies = cookies;
    	}
    }

}