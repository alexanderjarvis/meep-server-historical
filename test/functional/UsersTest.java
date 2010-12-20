package functional;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.mvc.Http;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

public class UsersTest extends FunctionalTest {
	
	private Http.Response response;

	private static final String BASE_CONTROLLER_PATH = "/users";
	private static String BASE_QUERY = "?oauth_token=";	
	
	@Before
	public void setUp() {
		Fixtures.load("data.yml");
		User user = User.find("byEmail", "bob@gmail.com").first();
		BASE_QUERY += user.accessToken + "&";
	}
	
	@After
	public void tearDown() {
		Fixtures.deleteAll();
	}
	
	@After
	public void log() {
		if (response != null) {
			Logger.debug("Response: \n\n" + response.out.toString());
			//response = null;
		}
	}

	@Test
	public void testIndexPage() {
		response = GET(BASE_CONTROLLER_PATH + BASE_QUERY);
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
		Logger.debug("Response Status: ", response.status);
	}

	@Test
	public void testCreateGET() {
		response = GET(BASE_CONTROLLER_PATH + BASE_QUERY
				+ "user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}

	@Test
	public void testCreatePOST() {
		response = POST(BASE_CONTROLLER_PATH + BASE_QUERY
				+ "user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
		assertStatus(302, response);
	}
	
	@Test
	public void testShow() {
		//TODO: fix this test
		User user = (User)User.findAll().get(0);
		response = GET(BASE_CONTROLLER_PATH + "/" + user.id + BASE_QUERY);
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}
}
