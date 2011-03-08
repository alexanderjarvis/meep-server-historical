package assemblers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import models.User;
import models.UserLocation;
import results.RenderCustomJson;
import DTO.UserLocationDTO;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LocationsAssembler {
	
	/**
	 * 
	 * @param userLocation
	 * @return
	 */
	public static UserLocationDTO writeDTO(UserLocation userLocation) {
		UserLocationDTO userLocationDTO = new UserLocationDTO();
		userLocationDTO.time = userLocation.time;
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
	 * @param userLocationDTO
	 * @param user
	 */
	public static UserLocationDTO createUserLocation(UserLocationDTO userLocationDTO, User user) {
		UserLocation userLocation = new UserLocation();
		userLocation.user = user;
		userLocation.time = userLocationDTO.time;
		userLocation.coordinate = CoordinateAssembler.createCoordinate(userLocationDTO.coordinate);
		userLocation.speed = userLocationDTO.speed;
		userLocation.altitude = userLocationDTO.altitude;
		userLocation.trueHeading = userLocationDTO.trueHeading;
		userLocation.horizontalAccuracy = userLocationDTO.horizontalAccuracy;
		userLocation.verticalAccuracy = userLocationDTO.verticalAccuracy;
		userLocation.save();
		user.locationHistory.add(userLocation);
		user.save();
		return writeDTO(userLocation);
	}
	
	/**
	 * 
	 * @param userLocationDTOs
	 */
	public static List<UserLocationDTO> createUserLocations(List<UserLocationDTO> userLocationDTOs, User user) {
		
		List<UserLocationDTO> createdUserLocationDTOs = new ArrayList<UserLocationDTO>();
		for (UserLocationDTO userLocationDTO : userLocationDTOs) {
			createdUserLocationDTOs.add(createUserLocation(userLocationDTO, user));
		}
		return createdUserLocationDTOs;
	}
	
	/**
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static List<UserLocationDTO> userLocationDTOsWithJsonArray(JsonArray jsonArray) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat(RenderCustomJson.ISO8601_DATE_FORMAT);
		Type collectionType = new TypeToken<List<UserLocationDTO>>(){}.getType();
		List<UserLocationDTO> userLocationDTOs = gsonBuilder.create().fromJson(jsonArray, collectionType);
		return userLocationDTOs;
	}

}
