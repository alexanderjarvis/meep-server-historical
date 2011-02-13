package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class UserLocation extends Model {
	
	public Coordinate coordinate;
	
	public Date time;
	
	public Double accuracy;
	
	public Double speed;
    
}
