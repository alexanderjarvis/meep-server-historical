package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class UserConnection extends Model {
	
	@ManyToOne
	public User user;
	
	@OneToOne
	public UserConnection userConnection;
	
}
