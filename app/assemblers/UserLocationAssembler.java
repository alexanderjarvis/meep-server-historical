package assemblers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
import models.UserLocation;

import org.joda.time.DateTime;

import utils.GsonFactory;
import DTO.RecentUserLocationsDTO;
import DTO.UserDTO;
import DTO.UserLocationDTO;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserLocationAssembler {
	
	/**
	 * 
	 * @param userLocation
	 * @return
	 */
	public static UserLocationDTO writeDTO(UserLocation userLocation) {
		UserLocationDTO userLocationDTO = new UserLocationDTO();
		userLocationDTO.time = new DateTime(userLocation.time);
		userLocationDTO.coordinate = CoordinateAssembler.writeDTO(userLocation.coordinate);
		userLocationDTO.speed = userLocation.speed;
		userLocationDTO.altitude = userLocation.altitude;
		userLocationDTO.trueHeading = userLocation.trueHeading;
		userLocationDTO.horizontalAccuracy = userLocation.horizontalAccuracy;
		userLocationDTO.verticalAccuracy = userLocation.verticalAccuracy;
		return userLocationDTO;
	}
	
	/**
	 * 
	 * @param userLocations
	 * @return
	 */
	public static List<UserLocationDTO> writeDTOs(List<UserLocation> userLocations) {
		List<UserLocationDTO> userLocationList = new ArrayList<UserLocationDTO>();
		for (UserLocation userLocation : userLocations) {
			userLocationList.add(writeDTO(userLocation));
		}
		return userLocationList;
	}
	
	/**
	 * This method is private because it does not notify other users location streams and is
	 * always intended to be called internally by createUserLocations()
	 * 
	 * @param userLocationDTO
	 * @param user
	 */
	private static UserLocation createUserLocation(UserLocationDTO userLocationDTO, User user) {
		UserLocation userLocation = new UserLocation();
		userLocation.user = user;
		userLocation.time = userLocationDTO.time.toDate();
		userLocation.coordinate = CoordinateAssembler.createCoordinate(userLocationDTO.coordinate);
		userLocation.speed = userLocationDTO.speed;
		userLocation.altitude = userLocationDTO.altitude;
		userLocation.trueHeading = userLocationDTO.trueHeading;
		userLocation.horizontalAccuracy = userLocationDTO.horizontalAccuracy;
		userLocation.verticalAccuracy = userLocationDTO.verticalAccuracy;
		userLocation.create();
		return userLocation;
	}
	
	/**
	 * 
	 * @param userLocationDTOs
	 */
	public static List<UserLocationDTO> createUserLocations(List<UserLocationDTO> userLocationDTOs, UserDTO userDTO) {
		
		User user = User.findById(userDTO.id);
		
		List<UserLocationDTO> createdUserLocationDTOs = new ArrayList<UserLocationDTO>();
		
		if (user != null) {
			for (UserLocationDTO userLocationDTO : userLocationDTOs) {
				UserLocation userLocation = createUserLocation(userLocationDTO, user);
				createdUserLocationDTOs.add(writeDTO(userLocation));
			}
		}
		
		return createdUserLocationDTOs;
	}
	
	/**
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static List<UserLocationDTO> userLocationDTOsWithJsonArray(JsonArray jsonArray) {
		GsonBuilder gsonBuilder = GsonFactory.gsonBuilder();
		Type collectionType = new TypeToken<List<UserLocationDTO>>(){}.getType();
		List<UserLocationDTO> userLocationDTOs = gsonBuilder.create().fromJson(jsonArray, collectionType);
		return userLocationDTOs;
	}
	
}
