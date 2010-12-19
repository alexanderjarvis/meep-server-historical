package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Required;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@Entity
public class Meeting extends Item {
	
	@Required
	public Date time;
	
	@OneToOne
	public Coordinate place;
	
	@OneToMany(mappedBy="meeting")
	public List<Attendee> attendees;
	
	@ManyToOne
	public User owner;
	
	public String title;
	public String description;
	public String type;
	
	public Meeting(Date time, Coordinate place, User owner) {
		this.time = time;
		this.place = place;
		this.owner = owner;
		this.attendees = new ArrayList<Attendee>();
	}
	
	public boolean addAttendee(User user) {
		Attendee attendee = new Attendee(user, this);
		return attendees.add(attendee);
	}
	
	public boolean setAttendeeRSVP(User user, Attendee.MeetingResponse rsvp) {
		for (Attendee attendee : attendees) {
			if (attendee.getUser().equals(user)) {
				attendee.rsvp = rsvp;
				return true;
			}
		}
		return false;
	}

}