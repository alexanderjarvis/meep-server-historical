package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
@Table(name = "Coordinate", uniqueConstraints = @UniqueConstraint(columnNames={"latitude","longitude"}))
public class Coordinate extends Model {
	
	public Double latitude;
	
	public Double longitude;
	
	public Coordinate(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

}