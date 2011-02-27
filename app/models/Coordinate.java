package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class Coordinate extends Model {
	
	@Required
	public Double latitude;
	
	@Required
	public Double longitude;
	
	public Coordinate(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

}