package DTO;

/**
 * Represents an Attendee of a Meeting with respect to a specific User, with
 * some basic information about the User.
 * 
 * Associated with the Attendee model but optimised for transport.
 * 
 * @see Attendee
 * @see User
 * @see Meeting
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AttendeeDTO {

	/**
	 * This is the id of the User - NOT the Attendee.
	 */
	public Long id;

	/**
	 * The first name of the User that this Attendee represents.
	 */
	public String firstName;

	/**
	 * The last name of the User that this Attendee represents.
	 */
	public String lastName;

	/**
	 * The R.S.V.P status of this Attendee
	 */
	public String rsvp;

	/**
	 * The number of minutes before a meeting an Attendee wishes to be notified.
	 */
	public Integer minutesBefore;

}
