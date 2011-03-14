package assemblers;

import java.util.List;

import models.User;
import DTO.RecentUserLocationsDTO;
import DTO.UserLocationDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class RecentUserLocationsAssembler {
	
	/**
	 * 
	 * @param user
	 * @param locationHistory
	 * @return
	 */
	public static RecentUserLocationsDTO writeDTO(User user, List<UserLocationDTO> locationHistory) {
		
		RecentUserLocationsDTO recentUserLocation = new RecentUserLocationsDTO();
		recentUserLocation.id = user.id;
		recentUserLocation.firstName = user.firstName;
		recentUserLocation.lastName = user.lastName;
		recentUserLocation.locationHistory = locationHistory;
		
		return recentUserLocation;
		
	}

}
