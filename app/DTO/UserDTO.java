package DTO;

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

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserDTO {
	
	public Long id;
	
	@Email
	@Required
	public String email;
	
	@Required
	public String password;
	
	
	public String accessToken;
	
	@Required
    public String firstName;
	
	@Required
    public String lastName;
	
	public String serviceName;
	
	public String telephone;
    
    public List<UserSummaryDTO> connections;
    
    public UserDTO() {
    }
    
}
