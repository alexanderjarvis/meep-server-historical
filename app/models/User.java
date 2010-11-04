package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends Model {
	
	public String email;
    public String password;
    public String firstName;
    public String lastName;
    
    @OneToMany
    public List<UserConnection> connections;
    
    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public static User connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }
    
}
