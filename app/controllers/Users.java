package controllers;

import java.util.HashMap;
import java.util.Map;

import models.User;
import models.UserConnection;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Valid;
import play.mvc.Router;
import DTO.UserDTO;
import assemblers.UserAssembler;
import assemblers.UserSummaryAssembler;

/**
 * 
 * @author alex
 */
public class Users extends Application {
	
	/**
	 * Returns all users that the current authorised user is connected to
	 * (not all users in the system).
	 */
	public static void index() {
		User authUser = userAuth.getAuthroizedUser();
		renderJSON(UserSummaryAssembler.writeDTOs(authUser));
	}
    
	/**
	 * Creates a new user in the system.
	 * 
	 * @param userDTO
	 */
    public static void create(@Valid UserDTO user) {
    	if (validation.hasErrors()) {
    		for (Error error : validation.errors()) {
    			Logger.debug(error.getKey() + " : " + error.message());
    		}
			error(400, "Validation Errors");
		}
    	
    	// Check for existing users
    	User checkUser = User.find("byEmail", user.email).first();
		if (checkUser != null) {
			error(400, "Email already exists");
		}
		checkUser = User.find("byServiceName", user.serviceName).first();
		if (checkUser != null) {
			error(400, "ServiceName already exists");
		}
    	
    	// Create user
		UserDTO newUserDTO = UserAssembler.createUser(user);
		response.status = 201;
		renderJSON(newUserDTO);
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
     * TODO: revisit
     * 
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