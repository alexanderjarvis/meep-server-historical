package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class User extends Item implements Comparable {
	
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
    
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name="USER_CONNECTIONS",
        joinColumns=@JoinColumn(name="USER_1"),
        inverseJoinColumns=@JoinColumn(name="USER_2")
    )
    public List<User> userConnectionsTo;
    
    @ManyToMany(mappedBy="userConnectionsTo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<User> userConnectionsFrom;
    
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name="USER_CONNECTION_REQUESTS",
        joinColumns=@JoinColumn(name="USER_REQUEST_1"),
        inverseJoinColumns=@JoinColumn(name="USER_REQUEST_2")
    )
    public List<User> userConnectionRequestsTo;
    
    @ManyToMany(mappedBy = "userConnectionRequestsTo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<User> userConnectionRequestsFrom;
    
    public User() {
    	userConnectionsTo = new ArrayList<User>();
    	userConnectionsFrom = new ArrayList<User>();
    	userConnectionRequestsTo = new ArrayList<User>();
    	userConnectionRequestsFrom = new ArrayList<User>();
    }
    
    @Override
	public GenericModel delete() {
		
		// Remove userConnectionsTo links to this user
		for (User user : userConnectionsFrom) {
			user.userConnectionsTo.remove(this);
			user.save();
		}
		
		// Remove userConnectionRequest links to this user
		for (User user : userConnectionRequestsFrom) {
			user.userConnectionRequestsTo.remove(this);
			user.save();
		}
		
		return super.delete();
	}
    
    //TODO: implement this so that users can be alphabetically sorted
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
