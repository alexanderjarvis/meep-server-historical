package assemblers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
import models.UserLocation;
import DTO.RecentUserLocationsDTO;
import DTO.UserLocationDTO;

/**
 * Assembler for the RecenUserLocationsDTO and associated classes.
 * 
 * @see RecentUserLocationsDTO
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class RecentUserLocationsAssembler {
	
	/**
	 * Writes a RecentUserLocationsDTO for the specified User with a list of their location history.
	 * 
	 * @param user
	 * @param locationHistory
	 * @return
	 */
	public static RecentUserLocationsDTO writeDTO(User user, List<UserLocationDTO> locationHistory) {
		
		RecentUserLocationsDTO recentUserLocation = new RecentUserLocationsDTO();
		recentUserLocation.id = user.id;
		recentUserLocation.firstName = user.firstName;
		recentUserLocation.lastName = user.lastName;
		recentUserLocation.locationHistory = locationHistory;
		
		return recentUserLocation;
		
	}
	
	/**
	 * Returns a list of Users with their locations, which are attending the same 
	 * meetings as the user specified.
	 */
	@Deprecated
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
					// If attending
					if (attendee.rsvp == Attendee.MeetingResponse.YES) {
						User otherUser = attendee.user;
						
						// Check that the user has not already been added to the list from another meeting
						// otherwise there will be duplicate user data in the response
						boolean userExists = false;
						for (RecentUserLocationsDTO recentUserLocationsDTO : recentUserLocationsList) {
							if (otherUser.id.equals(recentUserLocationsDTO.id)) {
								userExists = true;
								
								// Merge this meetings data with the data already collected
								RecentUserLocationsDTO thisMeetingsUserLocations = recentUserLocation(otherUser, timeBeforeMeeting.getTime(), timeAfterMeeting.getTime());
								if (thisMeetingsUserLocations != null) {
									//TODO: merge
									//recentUserLocationsDTO.locationHistory.
								}
								
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
	 * Returns a RecentUserLocationsDTO for a User with the locations inbetween the specified date range.
	 * 
	 * @param user
	 * @param before
	 * @param after
	 * @return
	 */
	@Deprecated
	public static RecentUserLocationsDTO recentUserLocation(User user, Date before, Date after) {
		
		RecentUserLocationsDTO recentUserLocation = null;
		
		// Get the recent locations
		List<UserLocation> userLocations = UserLocation.find("user = ? and time between ? and ?", user, before, after).fetch();
		if (userLocations != null) {
			recentUserLocation = new RecentUserLocationsDTO();
			recentUserLocation.id = user.id;
			recentUserLocation.firstName = user.firstName;
			recentUserLocation.lastName = user.lastName;
			recentUserLocation.locationHistory = UserLocationAssembler.writeDTOs(userLocations);
		}
		return recentUserLocation;
	}

}
