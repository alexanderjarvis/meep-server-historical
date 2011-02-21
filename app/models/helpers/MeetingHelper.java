package models.helpers;

import models.Attendee;
import models.Meeting;
import models.User;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingHelper {
	
	public static boolean addAttendee(Meeting meeting, User user) {
		
		Attendee attendee = new Attendee();
		attendee.meeting = meeting;
		attendee.user = user;
		
		attendee.save();
		return true;
	}
	
	public static boolean removeAttendee(Meeting meeting, User user) {
		
		for (Attendee attendee : meeting.attendees) {
			if (attendee.user.equals(user)) {
				attendee.delete();
				return true;
			}
		}
		return false;
	}
	
	public static boolean setAttendeeRSVP(Meeting meeting, User user, Attendee.MeetingResponse rsvp) {
		if (user != null) {
			for (Attendee attendee : meeting.attendees) {
				if (attendee.user.equals(user)) {
					attendee.rsvp = rsvp;
					attendee.save();
					return true;
				}
			}
		}
		return false;
	}

}
