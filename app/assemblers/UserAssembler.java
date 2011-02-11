package assemblers;

import models.User;
import oauth2.AccessTokenGenerator;
import oauth2.Security;
import DTO.UserDTO;
import assemblers.helpers.ModelMerger;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserAssembler {
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public static UserDTO writeDTO(User user) {
		return writeDTO(user, false);
	}
	
	/**
	 * 
	 * @param user
	 * @param authorisedUser
	 * @return
	 */
	public static UserDTO writeDTO(User user, boolean authorisedUser) {
		
		UserDTO userDTO = new UserDTO();
		
		userDTO.id = user.id;
		userDTO.email = user.email;
		userDTO.firstName = user.firstName;
		userDTO.lastName = user.lastName;
		userDTO.mobileNumber = user.mobileNumber;
		if (authorisedUser) {
			userDTO.accessToken = user.accessToken;
			userDTO.connections = UserSummaryAssembler.writeDTOs(user);
		}
		
		return userDTO;
	}
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public static UserDTO createUser(UserDTO userDTO) {
		
		User user = new User();
		
		user.email = userDTO.email;
		user.firstName = userDTO.firstName;
		user.lastName = userDTO.lastName;
		user.mobileNumber = userDTO.mobileNumber;
		
		user.passwordHash = Security.sha256hexWithSalt(userDTO.password);
		user.accessToken = AccessTokenGenerator.generate();
		
		user.save();
		
		return writeDTO(user, true);
	}
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public static UserDTO updateUser(UserDTO userDTO) {
		
		User user = User.findById(userDTO.id);
		
		ModelMerger.merge(userDTO, user);
		
		if (userDTO.password != null) {
			user.passwordHash = Security.sha256hexWithSalt(userDTO.password);
		}
		
		user.save();
		
		return writeDTO(user, true);
	}

}
