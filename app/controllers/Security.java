package controllers;

import models.User;
import play.mvc.Controller;

public class Security extends Controller {
	
	private static final String token = "12312fadsf23asdf";
	
	public static void authenticate(String username, String password) {
		User user = User.find("byEmail", username).first();
        if (user != null && user.password.equals(password)) {
        	// return auth token
        	renderJSON(token);
        }
		unauthorized("Realm");
	}
	
	public boolean isTokenValid(String token) {
		
		return token.equals(token);
	}
	
}
