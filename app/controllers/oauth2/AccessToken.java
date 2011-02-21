package controllers.oauth2;

import models.User;
import oauth2.AccessTokenGenerator;
import oauth2.CheckUserAuthentication;
import play.data.validation.Required;
import play.mvc.With;
import controllers.JSONRequestTypeFilter;
import controllers.NoCookieFilter;

/**
 * 
 * TODO: add filter that rejects too many requests from same IP over small period of time
 * and blocks the IP temporarily.
 *
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@With(JSONRequestTypeFilter.class)
public class AccessToken extends NoCookieFilter {
	
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
    			// if correct, generate access token
        		String accessToken = AccessTokenGenerator.generate();
        		
        		User user = checkUserAuthentication.getAuthorisedUser();
        		
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
