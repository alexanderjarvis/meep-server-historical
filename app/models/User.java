package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Transient;

import play.data.validation.Email;
import play.data.validation.Match;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@Entity
public class User extends Item {
	
	@Email
	@Required
	public String email;
	
	public String passwordHash;
	
	public String accessToken;
	
	@Required
    public String firstName;
	
	@Required
    public String lastName;
	
	public String serviceName;
	
	public String telephone;
    
    @OneToMany(mappedBy="owner", cascade={CascadeType.ALL}, orphanRemoval=true)
    public List<Meeting> meetingsCreated;
    
    @OneToMany(mappedBy="user", cascade={CascadeType.ALL}, orphanRemoval=true)
    public List<Attendee> meetingsRelated;
    
    @OneToMany(mappedBy="user", cascade={CascadeType.REMOVE}, orphanRemoval=true)
    public List<UserConnection> connections;
    
    public User() {
    	connections = new ArrayList<UserConnection>();
    }
    
    public static User connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }
    
    //TODO: revisit
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
