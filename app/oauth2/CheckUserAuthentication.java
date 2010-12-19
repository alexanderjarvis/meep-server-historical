package oauth2;

import play.cache.Cache;
import models.User;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class CheckUserAuthentication   {
	
	private User authorizedUser;
	
	public CheckUserAuthentication() {
	}

	public boolean validCredentials(String client_id, String client_secret) {
		User user = User.find("byEmail", client_id).first();
		if (user != null) {
			String client_secret_hash = Security.sha256hexWithSalt(client_secret);
			
			if (client_secret_hash.equals(user.passwordHash)) {
				authorizedUser = user;
				return true;
			}
		}
		return false;
	}
	
	public boolean validToken(String access_token) {
	    User user = Cache.get(OAuth2Constants.CACHE_PREFIX + access_token, User.class);
	    if (user == null) {
	    	user = User.find("byAccessToken", access_token).first();
	    	Cache.set(OAuth2Constants.CACHE_PREFIX + access_token, user, OAuth2Constants.CACHE_TIME);
	    }
		if (user != null) {
			authorizedUser = user;
			return true;
		}
		return false;
	}
	
	public User getAuthroizedUser() {
		return this.authorizedUser;
	}

}
