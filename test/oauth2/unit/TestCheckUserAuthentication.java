package oauth2.unit;

import oauth2.CheckUserAuthentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class TestCheckUserAuthentication extends UnitTest {
	
	@Before
	public void loadFixtures() {
		Fixtures.load("data.yml");
	}
	
	@Test
	public void testValidCredentials() {
		CheckUserAuthentication checkUserAuthentication = new CheckUserAuthentication();
		assertTrue(checkUserAuthentication.validateCredentials("bob@gmail.com", "secret"));
	}
	
	@Test
	public void testNonValidClientId() {
		CheckUserAuthentication checkUserAuthentication = new CheckUserAuthentication();
		assertFalse(checkUserAuthentication.validateCredentials("a@gmail.com", "secret"));
	}
	
	@Test
	public void testNonValidClientSecret() {
		CheckUserAuthentication checkUserAuthentication = new CheckUserAuthentication();
		assertFalse(checkUserAuthentication.validateCredentials("bob@gmail.com", "a"));
	}
	
	@After
	public void deleteFixtures() {
		Fixtures.deleteAll();
	}
	
}
