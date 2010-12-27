package assemblers;

import java.lang.reflect.Field;

import assemblers.helpers.ModelMerger;

import oauth2.AccessTokenGenerator;
import oauth2.Security;
import play.Logger;
import sun.reflect.Reflection;
import models.User;
import DTO.UserDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserAssembler {
	
	public static UserDTO writeDTO(User user) {
		return writeDTO(user, false);
	}
	
	public static UserDTO writeDTO(User user, boolean authorisedUser) {
		
		UserDTO userDTO = new UserDTO();
		
		userDTO.id = user.id;
		userDTO.email = user.email;
		userDTO.firstName = user.firstName;
		userDTO.lastName = user.lastName;
		userDTO.serviceName = user.serviceName;
		userDTO.telephone = user.telephone;
		if (authorisedUser) {
			userDTO.accessToken = user.accessToken;
			userDTO.connections = UserSummaryAssembler.writeDTOs(user);
		}
		
		return userDTO;
	}
	
	public static UserDTO createUser(UserDTO userDTO) {
		
		User user = new User();
		
		user.email = userDTO.email;
		user.firstName = userDTO.firstName;
		user.lastName = userDTO.lastName;
		user.serviceName = userDTO.serviceName;
		user.telephone = userDTO.telephone;
		
		user.passwordHash = Security.sha256hexWithSalt(userDTO.password);
		user.accessToken = AccessTokenGenerator.generate();
		
		user.save();
		
		return writeDTO(user, true);
	}
	
	public static UserDTO updateUser(UserDTO userDTO) {
		
		User user = User.findById(userDTO.id);
		
		ModelMerger.merge(userDTO, user);
		
		user.passwordHash = Security.sha256hexWithSalt(userDTO.password);
		
		user.save();
		
		return writeDTO(user, true);
	}

}
