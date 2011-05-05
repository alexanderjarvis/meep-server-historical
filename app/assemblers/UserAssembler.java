package assemblers;

import models.User;
import oauth2.AccessTokenGenerator;
import oauth2.Security;

import org.apache.commons.lang.StringUtils;

import utils.GsonFactory;
import DTO.UserDTO;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Assembler for the UserDTO and related classes.
 * 
 * @see UserDTO
 * @see User
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserAssembler {
	
	/**
	 * Writes a UserDTO using a User object.
	 * 
	 * Note - returns the UserDTO as if it is the unauthorised user.
	 * To return the UserDTO of the authorised user you have to use the
	 * accompanying method.
	 * @param user
	 * @return
	 */
	public static UserDTO writeDTO(User user) {
		return writeDTO(user, false);
	}
	
	/**
	 * Writes a UserDTO for the specified User and if the authorisedUser
	 * flag is set then additional attributes are set: 
	 * <ul>
	 * <li>accessToken,</li>
	 * <li>connections,</li>
	 * <li>connectionRequestsTo,</li>
	 * <li>connectionRequestsFrom,</li>
	 * <li>meetingsRelated</li>
	 * </ul>
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
			userDTO.connectionRequestsTo = UserRequestSummaryAssembler.writeToDTOs(user);
			userDTO.connectionRequestsFrom = UserRequestSummaryAssembler.writeFromDTOs(user);
			userDTO.meetingsRelated = MeetingAssembler.writeDTOs(user);
		}
		
		return userDTO;
	}
	
	/**
	 * Creates a new User object with the attributes provided by a UserDTO.
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
		user.create();
		
		return writeDTO(user, true);
	}
	
	/**
	 * Updates the User with the attributes provided by the UserDTO.
	 * 
	 * @param userDTO
	 * @return
	 */
	public static UserDTO updateUser(UserDTO userDTO) {
		
		User user = User.findById(userDTO.id);
		
		if (StringUtils.isNotBlank(userDTO.email)) {
			user.email = userDTO.email;
		}
		if (StringUtils.isNotBlank(userDTO.firstName)) {
			user.firstName = userDTO.firstName;
		}
		if (StringUtils.isNotBlank(userDTO.lastName)) {
			user.lastName = userDTO.lastName;
		}
		if (StringUtils.isNotBlank(userDTO.mobileNumber)) {
			user.mobileNumber = userDTO.mobileNumber;
		}
		if (StringUtils.isNotBlank(userDTO.password)) {
			user.passwordHash = Security.sha256hexWithSalt(userDTO.password);
		}
		
		user.save();
		
		return writeDTO(user, true);
	}
	
	/**
	 * Creates a UserDTO from a JsonObject. Used when creating the UserDTO object 
	 * from the body of a JSON request.
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static UserDTO userDTOWithJsonObject(JsonObject jsonObject) {
		GsonBuilder gsonBuilder = GsonFactory.gsonBuilder();
		UserDTO userDTO = gsonBuilder.create().fromJson(jsonObject, UserDTO.class);
		return userDTO;
	}

}
