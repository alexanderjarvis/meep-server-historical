package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class UserConnection extends Model {
	
	@ManyToOne
	public User user;
	
	@OneToOne
	public UserConnection userConnection;
	
	/**
	 * Not to be used without the UserConnectionHelper.removeUserConnection()
	 * helper method.
	 */
	@Override
	public UserConnection delete() {
		
		// Remove reference of this UserConnection from  the user
		user.connections.remove(this);
		
		// userConnection will be null if this is the 2nd UserConnection 
		// and the 1st has been de-referenced
		if (userConnection != null) {
			userConnection.userConnection = null;
			userConnection.save();
		}
		
		return super.delete();
	}
	
}
