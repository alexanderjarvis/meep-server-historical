package controllers;

import models.User;
import models.UserConnection;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Valid;
import DTO.UserDTO;
import assemblers.UserAssembler;
import assemblers.UserSummaryAssembler;

/**
 * 
 * @author alex
 */
public class Users extends Application {
	
	/**
	 * Returns all users
	 * TODO: this should only be available to admins.
	 */
	public static void index() {
		User authUser = userAuth.getAuthroizedUser();
		renderJSON(UserSummaryAssembler.writeDTOs(authUser));
	}
    
	/**
	 * Create
	 * @param email
	 * @param password
	 */
    public static void create(@Valid UserDTO user) {
    	if (validation.hasErrors()) {
    		for (Error error : validation.errors()) {
    			Logger.debug(error.getKey() + " : " + error.message());
    		}
			error(500, "Validation Errors");
		}
    	
    	
    	
    	
//    	user.save();
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	map.put("id", user.id);
//    	String url = Router.reverse("Users.show", map).url;
//    	Logger.debug("Entity Created - Redirect URL: %s", url);
//    	redirect(url);
    	//TODO: return 201 ?
    }
    
    /**
     * Shows information on a user in the system.
     * 
     * Special behaviour is applied if the user is the authorised user
     * and only users that the authorised user is connected to are allowed
     * to be shown.
     * 
     * @param id
     */
    public static void show(Long id) {
    	
    	User authUser = userAuth.getAuthroizedUser();
    	
    	if (id.equals(authUser.id)) {
    		renderJSON(UserAssembler.writeDTO(authUser, true));
    	} else {
    		// is the user connected
    		User user = User.findById(id);
    		if (user != null) {
		    	for (UserConnection connection : authUser.connections) {
		    		if (id.equals(connection.userConnection.user.id)) {
		    			renderJSON(UserAssembler.writeDTO(user, false));
		    		}
		    	}
		    	error(400, "User not connected");
    		} else {
    			error(404, "User does not exist");
    		}
    	}
    }
    
    /**
     * Update
     * @param user
     */
    public static void update(@Valid User user) {
    	if (validation.hasErrors()) {
			render("@create", user);
		}
    	user = user.merge();
    	user.save();
		index();
    }

}