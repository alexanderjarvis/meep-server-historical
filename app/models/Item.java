package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

/**
 * Enables auditing of model classes that extend this type by providing
 * date created and date modified attributes that are handled automatically.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class Item extends Model {
	
	public Date dateCreated;
	public Date dateModified;
	
	public Item() {
		final Date today = new Date();
		this.dateCreated = today;
		this.dateModified = today;
	}
	
	@SuppressWarnings("unused")
	@PreUpdate
	private void updateDateModified() {
		this.dateModified = new Date();
	}
    
}
