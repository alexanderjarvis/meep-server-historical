package controllers;

import java.util.ArrayList;
import java.util.List;

import models.User;
import models.helpers.UserConnectionHelper;
import notifiers.Mails;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.data.validation.Error;
import DTO.UserDTO;
import DTO.UserSummaryDTO;
import assemblers.UserAssembler;
import assemblers.UserSummaryAssembler;

import com.google.gson.JsonObject;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Users extends ServiceApplicationController {
	
	/**
	 * Returns all users that the current authorised user is connected to
	 * (not all users in the system).
	 */
	public static void index() {
		User authUser = getAuthorisedUser();
		renderJSON(UserSummaryAssembler.writeDTOs(authUser));
	}
    
	/**
	 * Creates a new user in the system.
	 * 
	 * @param body
	 */
	public static void create(JsonObject body) {
    	
    	if (body != null && body.isJsonObject()) {
    		
    		UserDTO userDTO = UserAssembler.userDTOWithJsonObject(body);
    		
    		validation.valid(userDTO);
    		if (validation.hasErrors()) {
        		for (Error error : validation.errors()) {
        			Logger.debug(error.getKey() + " : " + error.message());
        		}
    			error(400, "Validation Errors");
    		}
    		
    		if (userDTO != null) {
    			// Check for existing users
    	    	User checkEmail = User.find("byEmail", userDTO.email).first();
    			if (checkEmail != null) {
    				error(400, "Email already exists");
    			}
    			
    			// Check for existing phone number
    			if (StringUtils.isNotBlank(userDTO.mobileNumber)) {
	    			User checkNumber = User.find("byMobileNumber", userDTO.mobileNumber).first();
	    			if (checkNumber != null) {
	    				error(400, "Mobile number already exists");
	    			}
    			}
    	    	
    	    	// Create user
    			UserDTO newUserDTO = UserAssembler.createUser(userDTO);
    			
    			// Send email
    			Mails.welcome(newUserDTO);
    			
    			response.status = 201;
    			renderJSON(newUserDTO);
    		}
    	} else {
    		badRequest();
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
    	
    	User authUser = getAuthorisedUser();
    	
		if (id.equals(authUser.id.toString()) || id.equals(authUser.email)) {
			renderJSON(UserAssembler.writeDTO(authUser, true));
    	} else {
    		
    		User user = getUserFromIdOrEmail(id);
    		
    		// is the user connected?
    		if (user != null) {
	    		if (UserConnectionHelper.isUsersConnected(authUser, user)) {
	    			renderJSON(UserAssembler.writeDTO(user, false));
	    		} else {
	    			error(400, "User not connected");
	    		}
    		} else {
    			notFound();
    		}
    	}
    }
    
    /**
     * Updates the User specified in the resource URI with the contents of the UserDTO
     * in the body of the request.
     * 
     * @param user
     */
    public static void update(String id, JsonObject body) {
    	
    	// Only able to update the authorised user
    	User authUser = getAuthorisedUser();
    	User user = getUserFromIdOrEmail(id);
    	if (user != null && user.equals(authUser)) {
    		
    		if (body != null && body.isJsonObject()) {
        		
        		UserDTO userDTO = UserAssembler.userDTOWithJsonObject(body);
        		if (userDTO != null) {
        			
        			// Validation
            		validation.valid(userDTO);
            		if (validation.hasErrors()) {
                		for (Error error : validation.errors()) {
                			Logger.debug(error.getKey() + " : " + error.message());
                		}
            			error(400, "Validation Errors");
            		}
            		
        			// If the email has changed check that another user does not have it.
            		if (!authUser.email.equalsIgnoreCase(userDTO.email)) {
            			User checkUser = User.find("byEmail", userDTO.email).first();
            	    	if (checkUser != null) {
            				error(400, "Email already exists");
            			}
            		}
        	    	
        	    	// Update user
        	    	userDTO.id = user.id;
        	    	renderJSON(UserAssembler.updateUser(userDTO));
        		}
    		}
    	}
    	badRequest();
    }
    
    /**
     * Deletes the connection between the authorised User and the User specified.
     * @param id
     */
    public static void delete(String id) {
    	User authUser = getAuthorisedUser();
    	User otherUser = getNonAuthorisedUser(id);
    	if (otherUser != null) {
    		if (UserConnectionHelper.removeUserConnection(authUser, otherUser)) {
    			ok();
    		} else {
    			badRequest();
    		}
    	}
    	notFound();
    }
    
    /**
     * 
     * @param id
     */
    public static void addUserRequest(String id) {
    	User authUser = getAuthorisedUser();
    	User otherUser = getNonAuthorisedUser(id);
    	if (otherUser != null) {
    		if (UserConnectionHelper.createUserConnectionRequest(authUser, otherUser)) {
    			// Send email
    			Mails.newFriendRequest(UserAssembler.writeDTO(otherUser), UserAssembler.writeDTO(authUser));
    			ok();
    		}
    	}
    	badRequest();
    }
    
    /**
     * 
     * @param id
     */
    public static void acceptUserRequest(String id) {
    	User authUser = getAuthorisedUser();
    	User otherUser = getNonAuthorisedUser(id);
    	if (otherUser != null) {
    		if (UserConnectionHelper.removeUserConnectionRequest(otherUser, authUser)) {
    			UserConnectionHelper.createUserConnection(authUser, otherUser);
    			ok();
    		}
    	}
    	badRequest();
    }
    
    /**
     * 
     * @param id
     */
    public static void declineUserRequest(String id) {
    	User authUser = getAuthorisedUser();
    	User otherUser = getNonAuthorisedUser(id);
    	if (otherUser != null) {
    		if (UserConnectionHelper.removeUserConnectionRequest(otherUser, authUser)) {
    			ok();
    		}
    	}
    	badRequest();
    }
    
    /**
     * Searches for users
     * @param query
     */
    public static void searchUsers(String query) {
    	query = query.trim();
    	String firstName = query;
    	String lastName = query;
    	
    	String[] names = query.split("\\s");
    	if (names.length >= 2) {
    		firstName = names[0];
    		lastName = names[1];
    	}
    	
    	// TODO: move this query away from the controller and/or implement lucene based search
    	List<User> userResults = User.find("select u from User u "
    			+ "where LOWER(u.firstName) like LOWER(?) "
    			+ "or LOWER(u.lastName) like LOWER(?)", firstName+"%", lastName+"%").fetch(50);
    	
    	if (userResults != null && userResults.size() > 0) {
    		List<UserSummaryDTO> userSummaryList = new ArrayList<UserSummaryDTO>();
    		for (User user : userResults) {
    			userSummaryList.add(UserSummaryAssembler.writeRestrictedDTO(user));
    		}
    		renderJSON(userSummaryList);
    	} else {
    		error(404, "Not found");
    	}
    
    }
    
    /**
     * Returns the User where the User cannot be the authorised User.
     * 
     * When the id matches the authorised User, this method returns null.
     * 
     * @param id
     * @return
     */
    private static User getNonAuthorisedUser(String id) {
    	User authUser = getAuthorisedUser();
    	
    	// If the id is the authorised user
    	if (!id.equals(authUser.id.toString()) && !id.equals(authUser.email)) {
    		return getUserFromIdOrEmail(id);
    	}
    	
    	return null;
    }
    
    /**
     * Returns the User from the id parameter in the URL. This could either be the
     * id of type Long held against the User - or it could be their email.
     * 
     * @param id
     * @return
     */
    private static User getUserFromIdOrEmail(String id) {
    	// determine the ID type
		boolean emailID = true;
		Long longID = null;
		try {
			longID = new Long(id);
			emailID = false;
		} catch (NumberFormatException e) {
			Logger.debug("User id is not a number, maybe an email");
		}
		
		// get the User
		User user = null;
		if (emailID) {
			user = User.find("byEmail", id).first();
		} else {
			user = User.findById(longID);
		}
		
		if (user != null) {
			return user;
		}
		return null;
    }

}