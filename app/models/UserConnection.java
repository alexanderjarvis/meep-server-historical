package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table( uniqueConstraints = { @UniqueConstraint(columnNames = {"user1_id", "user2_id" }) })
public class UserConnection extends Model {
	
	@ManyToOne
	@JoinColumn(name = "user1_id")
	private User user1;
	
	@ManyToOne
	@JoinColumn(name = "user2_id")
	private User user2;
	
	public UserConnection(User user1, User user2) {
		// This trick ensures that the constraint will work no matter which user is user1.
		// And will therefore prevent duplication of the data in reverse.
		if (user1.id < user2.id) {
			this.user1 = user1;
			this.user2 = user2;
		} else {
			this.user1 = user2;
			this.user2 = user1;
		}
	}
	
	public User getUser1() {
		return this.user1;
	}
	
	public User getUser2() {
		return this.user2;
	}
	
	@Override
	public JPASupport delete() {
		user1.removeConnectionWithId(user2.id);
		user2.removeConnectionWithId(user1.id);
		
		return super.delete();
	}
    
}
