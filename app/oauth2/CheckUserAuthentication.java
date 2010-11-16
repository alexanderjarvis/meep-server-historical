package oauth2;

import models.User;

public class CheckUserAuthentication   {
	
	private User authorizedUser;
	
	public CheckUserAuthentication() {
	}

	public boolean validateCredentials(String client_id, String client_secret) {
		User user = User.find("byEmail", client_id).first();
		if (user != null) {
			String client_secret_hash = Security.sha256hexWithSalt(client_secret);
			
			if (client_secret_hash.equals(user.userDetail.passwordHash)) {
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
