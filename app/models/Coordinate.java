package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class Coordinate extends Model {
	
	public double longitude;
	public double latitude;
	
	public Coordinate(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

}
