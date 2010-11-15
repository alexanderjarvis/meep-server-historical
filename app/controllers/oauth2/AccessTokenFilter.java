package controllers.oauth2;

import java.util.Properties;

import oauth2.OAuth2Constants;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;

public class AccessTokenFilter extends Controller {
	
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
			// good
			// Check token exists in system
		} else if (!request.path.equals(Router.reverse("oauth2.AccessToken.auth"))) {
			badRequest();
		}
	}
	
}
