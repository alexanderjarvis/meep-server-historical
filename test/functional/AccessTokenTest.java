package functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

public class AccessTokenTest extends FunctionalTest {
	
	private Response response;
	
	private void requestKey() {
		response = POST("/auth/?grant_type=password&client_id=id&client_secret=secret");
	}
	
	@Test
	public void testResponseOK() {
		requestKey();
		assertIsOk(response);
	}
	
	@Test
	public void testResponseKeyLength() {
		requestKey();
		assertEquals(32, response.out.toString().length());
	}
	
	@Test
	public void testKeyIsRandom() {
		requestKey();
		String key1 = response.out.toString();
		requestKey();
		String key2 = response.out.toString();
		assertNotSame(key1, key2);
	}
	
	@Test
	public void testOtherGrantTypes() {
		// authorization_code
		response = POST("/auth/?grant_type=authorization_code&client_id=id&client_secret=secret");
		assertStatus(400, response);
		
		// unknown
		response = POST("/auth/?grant_type=unknown&client_id=id&client_secret=secret");
		assertStatus(400, response);
	}
	
	@Test
	public void testValidationErrors() {
		//TODO: test when missing params
	}
	
	@After
	public void log() {
		if (response != null) {
			Logger.debug("Response: " + response.out.toString());
		}
	}

}
