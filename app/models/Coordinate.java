package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * Encapsulates the latitude and longitude values for a specific location.
 * 
 * Also has a unique constraint so that only one item can exist with the same latitude/longitude values.
 * This is to reduce the size of the database as otherwise there could be many, many duplicates.
 * 
 * To enforce this policy, there are no setters for the model, only a constructor with args.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
@Table(name = "Coordinate", uniqueConstraints = @UniqueConstraint(columnNames={"latitude","longitude"}))
public class Coordinate extends Model {
	
	/**
	 * The latitude value of this Coordinate
	 */
	private Double latitude;
	
	/**
	 * The longitude value of this Coordinate
	 */
	private Double longitude;
	
	/**
	 * A Coordinate can only set its latitude/longitude values when it is created.
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public Coordinate(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * @return The latitude value of this Coordinate
	 */
	public Double getLatitude() {
		return this.latitude;
	}
	
	/**
	 * @return The longitude value of this Coordinate
	 */
	public Double getLongitude() {
		return this.longitude;
	}

}