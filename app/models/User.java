package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@Entity
public class User extends Item {
	
	@Email
	@Required
	@Column(unique=true) 
	public String email;
	
	public String passwordHash;
	
	public String accessToken;
	
	@Required
    public String firstName;
	
	@Required
    public String lastName;
	
	public String mobileNumber;
    
    @OneToMany(mappedBy="owner", cascade={CascadeType.ALL}, orphanRemoval=true)
    public List<Meeting> meetingsCreated;
    
    @OneToMany(mappedBy="user", cascade={CascadeType.ALL}, orphanRemoval=true)
    public List<Attendee> meetingsRelated;
    
    @OneToMany(mappedBy="user", cascade={CascadeType.REMOVE}, orphanRemoval=true)
    public List<UserConnection> connections;
    
    @ManyToMany
    public List<User> userConnectionRequestsTo;
    
    @ManyToMany(mappedBy="userConnectionRequestsTo")
    public List<User> userConnectionRequestsFrom;
    
    public User() {
    	connections = new ArrayList<UserConnection>();
    	userConnectionRequestsTo = new ArrayList<User>();
    	userConnectionRequestsFrom = new ArrayList<User>();
    }
    
    //TODO: revisit
    @Override
	public GenericModel delete() {
    	Query query = JPA.em().createQuery("SELECT uc FROM UserConnection uc WHERE user1.id = :user or user2.id = :user");
		query.setParameter("user", this.id);
		List<UserConnection> connections = query.getResultList();
		for (UserConnection userConnection : connections) {
			userConnection.delete();
		}
		
		
		// Remove userConnectionRequest links to this user
		for (User user : userConnectionRequestsFrom) {
			user.userConnectionRequestsTo.remove(this);
			user.save();
		}
		
		return super.delete();
	}
    
}
