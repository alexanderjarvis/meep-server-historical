package functional;
import java.util.Map;

import org.junit.Test;

import play.Logger;
import play.mvc.Http.Header;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

public class UsersTest extends FunctionalTest {

	private static String CONTROLLER_PATH = "/Users";

	@Test
	public void testIndexPage() {
		Response response = GET(CONTROLLER_PATH);
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}

	@Test
	public void testCreateGET() {
		Response response = GET(CONTROLLER_PATH
				+ "?user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}

	@Test
	public void testCreatePOST() {
		Response response = POST(CONTROLLER_PATH
				+ "?user.email=alex@alex.com&user.password=ASDFGH&user.firstName=alex&user.lastName=hello");
		assertStatus(302, response);
	}
	
	@Test
	public void testShow() {
		//TODO: fix this test
		Response response = GET(CONTROLLER_PATH + "/3");
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
	}
}
