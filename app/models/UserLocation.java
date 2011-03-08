package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class UserLocation extends Model {
	
	@ManyToOne
	public User user;
	
	public Date time;
	
	@OneToOne
	public Coordinate coordinate;
	
	public Double speed;
	
	public Double altitude;
	
	public Double trueHeading;
	
	public Double verticalAccuracy;
	
	public Double horizontalAccuracy;
    
}
