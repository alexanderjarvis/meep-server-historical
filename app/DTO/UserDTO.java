package DTO;

import java.util.List;

import play.data.validation.Email;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserDTO {
	
	public Long id;
	
	@Email
	public String email;
	
	public String password;
	
	public String accessToken;
	
    public String firstName;
	
    public String lastName;
	
	public String serviceName;
	
	public String telephone;
    
    public List<UserSummaryDTO> connections;
    
    public UserDTO() {
    }
    
}
