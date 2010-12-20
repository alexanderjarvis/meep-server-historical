package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.User;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Valid;
import play.mvc.Router;
import DTO.UserSummaryDTO;
import assemblers.UserSummaryAssembler;

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
		User authUser = userAuth.getAuthroizedUser();
		renderJSON(UserSummaryAssembler.writeDTOs(authUser));
	}
    
	/**
	 * Create
	 * @param email
	 * @param password
	 */
    public static void create(User user) {
    	if (validation.hasErrors()) {
    		for (Error error : validation.errors()) {
    			Logger.debug(error.getKey() + " : " + error.message());
    		}
			error(500, "Validation Errors");
		}
    	user.save();
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("id", user.id);
    	String url = Router.reverse("Users.show", map).url;
    	Logger.debug("Entity Created - Redirect URL: %s", url);
    	redirect(url);
    	//TODO: return 201 ?
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
    public static void update(@Valid User user) {
    	if (validation.hasErrors()) {
			render("@create", user);
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