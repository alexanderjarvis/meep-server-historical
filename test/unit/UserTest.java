package unit;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Tests the most basic functions of the User model to verify its integrity.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserTest extends UnitTest {
	
	User user;
	
	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
		
		user = new User();
		user.email = "alex@jarvis.com";
		user.passwordHash = "1A2B";
		user.accessToken = "1A2B3C";
		user.firstName = "Alex";
		user.lastName = "Jarvis";
		user.mobileNumber = "123456";
		user.create();
	}
	
	@Test
	public void testFindUserByEmail() {
		User sameUser = User.find("byEmail", user.email).first();
		assertNotNull(sameUser);
		assertEquals(user, sameUser);
	}
	
	@Test
	public void testFindByMobileNumber() {
		User sameUser = User.find("byMobileNumber", user.mobileNumber).first();
		assertNotNull(sameUser);
		assertEquals(user, sameUser);
	}
	
	@Test
	public void testBasicAttributes() {
		
		User sameUser = User.findById(user.id);
		assertNotNull(sameUser);
		
		assertEquals(sameUser.email, user.email);
		assertEquals(sameUser.passwordHash, user.passwordHash);
		assertEquals(sameUser.accessToken, user.accessToken);
		assertEquals(sameUser.firstName, user.firstName);
		assertEquals(sameUser.lastName, user.lastName);
		assertEquals(sameUser.mobileNumber, user.mobileNumber);
	}
	
	@Test
	public void testDelete() {
		user.delete();
		User sameUser = User.findById(user.id);
		assertNull(sameUser);
	}
	
}