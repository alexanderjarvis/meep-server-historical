package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@Entity
public class Attendee extends Model {
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Meeting meeting;
	
	@Enumerated(EnumType.STRING)
	public MeetingResponse rsvp;
	
	public enum MeetingResponse {
		YES, MAYBE, NO
	}
	
	public Attendee(User user, Meeting meeting) {
		this.user = user;
		this.meeting = meeting;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public Meeting getMeeting() {
		return this.meeting;
	}

}