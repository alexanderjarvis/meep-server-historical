package models.helpers;

import models.Attendee;
import models.Meeting;
import models.User;
import models.Attendee.MeetingResponse;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingHelper {
	
	public static void createAttendee(Meeting meeting, User user) {
		Attendee attendee = new Attendee();
		attendee.meeting = meeting;
		attendee.user = user;
		attendee.save();
		meeting.attendees.add(attendee);
		meeting.save();
		user.meetingsRelated.add(attendee);
		user.save();
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
	
	public static boolean acceptMeetingRequest(Meeting meeting, User user) {
		return setAttendeeRSVP(meeting, user, MeetingResponse.YES);
	}
	
	public static boolean declineMeetingRequest(Meeting meeting, User user) {
		return setAttendeeRSVP(meeting, user, MeetingResponse.NO);
	}
	
	public static boolean isUserInMeetingAttendees(User user, Meeting meeting) {
		for (Attendee attendee : meeting.attendees) {
			if (attendee.user.equals(user)) {
				return true;
			}
		}
		return false;
	}

}
