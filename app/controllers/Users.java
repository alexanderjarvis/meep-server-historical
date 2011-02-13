package controllers;

import java.util.ArrayList;
import java.util.List;

import models.User;
import models.UserConnection;
import models.helpers.UserConnectionHelper;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Valid;
import DTO.UserDTO;
import DTO.UserSummaryDTO;
import assemblers.UserAssembler;
import assemblers.UserSummaryAssembler;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Users extends Application {
	
	/**
	 * Returns all users that the current authorised user is connected to
	 * (not all users in the system).
	 */
	public static void index() {
		User authUser = userAuth.getAuthroisedUser();
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
    	
    	if (user != null) {
	    	// Check for existing users
	    	User checkUser = User.find("byEmail", user.email).first();
			if (checkUser != null) {
				error(400, "Email already exists");
			}
	    	
	    	// Create user
			UserDTO newUserDTO = UserAssembler.createUser(user);
			response.status = 201;
			renderJSON(newUserDTO);
    	} else {
    		error(500, "User is null");
    	}
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
    public static void show(String id) {
    	
    	User authUser = userAuth.getAuthroisedUser();
    	
		if (id.equals(authUser.id.toString()) || id.equals(authUser.email)) {
			renderJSON(UserAssembler.writeDTO(authUser, true));
    	} else {
    		// get the user, first by ID and then by email
    		boolean emailID = true;
    		try {
    			Long longID = new Long(id);
    			emailID = false;
    		} catch (NumberFormatException e) {
    			Logger.debug("User id is not a number, maybe an email");
    		}
    		
    		User user = null;
    		if (emailID) {
    			user = User.find("byEmail", id).first();
    		} else {
    			user = User.findById(new Long(id));
    		}
    		
    		// is the user connected?
    		// TODO: user isUserConnected method instead
    		if (user != null) {
		    	
	    		if (emailID) {
	    			for (UserConnection connection : authUser.connections) {
	    				if (id.equals(connection.userConnection.user.email)) {
			    			renderJSON(UserAssembler.writeDTO(user, false));
			    		}
	    			}
	    		} else {
	    			for (UserConnection connection : authUser.connections) {
	    				if (id.equals(connection.userConnection.user.id.toString())) {
			    			renderJSON(UserAssembler.writeDTO(user, false));
			    		}
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
     * TODO: revisit to enable updating via email id.
     * 
     * @param user
     */
    public static void update(Long id, @Valid UserDTO user) {
    	
    	// Only able to update the authorised user
    	if (id.equals(userAuth.getAuthroisedUser().id)) {
    		
	    	if (validation.hasErrors()) {
	    		for (Error error : validation.errors()) {
	    			Logger.debug(error.getKey() + " : " + error.message());
	    		}
				error(400, "Validation Errors");
			}
	    	
	    	// Check for existing emails
	    	User checkUser = User.find("byEmail", user.email).first();
			if (checkUser != null && !id.equals(checkUser.id)) {
				error(400, "Email already exists");
			}
	    	
	    	// Update user
	    	user.id = id;
	    	renderJSON(UserAssembler.updateUser(user));
	    	
    	} else {
    		badRequest();
    	}
    }
    
    
    
    /**
     * 
     * @param id
     */
    public static void addUserRequest(String id) {
    	User authUser = userAuth.getAuthroisedUser();
    	User otherUser = getNonAuthorisedUser(id);
    	if (otherUser != null) {
    		boolean success = UserConnectionHelper.createUserConnectionRequest(authUser, otherUser);
    		if (success) {
    			renderJSON("");
    		}
    	}
    	badRequest();
    }
    
    public static void acceptUserRequest(String id) {
    	User authUser = userAuth.getAuthroisedUser();
    	User otherUser = getNonAuthorisedUser(id);
    	if (otherUser != null) {
    		boolean success = UserConnectionHelper.removeUserConnectionRequest(otherUser, authUser);
    		if (success) {
    			UserConnectionHelper.createUserConnection(authUser, otherUser);
    			renderJSON("");
    		}
    	}
    	badRequest();
    }
    
    public static void declineUserRequest(String id) {
    	User authUser = userAuth.getAuthroisedUser();
    	User otherUser = getNonAuthorisedUser(id);
    	if (otherUser != null) {
    		boolean success = UserConnectionHelper.removeUserConnectionRequest(otherUser, authUser);
    		if (success) {
    			renderJSON("");
    		}
    	}
    	badRequest();
    }
    
    /**
     * For now, just search by firstname
     * @param search
     */
    public static void searchUser(String search) {
    	List<User> userResults = User.find("byFirstName", search).fetch();
    	
    	if (userResults != null && userResults.size() > 0) {
    		List<UserSummaryDTO> userSummaryList = new ArrayList<UserSummaryDTO>();
    		for (User user : userResults) {
    			userSummaryList.add(UserSummaryAssembler.writeDTO(user));
    		}
    		renderJSON(userSummaryList);
    	} else {
    		error(404, "Not found");
    	}
    
    }
    
    /**
     * 
     * @param id
     * @return
     */
    private static User getNonAuthorisedUser(String id) {
    	User authUser = userAuth.getAuthroisedUser();
    	
    	// If the id is the authorised user (trying to add themselves)
    	if (id.equals(authUser.id.toString()) || id.equals(authUser.email)) {
    		badRequest();
    	} else {
    		
    		// determine the ID type
    		boolean emailID = true;
    		try {
    			Long longID = new Long(id);
    			emailID = false;
    		} catch (NumberFormatException e) {
    			Logger.debug("User id is not a number, maybe an email");
    		}
    		// get the User
    		User otherUser = null;
    		if (emailID) {
    			otherUser = User.find("byEmail", id).first();
    		} else {
    			otherUser = User.findById(new Long(id));
    		}
    		if (otherUser != null) {
    			return otherUser;
    		}
    	}
    	
    	return null;
    }

}