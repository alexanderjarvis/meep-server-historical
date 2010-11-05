package controllers;

import java.util.HashMap;
import java.util.Map;

import factories.UserConnectionFactory;

import models.User;
import models.UserConnection;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Router;

public class UserConnections extends Application {

    public static void index() {
        renderJSON(UserConnection.findAll());
    }
    
    /**
	 * Create
	 * @param user1
	 * @param user2
	 */
    public static void create(Long user1id, Long user2id) {
    	User user1 = User.findById(user1id);
    	User user2 = User.findById(user2id);
    	UserConnection userConnection = UserConnectionFactory.createUserConnection(user1, user2);

    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("id", userConnection.id);
    	String url = Router.reverse("UserConnections.show", map).url;
    	Logger.debug("Entity Created - Redirect URL: %s", url);
    	redirect(url);
    }
    
    /**
     * Read
     * @param id
     */
    public static void show(Long id) {
    	UserConnection userConnection = UserConnection.findById(id);
    	if (userConnection == null) {
    		error(404, "Entity does not exist.");
    	}
    	renderJSON(userConnection);
    }
    
    /**
     * Delete
     * @param id
     */
    public static void delete(Long id) {
    	UserConnection entity = UserConnection.findById(id);
    	entity.delete();
    	index();
    }

}
