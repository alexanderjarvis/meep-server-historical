package functional;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import models.User;
import models.helpers.UserConnectionHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UsersTest extends FunctionalTest {
	
	private Http.Request request;
	private Http.Response response;

	private static final String BASE_CONTROLLER_PATH = "/users";
	private static String BASE_QUERY = "?oauth_token=";
	
	User user1;
	User user2;
	
	@Before
	public void setUp() {
		request = new Http.Request();
		request.headers.put("accept", new Http.Header("Accept", "application/json"));
		
		Fixtures.load("data.yml");
		
		user1 = User.find("byEmail", "bob@gmail.com").first();
		BASE_QUERY += user1.accessToken + "&";
		
		user2 = User.find("byEmail", "bob2@gmail.com").first();
		UserConnectionHelper.createUserConnection(user1, user2);
	}
	
	@After
	public void tearDown() {
		Fixtures.deleteAll();
		Cache.clear();
	}
	
	@After
	public void log() {
		if (response != null) {
			Logger.debug("Response: \n\n" + response.out.toString());
		}
	}

	@Test
	public void testIndexPage() {
		response = GET(BASE_CONTROLLER_PATH + BASE_QUERY);
		assertIsOk(response);
		assertContentType("application/json", response);
	}

//	@Test
//	public void testCreateGET() {
//		response = GET(BASE_CONTROLLER_PATH + BASE_QUERY
//				+ "user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
//		assertIsOk(response);
//		assertContentType("application/json", response);
//		assertCharset("utf-8", response);
//	}
//
//	@Test
//	public void testCreatePOST() {
//		response = POST(BASE_CONTROLLER_PATH + BASE_QUERY
//				+ "user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
//		assertStatus(302, response);
//	}
	
	@Test
	public void testShowAuthUser() {
		response = GET(BASE_CONTROLLER_PATH + "/" + user1.id + BASE_QUERY);
		assertIsOk(response);
		assertContentType("application/json", response);
		
		// Contains the connections to other users
		assertTrue(response.out.toString().contains("connections"));
	}
	
	@Test
	public void testShowUser() {
		response = GET(BASE_CONTROLLER_PATH + "/" + user2.id + BASE_QUERY);
		assertIsOk(response);
		assertContentType("application/json", response);
		
		// Does not contain the connections to other users
		assertFalse(response.out.toString().contains("connections"));
	}
	
	@Test
	public void testShowNonUser() {
		response = GET(request, BASE_CONTROLLER_PATH + "/" + "9999999999" + BASE_QUERY);
		assertIsNotFound(response);
		assertContentType("application/json", response);
	}
}
