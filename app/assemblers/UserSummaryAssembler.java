package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.User;
import models.helpers.UserConnectionHelper;
import DTO.UserSummaryDTO;

/**
 * Assembler for the UserSummaryDTO class and related classes.
 * 
 * @see UserSummaryDTO
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserSummaryAssembler {
	
	/**
	 * Writes a list of UserSummaryDTO objects based on the connections of the specified User.
	 * 
	 * @param user
	 * @return
	 */
	public static List<UserSummaryDTO> writeDTOs(User user) {
		
		List<UserSummaryDTO> userSummaries = new ArrayList<UserSummaryDTO>();
		
		List<User> connections = UserConnectionHelper.userConnectionsAsUsers(user);
		for (User connection : connections) {
			userSummaries.add(writeDTO(connection));
		}
		
		return userSummaries;
	}
	
	/**
	 * Writes a UserSummaryDTO from the attributes of the User specified.
	 * @param user
	 * @return
	 */
	public static UserSummaryDTO writeDTO(User user) {
		
		UserSummaryDTO userSummaryDTO = new UserSummaryDTO();
		
		userSummaryDTO.id = user.id;
		userSummaryDTO.email = user.email;
		userSummaryDTO.firstName = user.firstName;
		userSummaryDTO.lastName = user.lastName;
		userSummaryDTO.mobileNumber = user.mobileNumber;
		
		return userSummaryDTO;
	}
	
	/**
	 * Writes a UserSummaryDTO that only contains elements which are globally
	 * viewable e.g. the users do not have to be connected.
	 * 
	 * @param user
	 * @return
	 */
	public static UserSummaryDTO writeRestrictedDTO(User user) {
		
		UserSummaryDTO userSummaryDTO = new UserSummaryDTO();
		
		userSummaryDTO.id = user.id;
		userSummaryDTO.firstName = user.firstName;
		userSummaryDTO.lastName = user.lastName;
		
		return userSummaryDTO;
	}

}
