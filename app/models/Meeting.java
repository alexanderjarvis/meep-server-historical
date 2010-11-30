package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */

@Entity
public class Meeting extends Item {
	
	public Date time;
	
	@OneToOne
	public Coordinate place;
	
	@OneToMany
	public List<Attendee> attendees;
	
	@ManyToOne
	public User owner;
	
	public String title;
	public String description;
	public String type;
	
	public Meeting(Date time, Coordinate place, List<Attendee> attendees, User owner) {
		
		super();
		this.time = time;
		this.place = place;
		this.attendees = attendees;
		this.owner = owner;
	}

}
