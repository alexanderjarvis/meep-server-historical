package functional;
import java.util.Map;

import models.User;

import org.junit.Test;

import play.Logger;
import play.mvc.Http.Header;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

public class UsersTest extends FunctionalTest {

	private static final String BASE_CONTROLLER_PATH = "/Users";
	private static final String BASE_QUERY = "?oauth_token=&";

	@Test
	public void testIndexPage() {
		Response response = GET(BASE_CONTROLLER_PATH + BASE_QUERY);
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}

	@Test
	public void testCreateGET() {
		Response response = GET(BASE_CONTROLLER_PATH + BASE_QUERY
				+ "user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}

	@Test
	public void testCreatePOST() {
		Response response = POST(BASE_CONTROLLER_PATH + BASE_QUERY
				+ "user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
		assertStatus(302, response);
	}
	
	@Test
	public void testShow() {
		//TODO: fix this test
		User user = (User)User.findAll().get(0);
		Response response = GET(BASE_CONTROLLER_PATH + "/" + user.id + BASE_QUERY);
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}
}
