package controllers.websockets;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Attendee;
import models.Meeting;
import models.User;
import DTO.RecentUserLocationsDTO;
import DTO.UserDTO;
import DTO.UserLocationDTO;
import assemblers.RecentUserLocationsAssembler;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LocationStreamHelper {
	
	/**
	 * 
	 * @param userLocations
	 * @param user
	 */
	public static void publishNewUserLocations(List<UserLocationDTO> userLocations, UserDTO userDTO) {
		
		User user = User.findById(userDTO.id);
		Set<User> usersToPublishTo = getUsersToPublishTo(user);
		
		if (!usersToPublishTo.isEmpty()) {
			
			RecentUserLocationsDTO recentUserLocationsDTO = RecentUserLocationsAssembler.writeDTO(user, userLocations);
			LocationEvent.OtherUserUpdated locationEvent = new LocationEvent.OtherUserUpdated(recentUserLocationsDTO);
			
			LocationStreamManager locationStreamManager = LocationStreamManager.getInstance();
			for (User otherUser : usersToPublishTo) {
				locationStreamManager.publishLocationEventToUserWithId(otherUser.id, locationEvent);
			}
		}
	}
	
	/**
	 * Returns a valid list of users that this user can publish to based on if they
	 * are attending the same meeting and the time range for the meetings is valid.
	 * 
	 * @param user
	 * @return
	 */
	public static Set<User> getUsersToPublishTo(User user) {
		
		Set<User> usersToPublishTo = new HashSet<User>();
		
		Calendar timeNow = new GregorianCalendar();
		
		// Get the meetings associated with the user
		for (Attendee attendeeThisUser : user.meetingsRelated) {
			
			// Only if this user is attending, then share their location
			if (attendeeThisUser.rsvp == Attendee.MeetingResponse.YES) {
			
				Meeting meeting = attendeeThisUser.meeting;
				
				//TODO: time before could be configurable for each attendee
				Calendar timeBeforeMeeting = new GregorianCalendar();
				timeBeforeMeeting.setTime(meeting.time);
				timeBeforeMeeting.add(java.util.Calendar.MINUTE, -15);
				
				Calendar timeAfterMeeting = new GregorianCalendar();
				timeAfterMeeting.setTime(meeting.time);
				timeAfterMeeting.add(java.util.Calendar.MINUTE, 15);
				
				// Time now between (or equal to) the valid meeting time range (15 minutes either side).
				//if (timeNow.compareTo(timeBeforeMeeting) >= 0 && timeNow.compareTo(timeAfterMeeting) <= 0) {
					
					// Get the Users who are attending
					for (Attendee attendee : meeting.attendees) {
						// If not the current user
						if (!attendee.user.equals(user)) {
							// If attending
							if (attendee.rsvp == Attendee.MeetingResponse.YES) {
								
								User otherUser = attendee.user;
								usersToPublishTo.add(otherUser);
							}
						}
					}
				//}
			}
		}
		
		return usersToPublishTo;
	}

}
