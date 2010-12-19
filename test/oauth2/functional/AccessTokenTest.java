package oauth2.functional;

import oauth2.OAuth2Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

public class AccessTokenTest extends FunctionalTest {
	
	private Response response;
	private String access_token;
	
	private void requestAccessToken() {
		response = POST("/oauth2/?grant_type=password&client_id=bob@gmail.com&client_secret=password");
		access_token = response.out.toString();
	}
	
	@Before
	public void loadFixtures() {
		Fixtures.load("data.yml");
		
	}
	
	@Test
	public void testResponseOK() {
		requestAccessToken();
		assertIsOk(response);
	}
	
	@Test
	public void testResponseKeyLength() {
		requestAccessToken();
		assertEquals(32, access_token.length());
	}
	
	@Test
	public void testKeyIsRandom() {
		requestAccessToken();
		String key1 = access_token;
		requestAccessToken();
		String key2 = access_token;
		assertNotSame(key1, key2);
	}
	
	@Test
	public void testUnknownGrantTypes() {	
		response = POST("/oauth2/?grant_type=unknown&client_id=bob@gmail.com&client_secret=secret");
		assertStatus(400, response);
	}
	
	@Test
	public void testValidationErrors() {
		response = POST("/oauth2/?grant_type=password&client_id=bob@gmail.com");
		assertStatus(400, response);
	}
	
	@Test
    public void testBadRequest() {
        response = GET("/");
        assertStatus(400, response);
    }
	
	@Test
	public void testAccessWithToken() {
		requestAccessToken();
		response = GET("/?"+OAuth2Constants.PARAM_OAUTH_TOKEN+"="+access_token);
		assertStatus(200, response);
	}
	
	@After
	public void log() {
		if (response != null) {
			Logger.debug("Response: " + response.out.toString());
		}
	}

}
