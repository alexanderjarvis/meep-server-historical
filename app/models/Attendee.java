package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

/**
 * Each User is linked to a Meeting via an Attendee object (with a separate
 * instance for each meeting).
 * 
 * This allows the Attendee object to represent the Users connection with the
 * Meeting and encapsulates extra information about the User with respect to the
 * meeting, e.g. R.S.V.P status.
 * 
 * @see User
 * @see Meeting
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class Attendee extends Item {
	
	/**
	 * The User that this Attendee represents.
	 */
	@ManyToOne
	public User user;
	
	/**
	 * The Meeting that this Attendee is connected to.
	 */
	@ManyToOne
	public Meeting meeting;
	
	/**
	 * The R.S.V.P status of this attendee
	 */
	@Enumerated(EnumType.STRING)
	public MeetingResponse rsvp;
	
	/**
	 * The possible responses to a Meeting.
	 * Currently only YES/NO is supported at the application level,
	 * but a MAYBE response is enabled for forward compatibility.
	 * 
	 * @author Alex Jarvis axj7@aber.ac.uk
	 */
	public enum MeetingResponse {
		YES, MAYBE, NO
	}
	
	/**
	 * The number of minutes before a meeting an Attendee wishes to be notified.
	 */
	public Integer minutesBefore;

	@Override
	public GenericModel delete() {
		
		user.meetingsRelated.remove(this);
		user.save();
		meeting.attendees.remove(this);
		meeting.save();

		return super.delete();
	}

}