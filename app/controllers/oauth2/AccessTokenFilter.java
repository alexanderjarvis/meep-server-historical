package controllers.oauth2;

import java.util.Properties;

import controllers.NoCookieFilter;

import models.User;

import oauth2.CheckUserAuthentication;
import oauth2.OAuth2Constants;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenFilter extends NoCookieFilter {
	
	private static CheckUserAuthentication userAuth;
	
	/**
	 * Checks that the request is secure and therefore encrypted.
	 */
	@Before
	static void checkSSL() {
		
		// Check that HTTPS is being used.
		final boolean sslRequired = Boolean.parseBoolean(Play.configuration.getProperty(OAuth2Constants.SSL_REQUIRED, Boolean.TRUE.toString()));
		if (sslRequired && !request.secure) {
			error(500, "not secure");
		}
	}
	
	/**
	 * Checks that the request contains a valid access token.
	 */
	@Before
	static void checkAccessToken() {
		
		// Check oauth_token present in request, if not, error
		if (params._contains(OAuth2Constants.PARAM_OAUTH_TOKEN)) {
			
			// Check token exists in system
			userAuth = new CheckUserAuthentication();
			if (!userAuth.validToken(params.get(OAuth2Constants.PARAM_OAUTH_TOKEN))) {
				badRequest();
			}
			
		} else if (!request.path.equals(Router.reverse("oauth2.AccessToken.auth"))) {
			badRequest();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public User getAuthorisedUser() {
		return userAuth.getAuthroizedUser();
	}
	
}
