package controllers.filters;

import oauth2.OAuth2Constants;
import play.Play;
import play.mvc.*;

/**
 * When the configuration property is enabled, this filter checks if the request
 * is secure and if it is not throws a HTTP 400 error.
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
