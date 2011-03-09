package controllers;

import java.util.List;

import models.User;
import play.Logger;
import play.data.validation.Error;
import play.mvc.With;
import DTO.UserLocationDTO;
import assemblers.LocationsAssembler;

import com.google.gson.JsonArray;

import controllers.oauth2.AccessTokenFilter;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@With({JSONRequestTypeFilter.class, NoCookieFilter.class, LoggingFilter.class, SSLCheckFilter.class})
public class Locations extends AccessTokenFilter {
	
	/**
	 * 
	 */
    public static void index() {
    	User authUser = userAuth.getAuthorisedUser();
        ok();
    }
    
    /**
     * 
     */
    public static void update(JsonArray body) {
        	
    	if (body != null && body.isJsonArray()) {
    		
    		List<UserLocationDTO> userLocationDTOs = LocationsAssembler.userLocationDTOsWithJsonArray(body);
    		
    		validation.valid(userLocationDTOs);
    		if (validation.hasErrors()) {
        		for (Error error : validation.errors()) {
        			Logger.debug(error.getKey() + " : " + error.message());
        		}
    			error(400, "Validation Errors");
    		}
    		
    		// Update
    		if (userLocationDTOs != null) {
    			User authUser = userAuth.getAuthorisedUser();
    			List<UserLocationDTO> createdUserLocations = LocationsAssembler.createUserLocations(userLocationDTOs, authUser);
    			//TODO: notify observers of this users location
    			
    			renderJSON(createdUserLocations);
    		}
    	}
    	
    	badRequest();
    }

}
