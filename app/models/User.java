package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import play.data.validation.Email;
import play.data.validation.Match;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	@Email
	@Required
	public String email;
	//TODO: include special chars
	@Match("[A-Za-z0-9]{6,16}")
	@Required
    public String password;
	@MinSize(2)
	@Required
    public String firstName;
	@Required
    public String lastName;
	public String fullName;
	public Date dateCreated;
	public Date dateModified;
	
	@OneToOne
	public UserDetail userDetail;
    
    /**
     * A simple list of the relationships of this user to other users.
     * The application also stores this information in the UserConnection class,
     * but this representation is maintained as well because otherwise there would
     * be a cyclic representation.
     */
    public ArrayList<Long> connections;
    
    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        
        Date today = new Date();
        this.dateCreated = today;
        this.dateModified = today; 
        
        this.connections = new ArrayList<Long>();
    }
    
    public static User connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }
    
    public void removeConnectionWithId(Long id) {
    	connections.remove(connections.indexOf(id));
    }
    
    @Override
	public GenericModel delete() {
    	Query query = JPA.em().createQuery("SELECT uc FROM UserConnection uc WHERE user1.id = :user or user2.id = :user");
		query.setParameter("user", this.id);
		List<UserConnection> connections = query.getResultList();
		for (UserConnection userConnection : connections) {
			//TODO: add part where it updates the other users static list?
			userConnection.delete();
		}
		
		return super.delete();
	}
    
}
