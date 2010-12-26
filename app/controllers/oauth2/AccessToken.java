package controllers.oauth2;

import models.User;
import oauth2.AccessTokenGenerator;
import oauth2.CheckUserAuthentication;
import oauth2.OAuth2Constants;

import org.apache.commons.lang.RandomStringUtils;

import play.cache.Cache;
import play.data.validation.Required;
import controllers.NoCookieFilter;

/**
 * 
 * TODO: add filter that rejects too many requests from same IP over small period of time
 * and blocks the IP temporarily.
 *
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessToken extends NoCookieFilter {
	
    public static void auth(@Required String grant_type, 
    						@Required String client_id, 
    						@Required String client_secret) {
    	
    	if (validation.hasErrors()) {
    		error(400, "missing required field(s)");
    	}
    		
    	if (grant_type.equals("password")) {
    		// check user name and password
    		CheckUserAuthentication checkUserAuthentication = new CheckUserAuthentication();
    		if (checkUserAuthentication.validCredentials(client_id, client_secret)) {
    			// if correct, generate access token
        		String accessToken = AccessTokenGenerator.generate();
        		
        		User user = checkUserAuthentication.getAuthroizedUser();
        		Cache.set(OAuth2Constants.CACHE_PREFIX + accessToken, user, OAuth2Constants.CACHE_TIME);
        		
        		user.accessToken = accessToken;
        		user.save();
        		
        		renderJSON(accessToken);
    		} else {
    			error(400, "invalid credentials");
    		}
    		
    	} else {
    		error(400, "grant_type unknown: " + grant_type);
    	}
    	
    }
    
}
