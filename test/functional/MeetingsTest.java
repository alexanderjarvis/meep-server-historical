package functional;

import models.User;
import models.helpers.UserConnectionHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.test.Fixtures;
import play.test.FunctionalTest;

public class MeetingsTest extends FunctionalTest {
	
	private Http.Request request;
	private Http.Response response;

	private static final String BASE_CONTROLLER_PATH = "/meetings";
	private String user1BaseQuery = "?oauth_token=";
	
	User user1;
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
		Cache.clear();
		
		request = new Http.Request();
		request.headers.put("accept", new Http.Header("Accept", "application/json"));
		
		user1 = User.find("byEmail", "bob@gmail.com").first();
		user1BaseQuery += user1.accessToken;
	}
	
	@After
	public void log() {
		if (response != null) {
			Logger.debug("Response Status: " + response.status.toString());
			Logger.debug("Response: \n" + response.out.toString());
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
		String body = "{\"title\":\"Meeting title\",\"description\":\"Meeting description\"}";
		
		response = POST(BASE_CONTROLLER_PATH + user1BaseQuery, "application/json; charset=UTF-8", body);
		
		assertStatus(201, response);
	}

}
