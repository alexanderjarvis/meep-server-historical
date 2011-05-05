package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.User;
import models.helpers.UserConnectionHelper;
import DTO.UserRequestSummaryDTO;
import DTO.UserSummaryDTO;

/**
 * Assembler for the UserRequestSummaryDTO.
 * 
 * @see UserRequestSummaryDTO
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserRequestSummaryAssembler {
	
	/**
	 * Writes a list of UserRequestSummaryDTO objects based on the users in the
	 * userConnectionRequestsTo relationship of the specified User.
	 * 
	 * @param user
	 * @return
	 */
	public static List<UserRequestSummaryDTO> writeToDTOs(User user) {
		
		List<UserRequestSummaryDTO> userRequestSummaries = new ArrayList<UserRequestSummaryDTO>();
		
		List<User> connections = user.userConnectionRequestsTo;
		for (User connection : connections) {
			userRequestSummaries.add(writeDTO(connection));
		}
		
		return userRequestSummaries;
	}
	
	/**
	 * Writes a list of UserRequestSummaryDTO objects based on the users in the
	 * usersConnectionRequests from relationship of the specified User.
	 * 
	 * @param user
	 * @return
	 */
	public static List<UserRequestSummaryDTO> writeFromDTOs(User user) {
		
		List<UserRequestSummaryDTO> userRequestSummaries = new ArrayList<UserRequestSummaryDTO>();
		
		List<User> connections = user.userConnectionRequestsFrom;
		for (User connection : connections) {
			userRequestSummaries.add(writeDTO(connection));
		}
		
		return userRequestSummaries;
	}
	
	/**
	 * Writes a UserRequestSummaryDTO from attributes of the specified User.
	 * 
	 * @param user
	 * @return
	 */
	public static UserRequestSummaryDTO writeDTO(User user) {
		
		UserRequestSummaryDTO userRequestSummaryDTO = new UserRequestSummaryDTO();
		
		userRequestSummaryDTO.id = user.id;
		userRequestSummaryDTO.firstName = user.firstName;
		userRequestSummaryDTO.lastName = user.lastName;
		
		return userRequestSummaryDTO;
	}

}
