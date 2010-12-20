package assemblers;

import models.User;
import DTO.UserDTO;

public class UserAssembler {
	
	public UserDTO writeDTO(Long id) {
		User.findById(id);
		//TODO:
		UserDTO userDTO = new UserDTO();
		return userDTO;
	}

}
