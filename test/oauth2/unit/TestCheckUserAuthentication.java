package oauth2.unit;

import models.User;
import oauth2.CheckUserAuthentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Tests the CheckUserAuthentication class in isolation.
 * 
 * @see CheckUserAuthentication
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class TestCheckUserAuthentication extends UnitTest {
	
	private static final String TEST_CLIENT_ID = "bob@gmail.com";
	private static final String TEST_CLIENT_SECRET = "password";
	private CheckUserAuthentication checkUserAuthentication;
	
	@Before
	public void loadFixtures() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("test-data.yml");
	}
	
	@Test
	public void testValidCredentials() {
		checkUserAuthentication = new CheckUserAuthentication();
		assertTrue(checkUserAuthentication.validCredentials(TEST_CLIENT_ID, TEST_CLIENT_SECRET));
	}
	
	@Test
	public void testNonValidClientId() {
		checkUserAuthentication = new CheckUserAuthentication();
		assertFalse(checkUserAuthentication.validCredentials("a@gmail.com", TEST_CLIENT_SECRET));
	}
	
	@Test
	public void testNonValidClientSecret() {
		checkUserAuthentication = new CheckUserAuthentication();
		assertFalse(checkUserAuthentication.validCredentials(TEST_CLIENT_ID, "a"));
	}
	
	@Test
	public void testReturnsUser() {
		testValidCredentials();
		User user = checkUserAuthentication.getAuthorisedUser();
		assertNotNull(user);
		assertEquals(user.email, TEST_CLIENT_ID);
	}
	
	@After
	public void deleteFixtures() {
		Fixtures.deleteAll();
	}
	
}
