package oauth2;

import assemblers.UserAssembler;
import models.User;
import play.cache.Cache;
import DTO.UserDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class CheckUserAuthentication   {
	
	private User authorisedUser;
	private UserDTO authorisedUserDTO;
	
	public CheckUserAuthentication() {
	}
	
	/**
	 * 
	 * @param client_id
	 * @param client_secret
	 * @return
	 */
	public boolean validCredentials(String client_id, String client_secret) {
		User user = User.find("byEmail", client_id).first();
		if (user != null) {
			String client_secret_hash = Security.sha256hexWithSalt(client_secret);
			
			if (client_secret_hash.equals(user.passwordHash)) {
				authorisedUser = user;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public boolean validToken(String accessToken) {
		// Get the UserDTO from Cache using the access_token
	    UserDTO userDTO = Cache.get(OAuth2Constants.CACHE_PREFIX + accessToken, UserDTO.class);
	    if (userDTO != null) {
	    	// If the UserDTO exists in then the token is valid set the instance variable, return true
	    	this.authorisedUserDTO = userDTO;
	    	return true;
	    } else {
	    	// If the UserDTO does not exist in the Cache, then find it in the database.
	    	User user = User.find("byAccessToken", accessToken).first();
	    	if (user != null) {
	    		// If in the database, then set the instance variable, Cache the DTO, return true
	    		this.authorisedUser = user;
	    		userDTO = getAuthorisedUserDTO();
	    		Cache.set(OAuth2Constants.CACHE_PREFIX + accessToken, userDTO, OAuth2Constants.CACHE_TIME);
	    		return true;
	    	}	
	    }
		return false;
	}
	
	/**
	 * Intended to be run after validCredentials() or validToken() methods,
	 * this method returns the authorisedUser object for the current request.
	 * 
	 * @return
	 */
	public User getAuthorisedUser() {
		if (authorisedUser == null && authorisedUserDTO != null) {
			this.authorisedUser = User.findById(authorisedUserDTO.id);
		}
		return this.authorisedUser;
	}
	
	/**
	 * Intended to be run after the validCredentials() or validToken() methods,
	 * this method returns authorisedUserDTO object for the current request.
	 * 
	 * @return
	 */
	public UserDTO getAuthorisedUserDTO() {
		if (authorisedUserDTO == null && authorisedUser != null) {
			this.authorisedUserDTO = UserAssembler.writeDTO(authorisedUser, true);
		} else {
			this.authorisedUserDTO = UserAssembler.writeDTO(getAuthorisedUser(), true);
		}
		return this.authorisedUserDTO;
	}

}
