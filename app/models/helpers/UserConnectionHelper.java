package models.helpers;

import models.User;
import models.UserConnection;

public class UserConnectionHelper {
	
	public static void createUserConnection(User user1, User user2) {
		UserConnection con1 = new UserConnection();
		UserConnection con2 = new UserConnection();
		con1.user = user1;
		con2.user = user2;
		con1.userConnection = con2;
		con2.userConnection = con1;
		user1.connections.add(con1);
		user2.connections.add(con2);
	}

}
