package controllers.oauth2;

import models.User;
import oauth2.OAuth2Constants;
import play.cache.Cache;
import DTO.UserDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenDestroy extends AccessTokenFilter {
	
	public static void destroy() {
		User authorisedUser = userAuth.getAuthroisedUser();
		if (authorisedUser != null) {
			Cache.delete(OAuth2Constants.CACHE_PREFIX + authorisedUser.accessToken);
			authorisedUser.accessToken = "";
			authorisedUser.save();
		} else {
			badRequest();
		}
	}

}
