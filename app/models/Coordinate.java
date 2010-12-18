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
	public Double longitude;
	
	@Required
	public Double latitude;
	
	public Coordinate(Double longitude, Double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

}