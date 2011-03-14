package models;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import play.db.jpa.Model;

/**
 * Enables auditing of model classes that extend this type by providing
 * date created and date modified attributes that are handled automatically.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@MappedSuperclass
public class Item extends Model {
	
	public Date dateCreated;
	public Date dateModified;
	
	@PrePersist
	protected void onCreate() {
		this.dateCreated = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.dateModified = new Date();
	}
    
}