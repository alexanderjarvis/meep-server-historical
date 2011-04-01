package controllers;

import java.util.List;

import models.User;
import play.Logger;
import play.data.validation.Error;
import play.mvc.With;
import DTO.RecentUserLocationsDTO;
import DTO.UserDTO;
import DTO.UserLocationDTO;
import assemblers.UserLocationAssembler;

import com.google.gson.JsonArray;

import controllers.oauth2.AccessTokenFilter;
import controllers.websockets.LocationStreamHelper;

/**
 * This class is somewhat legacy and the LocationsSocket controller is intended to be used instead
 * to obtain recent locations and to send location updates.
 * 
 * It proved useful during development however as it enabled the development and testing of the
 * LocationsAssembler and all the associated domain/DTO objects without the complexity of the
 * WebSockets implementation which is much harder to test.
 * 
 * @see LocationsSocket
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Locations extends ServiceApplicationController {
	
	/**
	 * 
	 */
    public static void recent() {
    	User authUser = getAuthorisedUser();
    	List<RecentUserLocationsDTO> recentUserLocations = UserLocationAssembler.recentUserLocations(authUser);
    	renderJSON(recentUserLocations);
    }
    
    /**
     * 
     */
    public static void update(JsonArray body) {
        	
    	if (body != null && body.isJsonArray()) {
    		
    		List<UserLocationDTO> userLocationDTOs = UserLocationAssembler.userLocationDTOsWithJsonArray(body);
    		
    		validation.valid(userLocationDTOs);
    		if (validation.hasErrors()) {
        		for (Error error : validation.errors()) {
        			Logger.debug(error.getKey() + " : " + error.message());
        		}
    			error(400, "Validation Errors");
    		}
    		
    		// Update
    		if (userLocationDTOs != null) {
    			UserDTO currentUserDTO = getAuthorisedUserDTO();
    			List<UserLocationDTO> createdUserLocations = UserLocationAssembler.createUserLocations(userLocationDTOs, currentUserDTO);
    			
    			// Publish location to other users
    			LocationStreamHelper.publishNewUserLocations(createdUserLocations, currentUserDTO);
    			
    			renderJSON(createdUserLocations);
    		}
    	}
    	
    	badRequest();
    }
    
}
