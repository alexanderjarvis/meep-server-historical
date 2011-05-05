package controllers.oauth2;

import models.User;
import oauth2.OAuth2Constants;
import play.cache.Cache;
import controllers.ServiceApplicationController;

/**
 * Handles deleting the access token granted to a user, preventing any future
 * requests from being authorised without re-authorisation.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenDestroy extends ServiceApplicationController {
	
	public static void destroy() {
		User authorisedUser = getAuthorisedUser();
		if (authorisedUser != null) {
			Cache.delete(OAuth2Constants.CACHE_PREFIX + authorisedUser.accessToken);
			authorisedUser.accessToken = "";
			authorisedUser.save();
			ok();
		} else {
			badRequest();
		}
	}

}
