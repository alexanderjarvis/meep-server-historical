package controllers;

import oauth2.OAuth2Constants;
import play.Play;
import play.mvc.*;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class SSLCheckFilter extends Controller {

	/**
	 * Checks that the request is secure and therefore encrypted.
	 */
	@Before
	protected static void checkSSL() {
		
		// Check that HTTPS is being used.
		final boolean sslRequired = Boolean.parseBoolean(Play.configuration.getProperty(OAuth2Constants.SSL_REQUIRED, Boolean.TRUE.toString()));
		if (sslRequired && !request.secure) {
			error(400, "SSL required");
		}
	}

}
