package controllers.oauth2;

import oauth2.OAuth2Constants;
import play.cache.Cache;
import models.User;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenDestroy extends AccessTokenFilter {
	
	public static void destroy() {
		User authUser = userAuth.getAuthroizedUser();
		User user = User.findById(authUser.id);
		if (authUser != null && user != null) {
			Cache.delete(OAuth2Constants.CACHE_PREFIX + authUser.accessToken);
			user.accessToken = "";
			user.save();
		} else {
			badRequest();
		}
	}

}
