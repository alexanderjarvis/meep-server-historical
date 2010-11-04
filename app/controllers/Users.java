package controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.mvc.*;
import models.*;

/**
 * 
 * @author alex
 */
public class Users extends Application {
	
	/**
	 * Returns all users
	 * TODO: this should only be available to admins.
	 */
	public static void index() {
		renderJSON(User.findAll());
	}
    
	/**
	 * Create
	 * @param email
	 * @param password
	 */
    public static void create(String email, String password) {
    	User user = new User(email, password, "", "");
    	user.save();
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("id", user.id);
    	String url = Router.reverse("Users.show", map).url;
    	Logger.debug("Entity Created - Redirect URL: %s", url);
    	redirect(url);
    }
    
    /**
     * Read
     * @param id
     */
    public static void show(Long id) {
    	User user = User.findById(id);
    	if (user == null) {
    		error(404, "Entity does not exist.");
    	}
    	renderJSON(user);
    }
    
    /**
     * Update
     * @param user
     */
    public static void update(User user) {
    	if (validation.hasErrors()) {
			render("@create", user);
		}
    	// Save before merging in case connections gets updated.
    	for (UserConnection connection : user.connections) {
    		connection.save();
    	}
    	user = user.merge();
    	user.save();
		index();
    }
    
    /**
     * Delete
     * @param id
     */
    public static void delete(Long id) {
    	User entity = User.findById(id);
    	entity.delete();
    	index();
    }

}