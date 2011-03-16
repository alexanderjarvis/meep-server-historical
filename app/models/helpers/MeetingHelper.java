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
	
	/**
	 * Creates an Attendee for a Meeting with the User specified.
	 * @param meeting
	 * @param user
	 */
	public static void createAttendee(Meeting meeting, User user) {
		Attendee attendee = new Attendee();
		attendee.meeting = meeting;
		attendee.user = user;
		attendee.create();
		meeting.attendees.add(attendee);
		meeting.save();
		user.meetingsRelated.add(attendee);
		user.save();
	}
	
	/**
	 * Removes the Attendee from the Meeting where the Attendee is the User specified.
	 * @param meeting
	 * @param user
	 * @return
	 */
	public static boolean removeAttendee(Meeting meeting, User user) {
		
		for (Attendee attendee : meeting.attendees) {
			if (attendee.user.equals(user)) {
				attendee.delete();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets an Attendee's MeetingResponse (RSVP) status to that specified.
	 * @param meeting
	 * @param user
	 * @param rsvp
	 * @return
	 */
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
	
	/**
	 * Sets the User's MeetingResponse (RSVP) status to YES for a meeting
	 * where they are already an Attendee.
	 * @see MeetingResponse
	 * @param meeting
	 * @param user
	 * @return
	 */
	public static boolean acceptMeetingRequest(Meeting meeting, User user) {
		return setAttendeeRSVP(meeting, user, MeetingResponse.YES);
	}
	
	/**
	 * Sets the User's MeetingResponse (RSVP) status to NO for a meeting
	 * where they are already an Attendee.
	 * @see MeetingResponse
	 * @param meeting
	 * @param user
	 * @return
	 */
	public static boolean declineMeetingRequest(Meeting meeting, User user) {
		return setAttendeeRSVP(meeting, user, MeetingResponse.NO);
	}
	
	/**
	 * 
	 * @param meeting
	 * @param user
	 * @return
	 */
	public static boolean updateAttendeesMinutesBefore(Integer minutesBefore, Meeting meeting, User user) {
		if (user != null) {
			for (Attendee attendee : meeting.attendees) {
				if (attendee.user.equals(user)) {
					attendee.minutesBefore = minutesBefore;
					attendee.save();
					return true;
				}
			}
		}
		return false;
	}

}
