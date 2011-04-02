package DTO;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * Represents a Meeting as a DTO with enclosed DTOs to represent the place,
 * attendees and owner.
 * 
 * @see Meeting
 * @see CoordinateDTO
 * @see AttendeeDTO
 * @see UserSummaryDTO
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingDTO {
	
	/**
	 * The primary key of the Meeting.
	 */
	public Long id;
	
	/**
	 * The time of the Meeting.
	 */
	@Required
	public DateTime time;
	
	/**
	 * The location where the Meeting will take place.
	 */
	@Required
	public CoordinateDTO place;
	
	/**
	 * The Attendees that have been invited to the Meeting.
	 */
	public List<AttendeeDTO> attendees;
	
	/**
	 * The User that created the Meeting.
	 */
	public UserSummaryDTO owner;
	
	/**
	 * A descriptive title for the Meeting.
	 */
	@Required
	public String title;
	
	/**
	 * Extra information about the Meeting.
	 */
	public String description;

}
