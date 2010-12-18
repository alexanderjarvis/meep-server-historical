package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class UserLocation extends Model {
	
	public Coordinate coordinate;
	
	public Date time;
	
	public Double accuracy;
	
	public Double speed;
    
}
