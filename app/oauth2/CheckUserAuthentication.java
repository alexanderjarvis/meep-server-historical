package oauth2;

import models.User;

public class CheckUserAuthentication   {
	
	private User authorizedUser;
	
	public CheckUserAuthentication() {
	}

	public boolean validateCredentials(String client_id, String client_secret) {
		User user = User.find("byEmail", client_id).first();
		if (user != null) {
			// TODO: hash password
			if (client_secret.equals(user.password)) {
				authorizedUser = user;
				return true;
			}
		}
		return false;
	}
	
	public User getAuthroizedUser() {
		return this.authorizedUser;
	}

}
