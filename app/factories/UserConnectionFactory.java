package factories;

import models.User;
import models.UserConnection;

public class UserConnectionFactory {
	
	public static UserConnection createUserConnection(User user1, User user2) {
		UserConnection userConnection = new UserConnection(user1, user2);
		userConnection.save();
		user1.connections.add(user2.id);
		user2.connections.add(user1.id);
		user1.save();
		user2.save();
		return userConnection;
	}

}
