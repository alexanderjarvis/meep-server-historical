package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

/**
 * Represents a Meeting in the system with all the details necessary to hold a
 * meeting e.g. time, place, attendees
 * 
 * Also holds auxiliary context information such as the title, description and
 * the owner of the meeting.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class Meeting extends Item {
	
	/**
	 * The time of the Meeting.
	 */
	public Date time;
	
	/**
	 * The location where the Meeting will take place.
	 */
	@OneToOne
	public Coordinate place;
	
	/**
	 * The Attendees that have been invited to the Meeting.
	 */
	@OneToMany(mappedBy = "meeting", cascade = { CascadeType.ALL })
	public List<Attendee> attendees = new ArrayList<Attendee>();
	
	/**
	 * The User that created the Meeting.
	 */
	@ManyToOne
	public User owner;
	
	/**
	 * A descriptive title for the Meeting.
	 */
	public String title;
	
	/**
	 * Extra information about the Meeting.
	 */
	public String description;

}