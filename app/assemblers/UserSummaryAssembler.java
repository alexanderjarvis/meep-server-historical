package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.User;
import models.helpers.UserConnectionHelper;
import DTO.UserSummaryDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserSummaryAssembler {
	
	/**
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
	 * 
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

}
