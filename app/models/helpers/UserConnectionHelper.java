package models.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import models.User;
import models.UserConnection;
import play.db.jpa.JPA;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserConnectionHelper {
	
	public static boolean createUserConnectionRequest(User user1, User user2) {
		
		// If the users are already connected, return false
		if (isUsersConnected(user1, user2)) {
			return false;
		}
		
		// If there is already an existing user connection request, return false
		if (user1.userConnectionRequestsTo.contains(user2) && user2.userConnectionRequestsFrom.contains(user1)) {
			return false;
		}
		
		// If there is already an existing user connection request (in the other direction), return false
		if (user2.userConnectionRequestsTo.contains(user1) && user1.userConnectionRequestsFrom.contains(user2)) {
			return false;
		}
		
		// Create the user connection request
		user1.userConnectionRequestsTo.add(user2);
		user1.save();
		return true;
	}
	
	public static boolean removeUserConnectionRequest(User user1, User user2) {
		
		if (user1.userConnectionRequestsTo.contains(user2) && user2.userConnectionRequestsFrom.contains(user1)) {
			user1.userConnectionRequestsTo.remove(user2);
			user2.userConnectionRequestsFrom.remove(user1);
			return true;
		}
		return false;
	}
	
	public static void createUserConnection(User user1, User user2) {
		UserConnection con1 = new UserConnection();
		UserConnection con2 = new UserConnection();
		con1.user = user1;
		con2.user = user2;
		// It is necessary to save the UserConnection objects to obtain id's for
		// referencing each other.
		con1.save();
		con2.save();
		con1.userConnection = con2;
		con2.userConnection = con1;
		con1.save();
		con2.save();
	}
	
	public static void removeUserConnection(User user1, User user2) {
		
		// Get the UserConnection objects that represent user1 to user2
		Query query = JPA.em().createQuery("SELECT uc FROM UserConnection uc WHERE user = :user1 AND userConnection.user = :user2 ");
		query.setParameter("user1", user1);
		query.setParameter("user2", user2);
		UserConnection con1 = (UserConnection) query.getSingleResult();
		UserConnection con2 = con1.userConnection;
		
		// Delete the UserConnection objects
		con1.delete();
		con2.delete();
	}
	
	public static List<User> connectionsAsUsers(User user) {
		List<User> connections = new ArrayList<User>();
		for (UserConnection connection : user.connections) {
			connections.add(connection.userConnection.user);
		}
		return connections;
	}
	
	public static boolean isUsersConnected(User user1, User user2) {
		for (UserConnection connection : user1.connections) {
			if (connection.userConnection.user == user2) {
				return true;
			}
		}
		return false;
	}

}
