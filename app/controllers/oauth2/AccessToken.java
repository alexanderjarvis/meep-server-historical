package controllers.oauth2;

import models.User;
import oauth2.AccessTokenGenerator;
import oauth2.CheckUserAuthentication;
import oauth2.OAuth2Constants;
import play.cache.Cache;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import controllers.JSONRequestTypeFilter;
import controllers.LoggingFilter;
import controllers.NoCookieFilter;
import controllers.SSLCheckFilter;

/**
 * 
 * TODO: add filter that rejects too many requests from same IP over small period of time
 * and blocks the IP temporarily.
 *
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@With({JSONRequestTypeFilter.class, NoCookieFilter.class, LoggingFilter.class, SSLCheckFilter.class})
public class AccessToken extends Controller {
	
	/**
	 * 
	 * @param grant_type
	 * @param client_id
	 * @param client_secret
	 */
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
    			
    			// Invalidate old token
        		User user = checkUserAuthentication.getAuthorisedUser();
        		Cache.safeDelete(OAuth2Constants.CACHE_PREFIX + user.accessToken);
        		
        		// Generate, persist and set new token to cache
        		user.accessToken = AccessTokenGenerator.generate();
        		user.save();
        		
        		Cache.set(OAuth2Constants.CACHE_PREFIX + user.accessToken, checkUserAuthentication.getAuthorisedUserDTO(), OAuth2Constants.CACHE_TIME);
        		
        		renderJSON(user.accessToken);
        		
    		} else {
    			error(400, "invalid credentials");
    		}
    		
    	} else {
    		error(400, "grant_type unknown: " + grant_type);
    	}
    	
    }
    
    
}
