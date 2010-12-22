package assemblers;

import models.User;
import DTO.UserDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserAssembler {
	
	public static UserDTO writeDTO(User user, boolean authorisedUser) {
		
		UserDTO userDTO = new UserDTO();
		
		userDTO.id = user.id;
		userDTO.email = user.email;
		userDTO.firstName = user.firstName;
		userDTO.lastName = user.lastName;
		userDTO.serviceName = user.serviceName;
		userDTO.telephone = user.telephone;
		if (authorisedUser) {
			userDTO.connections = UserSummaryAssembler.writeDTOs(user);
		}
		
		return userDTO;
	}

}
