package functional;

import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.test.Fixtures;
import play.test.FunctionalTest;

public class LocationsTest extends FunctionalTest {
	
	private Http.Request request;
	private Http.Response response;

	private static final String BASE_CONTROLLER_PATH = "/locations";
	private String userBaseQuery = "?oauth_token=";
	private String user1Query = "";
	private String user2Query = "";
	private String user3Query = "";
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
		Cache.clear();
		
		request = new Http.Request();
		request.headers.put("accept", new Http.Header("Accept", "application/json"));
		
		user1 = User.find("byEmail", "bob@gmail.com").first();
		user1Query += userBaseQuery + user1.accessToken;
		
		user2 = User.find("byEmail", "bob2@gmail.com").first();
		user2Query += userBaseQuery + user2.accessToken;
		
		user3 = User.find("byEmail", "alex@jarvis.com").first();
		user3Query += userBaseQuery + user3.accessToken;
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
		response = GET(BASE_CONTROLLER_PATH + user1Query);
		assertIsOk(response);
	}

	@Test
	public void testUpdate() {
		String body = "[{\"time\":\"2011-06-01T12:00:00Z\","
			+ "\"coordinate\":{\"latitude\":52.416117,\"longitude\":-4.083803},"
			+ "\"speed\":0,"
			+ "\"altitude\":0,"
			+ "\"trueHeading\":0,"
			+ "\"verticalAccuracy\":0,"
			+ "\"horizontalAccuracy\":0}]";
		
		Http.Request request = newRequest();
		request.params.put("body", body);
		
		response = POST(request, BASE_CONTROLLER_PATH + user1Query, "application/json; charset=UTF-8", body);
		
		assertStatus(200, response);
	}

}
