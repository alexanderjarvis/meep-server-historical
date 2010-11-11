package oauth2;

import models.User;

public class CheckUserAuthenticationImpl implements CheckUserAuthentication {

	@Override
	public boolean validCredentials(String client_id, String client_secret) {
		User user = User.findById(client_id);
		if (user != null) {
			// TODO: hash password
			if (client_secret.equals(user.password)) {
				return true;
			}
		}
		return false;
	}

}
