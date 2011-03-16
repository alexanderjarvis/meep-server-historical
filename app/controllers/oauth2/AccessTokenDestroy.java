package controllers.oauth2;

import models.User;
import oauth2.OAuth2Constants;
import play.cache.Cache;
import controllers.ServiceApplicationController;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenDestroy extends ServiceApplicationController {
	
	/**
	 * 
	 */
	public static void destroy() {
		User authorisedUser = userAuth.getAuthorisedUser();
		if (authorisedUser != null) {
			Cache.delete(OAuth2Constants.CACHE_PREFIX + authorisedUser.accessToken);
			authorisedUser.accessToken = "";
			authorisedUser.save();
			renderJSON("");
		} else {
			badRequest();
		}
	}

}
