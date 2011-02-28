package functional;

import models.User;
import models.helpers.UserConnectionHelper;
import oauth2.functional.AccessTokenTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
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
	private String user1BaseQuery = "?oauth_token=";
	private String user2BaseQuery = "?oauth_token=";
	
	User user1;
	User user2;
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
		Cache.clear();
		
		request = new Http.Request();
		request.headers.put("accept", new Http.Header("Accept", "application/json"));
		
		user1 = User.find("byEmail", "bob@gmail.com").first();
		user1BaseQuery += user1.accessToken + "&";
		
		user2 = User.find("byEmail", "bob2@gmail.com").first();
		user2BaseQuery += user2.accessToken + "&";
		UserConnectionHelper.createUserConnection(user1, user2);
	}
	
	@After
	public void log() {
		if (response != null) {
			Logger.debug("Response Status: " + response.status.toString());
			Logger.debug("Response: " + (response.out.toString().isEmpty() ? "" : "\n" + response.out.toString()) );
		}
	}

	@Test
	public void testIndexPage() {
		response = GET(BASE_CONTROLLER_PATH + user1BaseQuery);
		assertIsOk(response);
		assertContentType("application/json", response);
	}

	@Test
	public void testCreate() {	
		String body = "{\"email\":\"axj7@aber.ac.uk\","
			+ "\"firstName\":\"Alex\","
			+ "\"lastName\":\"Jarvis\","
			+ "\"password\":\"password\","
			+ "\"mobileNumber\":\"123\"}";
		
		Http.Request request = newRequest();
		request.params.put("body", body);

		response = POST(request, BASE_CONTROLLER_PATH, "application/json; charset=UTF-8", body);
		
		assertStatus(201, response);
	}
	
	@Test
	public void testCreateErrorExistingEmail() {
		testCreate();
		
		String body = "{\"email\":\"axj7@aber.ac.uk\","
			+ "\"firstName\":\"Alex\","
			+ "\"lastName\":\"Jarvis\","
			+ "\"password\":\"password\","
			+ "\"mobileNumber\":\"123\"}";
		
		Http.Request request = newRequest();
		request.params.put("body", body);

		response = POST(request, BASE_CONTROLLER_PATH, "application/json; charset=UTF-8", body);
		
		assertStatus(400, response);
		assertContentEquals("Email already exists", response);
	}
	
	@Test
	public void testCreateWithEmailValidationError() {
		testCreate();
		
		String body = "{\"email\":\"axj7\","
			+ "\"firstName\":\"Alex\","
			+ "\"lastName\":\"Jarvis\","
			+ "\"password\":\"password\","
			+ "\"mobileNumber\":\"123\"}";
		
		Http.Request request = newRequest();
		request.params.put("body", body);

		response = POST(request, BASE_CONTROLLER_PATH, "application/json; charset=UTF-8", body);
		
		assertStatus(400, response);
		//assertContentEquals("Email already exists", response);
	}
	
	@Test
	public void testShowAuthUser() {
		response = GET(BASE_CONTROLLER_PATH + "/" + user1.id + user1BaseQuery);
		assertIsOk(response);
		assertContentType("application/json", response);
		
		// Contains the connections to other users
		assertTrue(response.out.toString().contains("connections"));
	}
	
	@Test
	public void testShowUser() {
		response = GET(BASE_CONTROLLER_PATH + "/" + user2.id + user1BaseQuery);
		assertIsOk(response);
		assertContentType("application/json", response);
		
		// Does not contain the connections to other users
		assertFalse(response.out.toString().contains("connections"));
	}
	
	@Test
	public void testShowNonUser() {
		response = GET(request, BASE_CONTROLLER_PATH + "/" + "9999999999" + user1BaseQuery);
		assertIsNotFound(response);
		assertContentType("application/json", response);
	}
	
	@Test
	public void testShowAuthUserWithEmail() {
		response = GET(BASE_CONTROLLER_PATH + "/" + user1.email + user1BaseQuery);
		assertIsOk(response);
		assertContentType("application/json", response);
		
		// Contains the connections to other users
		assertTrue(response.out.toString().contains("connections"));
	}
	
	@Test
	public void testShowUserWithEmail() {
		response = GET(BASE_CONTROLLER_PATH + "/" + user2.email + user1BaseQuery);
		assertIsOk(response);
		assertContentType("application/json", response);
		
		// Does not contain the connections to other users
		assertFalse(response.out.toString().contains("connections"));
	}
	
	@Test
	public void testUpdateAuthUser() {
		String newPassword = "newpassword";
		String data = "user.email="
						+ user1.email
						+ "&user.password=" + newPassword
						+ "&user.firstName=alex";
		
		response = PUT(request, BASE_CONTROLLER_PATH + "/" + user1.id + user1BaseQuery + data, "application/x-www-form-urlencoded", "");

		assertStatus(200, response);
		assertTrue("FirstName has not been updated", response.out.toString().contains("\"firstName\":\"alex\""));
		
		// Verify that the old password can no longer be used to authenticate the user
		AccessTokenTest accessTokenTest = new AccessTokenTest();
		accessTokenTest.requestAccessToken();
		assertStatus(400, accessTokenTest.response);
	}
	
	@Test
	public void testUpdateNonAuthUser() {
		String data = "user.email=" + user1.email
						+ "&user.password=password"
						+ "&user.firstName=alex"
						+ "&user.lastName=hello"
						+ "&user.serviceName=alex"
						+ "&user.telephone=123";
		
		response = PUT(request, BASE_CONTROLLER_PATH + "/" + user2.id + user1BaseQuery + data, "application/x-www-form-urlencoded", "");
		
		assertStatus(400, response);
	}
	
	@Test
	public void testUpdateClashes() {
		String data = "user.email=" + user2.email;
		response = PUT(request, BASE_CONTROLLER_PATH + "/" + user1.id + user1BaseQuery + data, "application/x-www-form-urlencoded", "");
		
		assertStatus(400, response);
		assertContentEquals("Email already exists", response);
	}
	
	@Test
	public void testAddUserRequest() {
		UserConnectionHelper.removeUserConnection(user1, user2);
		response = POST(BASE_CONTROLLER_PATH + "/" + user2.email + "/add/" + user1BaseQuery);
		assertIsOk(response);
	}
	
	@Test
	public void testAddUserRequestWithAuthUser() {
		response = POST(BASE_CONTROLLER_PATH + "/" + user1.email + "/add/" + user1BaseQuery);
		assertStatus(400, response);
	}
	
	@Test
	public void testAcceptUserRequest() {
		// Remove the existing user connection and add the request
		UserConnectionHelper.removeUserConnection(user1, user2);
		response = POST(BASE_CONTROLLER_PATH + "/" + user2.email + "/add/" + user1BaseQuery);
		assertIsOk(response);
		
		// Connect with 2nd user and accept the user connection request
		response = POST(BASE_CONTROLLER_PATH + "/" + user1.email + "/accept/" + user2BaseQuery);
		assertIsOk(response);
		
		// Try to accept the user connection request again
		response = POST(BASE_CONTROLLER_PATH + "/" + user1.email + "/accept/" + user2BaseQuery);
		assertStatus(400, response);
	}
	
	@Test
	public void testAcceptUserRequestNotExist() {
		response = POST(BASE_CONTROLLER_PATH + "/" + user1.email + "/accept/" + user2BaseQuery);
		assertStatus(400, response);
	}
	
	@Test
	public void testDeclineUserRequest() {
		// Remove the existing user connection and add the request
		UserConnectionHelper.removeUserConnection(user1, user2);
		response = POST(BASE_CONTROLLER_PATH + "/" + user2.email + "/add/" + user1BaseQuery);
		assertIsOk(response);
		
		// Connect with 2nd user and decline the user connection request
		response = POST(BASE_CONTROLLER_PATH + "/" + user1.email + "/decline/" + user2BaseQuery);
		assertIsOk(response);
		
		// Try to decline the user connection request again
		response = POST(BASE_CONTROLLER_PATH + "/" + user1.email + "/decline/" + user2BaseQuery);
		assertStatus(400, response);
	}
	
	@Test
	public void testDeclineUserRequestNotExist() {
		response = POST(BASE_CONTROLLER_PATH + "/" + user1.email + "/decline/" + user2BaseQuery);
		assertStatus(400, response);
	}
	
	@Test
	public void testSearchUserByFirstName() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "Bob" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByFirstNameLowerCase() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "bob" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByFirstNameUpperCase() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "BOB" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByLastName() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "Smith" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByLastNameLowerCase() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "smith" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByLastNameUpperCase() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "SMITH" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByActualName() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "Bob Smith" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByActualNameLowerCase() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "bob smith" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserByActualNameUpperCase() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "BOB SMITH" + user1BaseQuery);
		assertIsOk(response);
		assertContentMatch("(.)*firstName\":\"Bob\",\"lastName\":\"Smith\"(.)*", response);
	}
	
	@Test
	public void testSearchUserNotExist() {
		response = GET("/search" + BASE_CONTROLLER_PATH + "/" + "William" + user1BaseQuery);
		assertStatus(404, response);
	}
}
