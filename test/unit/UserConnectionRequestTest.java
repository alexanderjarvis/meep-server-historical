package unit;

import models.User;
import models.helpers.UserConnectionHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserConnectionRequestTest extends UnitTest {
	
	User user1;
	User user2;
	
	@Before
	public void setUp() {
		Fixtures.load("data.yml");
		user1 = User.find("byEmail", "bob@gmail.com").first();
		user2 = User.find("byEmail", "bob2@gmail.com").first();
	}
	
	@After
	public void tearDown() {
		Fixtures.deleteAll();
	}
	
	@Test
	public void testCreateUserConnectionRequest() {
		boolean result = false;
		result = UserConnectionHelper.createUserConnectionRequest(user1, user2);
		assertTrue(result);
		assertEquals(user2, user1.userConnectionRequestsTo.get(0));
		assertEquals(user1, user2.userConnectionRequestsFrom.get(0));
	}
	
	@Test
	public void testCreateUserConnectionRequestTwice() {
		boolean result = false;
		result = UserConnectionHelper.createUserConnectionRequest(user1, user2);
		assertTrue(result);
		result = UserConnectionHelper.createUserConnectionRequest(user1, user2);
		assertFalse(result);
	}
	
	@Test
	public void testRemoveUserConnectionRequest() {
		boolean result = false;
		result = UserConnectionHelper.createUserConnectionRequest(user1, user2);
		assertTrue(result);
		assertEquals(1, user1.userConnectionRequestsTo.size());
		assertEquals(1, user2.userConnectionRequestsFrom.size());
		result = UserConnectionHelper.removeUserConnectionRequest(user1, user2);
		assertTrue(result);
		assertEquals(0, user1.userConnectionRequestsTo.size());
		assertEquals(0, user2.userConnectionRequestsFrom.size());
	}
	
	@Test
	public void testRemoveUserConnectionRequestTwice() {
		boolean result = false;
		result = UserConnectionHelper.createUserConnectionRequest(user1, user2);
		assertEquals(1, user1.userConnectionRequestsTo.size());
		assertEquals(1, user2.userConnectionRequestsFrom.size());
		result = UserConnectionHelper.removeUserConnectionRequest(user1, user2);
		assertTrue(result);
		assertEquals(0, user1.userConnectionRequestsTo.size());
		assertEquals(0, user2.userConnectionRequestsFrom.size());
		result = UserConnectionHelper.removeUserConnectionRequest(user1, user2);
		assertFalse(result);
		assertEquals(0, user1.userConnectionRequestsTo.size());
		assertEquals(0, user2.userConnectionRequestsFrom.size());
	}
	
	@Test
	public void testCreateBiDirectionalUserConnectionRequest() {
		boolean result = false;
		result = UserConnectionHelper.createUserConnectionRequest(user1, user2);
		assertTrue(result);
		result = UserConnectionHelper.createUserConnectionRequest(user2, user1);
		assertFalse(result);
	}
	
	@Test
	public void testCreateUserConnectionRequestForConnectedUsers() {
		UserConnectionHelper.createUserConnection(user1, user2);
		assertFalse(UserConnectionHelper.createUserConnectionRequest(user1, user2));
	}

}
