package DTO;

import java.util.List;

/**
 * Used to represent the recent locations of a specific User using a List of UserLocationDTO.
 * 
 * @see UserLocationDTO
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class RecentUserLocationsDTO {
	
	/**
	 * The primary key of the User that this object represents.
	 */
	public Long id;
	
	/**
	 * The first name of the User that this object represents.
	 */
	public String firstName;
	
	/**
	 * The last name of the User that this object represents.
	 */
	public String lastName;
	
	/**
	 * A List of UserLocationDTO to represent the recent locations of a User.
	 */
	public List<UserLocationDTO> locationHistory;
	
}
