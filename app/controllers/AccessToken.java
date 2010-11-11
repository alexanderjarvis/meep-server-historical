package controllers;

import org.apache.commons.lang.RandomStringUtils;

import play.data.validation.Required;
import play.data.validation.Validation.Validator;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Router;

public class AccessToken extends Controller {
	
	// TODO: make HTTPS Only
    public static void auth(@Required String grant_type, @Required String client_id, String client_secret) {
    	
    	if (validation.hasErrors()) {
    		error(400, "validation has errors");
    	}
    	
    	// Possible grant_types are listed for future use.
    	if (grant_type.equals("authorization_code")) {
    		error(400, "grant_type not supported: " + grant_type);
    		
    	} else if (grant_type.equals("password")) {
    		// check user name and password
    		// TODO: check username and password
    		
    		// if correct, generate access token etc.
    		String result = RandomStringUtils.randomAlphanumeric(32);
    		renderJSON(result);
    		
    	} else if (grant_type.equals("assertion")) {
    		error(400, "grant_type not supported: " + grant_type);
    		
    	} else if (grant_type.equals("refresh_token")) {
    		error(400, "grant_type not supported: " + grant_type);
    	}
    	
    	error(400, "grant_type unknown: " + grant_type);
    }

}
