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
	public List<Attendee> attendees = new ArrayList<Attendee>();
	
	@ManyToOne
	public User owner;
	
	public String title;
	
	public String description;
	
	public String type;

}