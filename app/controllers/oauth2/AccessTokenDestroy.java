package controllers.oauth2;

import controllers.JSONRequestTypeFilter;
import controllers.NoCookieFilter;
import models.User;
import oauth2.OAuth2Constants;
import play.cache.Cache;
import play.mvc.With;
import DTO.UserDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@With({JSONRequestTypeFilter.class, NoCookieFilter.class})
public class AccessTokenDestroy extends AccessTokenFilter {
	
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
