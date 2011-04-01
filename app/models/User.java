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

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

/**
 * Represents every User that uses the service and stores all information about them including
 * what other domain objects they are related to.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@Entity
public class User extends Item {
	
	/**
	 * The email address of the User.
	 */
	@Column(unique=true) 
	public String email;
	
	/**
	 * The password hash used to verify the User's password.
	 */
	public String passwordHash;
	
	/**
	 * The access token used to verify each request to the service for a User.
	 */
	public String accessToken;
	
	/**
	 * The first name of the User.
	 */
    public String firstName;
	
    /**
     * The last name of the User.
     */
    public String lastName;
	
    /**
     * The mobile number of the User.
     */
	@Column(unique=true)
	public String mobileNumber;
    
	/**
	 * The Meetings that the User has created.
	 */
    @OneToMany(mappedBy="owner", cascade={CascadeType.ALL})
    public List<Meeting> meetingsCreated = new ArrayList<Meeting>();
    
    /**
     * The Meetings that the User is related to (an Attendee).
     */
    @OneToMany(mappedBy="user", cascade={CascadeType.ALL})
    public List<Attendee> meetingsRelated = new ArrayList<Attendee>();
    
    /**
     * The Users that the User is connected to.
     */
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name="USER_CONNECTIONS",
        joinColumns=@JoinColumn(name="USER_1"),
        inverseJoinColumns=@JoinColumn(name="USER_2")
    )
    public List<User> userConnectionsTo = new ArrayList<User>();
    
    /**
     * The Users that the User is connected to, but they created the connection.
     */
    @ManyToMany(mappedBy="userConnectionsTo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<User> userConnectionsFrom = new ArrayList<User>();
    
    /**
     * The connection requests that the User has sent to other Users.
     */
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name="USER_CONNECTION_REQUESTS",
        joinColumns=@JoinColumn(name="USER_REQUEST_1"),
        inverseJoinColumns=@JoinColumn(name="USER_REQUEST_2")
    )
    public List<User> userConnectionRequestsTo = new ArrayList<User>();
    
    /**
     * The connection requests that the User has received from other Users.
     */
    @ManyToMany(mappedBy = "userConnectionRequestsTo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<User> userConnectionRequestsFrom = new ArrayList<User>();
    
    /**
     * The location history of the User.
     */
    @OneToMany(mappedBy="user")
    public List<UserLocation> locationHistory = new ArrayList<UserLocation>();
    
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
}
