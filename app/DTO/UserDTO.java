package DTO;

import java.io.Serializable;
import java.util.List;

import play.data.validation.Email;
import play.data.validation.Required;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserDTO implements Serializable {
	
	public Long id;
	
	public String accessToken;
	
	@Email
	@Required
	public String email;
	
	public String password;
	
	@Required
    public String firstName;
	
	@Required
    public String lastName;
	
	public String mobileNumber;
    
    public List<UserSummaryDTO> connections;
    
    public List<UserRequestSummaryDTO> connectionRequestsTo;
    
    public List<UserRequestSummaryDTO> connectionRequestsFrom;
    
    public List<MeetingDTO> meetingsRelated;
    
}
