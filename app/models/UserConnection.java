package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class UserConnection extends Model {
	
	@Required
	public Long user1;
	@Required
	public Long user2;
	
	public UserConnection(Long user1, Long user2) {
		
	}
    
}
