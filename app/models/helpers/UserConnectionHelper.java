package models.helpers;

import java.util.ArrayList;
import java.util.List;

import models.User;

/**
 * Helps with creating connections and connection requests between Users (both
 * are many-to-many relationships).
 * 
 * Connection requests are unidirectional as one user has to accept the request
 * and this rule is enforced.
 * 
 * Connections are unidirectional in the database, but the owning and not owning
 * relationships (to/from) can be combined using the userConnectionsAsUsers()
 * method for a specific User - giving the illusion of a bidirectional
 * relationship.
 * 
 * @see User
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserConnectionHelper {

	/**
	 * Creates a connection request relationship from between two users.
	 * 
	 * @param user1
	 *            The User that creates the connection request.
	 * @param user2
	 *            The User that receives the connection request.
	 * @return The result of creating the connection request.
	 */
	public static boolean createUserConnectionRequest(User user1, User user2) {

		// If the users are already connected, return false
		if (isUsersConnected(user1, user2)) {
			return false;
		}

		// If there is already an existing user connection request, return false
		if (user1.userConnectionRequestsTo.contains(user2)
				&& user2.userConnectionRequestsFrom.contains(user1)) {
			return false;
		}

		// If there is already an existing user connection request (in the other
		// direction), return false
		if (user2.userConnectionRequestsTo.contains(user1)
				&& user1.userConnectionRequestsFrom.contains(user2)) {
			return false;
		}

		// Create the user connection request
		user1.userConnectionRequestsTo.add(user2);
		user1.save();
		return true;
	}

	/**
	 * Removes a connection request between Users, this is typically done after
	 * creating a connection (not just request) between Users.
	 * 
	 * @param user1
	 *            The User that created the connection request.
	 * @param user2
	 *            The User that received the connection request.
	 * @return The result of removing the connection request.
	 */
	public static boolean removeUserConnectionRequest(User user1, User user2) {

		if (user1.userConnectionRequestsTo.contains(user2)
				&& user2.userConnectionRequestsFrom.contains(user1)) {
			user1.userConnectionRequestsTo.remove(user2);
			user2.userConnectionRequestsFrom.remove(user1);
			user1.save();
			user2.save();
			return true;
		}
		return false;
	}

	/**
	 * Creates a connection between two Users. Checks if the users are already
	 * connected or not before proceeding.
	 * 
	 * @param user1
	 *            The User creating the connection.
	 * @param user2
	 *            The User on the receiving end of the connection.
	 * @return The result of creating the connection
	 */
	public static boolean createUserConnection(User user1, User user2) {
		if (isUsersConnected(user1, user2)) {
			return false;
		}
		user1.userConnectionsTo.add(user2);
		user1.save();
		return true;
	}

	/**
	 * Removes a connection between two Users if it exists.
	 * 
	 * Works regardless of which User owns the connection.
	 * 
	 * @param user1
	 * @param user2
	 * @return The result of removing the User connection
	 */
	public static boolean removeUserConnection(User user1, User user2) {

		if (user1.userConnectionsTo.contains(user2)
				&& user2.userConnectionsFrom.contains(user1)) {
			user1.userConnectionsTo.remove(user2);
			user1.save();
			return true;
		} else if (user2.userConnectionsTo.contains(user1)
				&& user1.userConnectionsFrom.contains(user2)) {
			user2.userConnectionsTo.remove(user1);
			user2.save();
			return true;
		}
		return false;
	}

	/**
	 * Retrieves all the Users that the specified User is connected to.
	 * @param user
	 * @return
	 */
	public static List<User> userConnectionsAsUsers(User user) {
		List<User> connectedUsers = new ArrayList<User>();
		connectedUsers.addAll(user.userConnectionsTo);
		connectedUsers.addAll(user.userConnectionsFrom);
		return connectedUsers;
	}

	/**
	 * Returns true if the Users are connection and false otherwise.
	 * @param user1
	 * @param user2
	 * @return
	 */
	public static boolean isUsersConnected(User user1, User user2) {
		return userConnectionsAsUsers(user1).contains(user2);
	}

}
