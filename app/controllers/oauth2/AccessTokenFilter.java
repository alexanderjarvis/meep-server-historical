package controllers.oauth2;

import models.User;
import oauth2.CheckUserAuthentication;
import oauth2.OAuth2Constants;
import play.mvc.Before;
import play.mvc.Router;
import play.mvc.Http.Inbound;
import DTO.UserDTO;
import controllers.RenderJSONEnhancer;

/**
 * The AccessTokenFilter intercepts requests and expects an access token parameter
 * to be in the query string. If the access token is valid, the request proceeds,
 * if not a 401 Unauthorized error is sent back in the response.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenFilter extends RenderJSONEnhancer {
	
	protected static CheckUserAuthentication userAuth;
	
	/**
	 * Checks that the request contains a valid access token.
	 */
	@Before
	protected static void checkAccessToken() {
		
		// Check oauth_token present in request, if not, error
		if (params._contains(OAuth2Constants.PARAM_OAUTH_TOKEN)) {
			
			// Check token exists in system
			userAuth = new CheckUserAuthentication();
			if (!userAuth.validToken(params.get(OAuth2Constants.PARAM_OAUTH_TOKEN))) {
				error(401, "Unauthorized");
			}
		
		// Resources actions that do not require an access token
		} else if (!request.action.equals(Router.reverse("oauth2.AccessToken.auth").action) &&
				!request.action.equals(Router.reverse("Users.create").action)) {
			error(401, "Unauthorized");
		}
	}
	
	/**
	 * Returns the current User object of the user which is authorised with this request.
	 * @return
	 */
	public static User getAuthorisedUser() {
		return userAuth.getAuthorisedUser();
	}
	
	/**
	 * Returns the current UserDTO object of the user which is authorised with this request.
	 * @return
	 */
	public static UserDTO getAuthorisedUserDTO() {
		return userAuth.getAuthorisedUserDTO();
	}
	
}
