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
import results.RenderCustomJson;
import DTO.RecentUserLocationsDTO;
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
	
	/**
	 * Returns a list of Users with their locations, which are attending the same 
	 * meetings as the user specified.
	 */
	public static List<RecentUserLocationsDTO> recentUserLocations(User user) {
		
		List<RecentUserLocationsDTO> recentUserLocationsList = new ArrayList<RecentUserLocationsDTO>();
		
		// Get the meetings associated with the user
		for (Attendee attendeeThisUser : user.meetingsRelated) {
			
			Meeting meeting = attendeeThisUser.meeting;
			
			//TODO: time before could be configurable for each attendee
			Calendar timeBeforeMeeting = new GregorianCalendar();
			timeBeforeMeeting.setTime(meeting.time);
			timeBeforeMeeting.add(java.util.Calendar.MINUTE, -15);
			
			Calendar timeAfterMeeting = new GregorianCalendar();
			timeAfterMeeting.setTime(meeting.time);
			timeAfterMeeting.add(java.util.Calendar.MINUTE, 15);
			
			// Get the Users who are attending
			for (Attendee attendee : meeting.attendees) {
				// If not the current user
				if (!attendee.user.equals(user)) {
					if (attendee.rsvp == Attendee.MeetingResponse.YES) {
						User otherUser = attendee.user;
						// check that the user has not already been added to the list from another meeting
						// otherwise there will be duplicate user data in the response
						boolean userExists = false;
						for (RecentUserLocationsDTO recentUserLocationsDTO : recentUserLocationsList) {
							if (otherUser.id.equals(recentUserLocationsDTO.id)) {
								userExists = true;
								break;
							}
						}
						if (!userExists) {
							RecentUserLocationsDTO recentUserLocationsDTO = recentUserLocation(otherUser, timeBeforeMeeting.getTime(), timeAfterMeeting.getTime());
							if (recentUserLocationsDTO != null) {
								recentUserLocationsList.add(recentUserLocationsDTO);
							}
						}
					}
				}
			}
		}
		return recentUserLocationsList;
	}
	
	/**
	 * 
	 * @param user
	 * @param before
	 * @param after
	 * @return
	 */
	public static RecentUserLocationsDTO recentUserLocation(User user, Date before, Date after) {
		
		RecentUserLocationsDTO recentUserLocation = null;
		
		// Get the recent locations
		List<UserLocation> userLocations = UserLocation.find("user = ? and time between ? and ?", user, before, after).fetch();
		if (userLocations != null) {
			recentUserLocation = new RecentUserLocationsDTO();
			recentUserLocation.id = user.id;
			recentUserLocation.firstName = user.firstName;
			recentUserLocation.lastName = user.lastName;
			recentUserLocation.locationHistory = writeDTOs(userLocations);
		}
		return recentUserLocation;
	}

}
