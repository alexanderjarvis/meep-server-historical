package controllers.oauth2;

import org.apache.commons.lang.RandomStringUtils;

import play.data.validation.Required;
import play.data.validation.Validation.Validator;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Router;

public class AccessToken extends Controller {
	
    public static void auth(@Required String grant_type, @Required String client_id, String client_secret) {
    	
    	if (validation.hasErrors()) {
    		error(400, "validation has errors");
    	}
    		
    	if (grant_type.equals("password")) {
    		// check user name and password
    		// TODO: check username and password
    		
    		// if correct, generate access token etc.
    		String result = RandomStringUtils.randomAlphanumeric(32);
    		renderJSON(result);
    		
    	} else {
    		error(400, "grant_type: " + grant_type);
    	}
    	
    }
    
}
