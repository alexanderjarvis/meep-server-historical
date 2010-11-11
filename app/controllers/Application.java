package controllers;

import oauth2.OAuth2Constants;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.Router;

/**
 *
 */
public class Application extends Controller {
	
	@Before
	static void authenticationCheck() {
		// Check that HTTPS is being used.
		if (request.secure == false) {
			error(500, "not secure");
		}
		// Check oauth_token present in request, if not, error
		if (params._contains(OAuth2Constants.PARAM_OAUTH_TOKEN)) {
			// good
		} else if (!request.path.equals(Router.reverse("AccessToken.auth"))) {
			error(400, "invalid request");
		}
	}
	
	@Finally
    static void log() {
        //Logger.info("Response contains:\n" + response.out);
    }

    public static void index() {
        render();
    }

}