package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class UserDetail extends Model {
	
	public String passwordHash;
    
}
