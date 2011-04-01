package unit;

import models.User;
import models.helpers.UserConnectionHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Tests creating and removing connection requests between Users using the UserConnectionHelper.
 * 
 * Connection requests are modelled as a many-to-many relationship with User
 * but are unidirectional and so this is constraint is tested for.
 * 
 * @see User
 * @see UserConnectionHelper
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserConnectionRequestTest extends UnitTest {
	
	User user1;
	User user2;
	
	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("test-data.yml");
		user1 = User.find("byEmail", "bob@gmail.com").first();
		user2 = User.find("byEmail", "bob2@gmail.com").first();
	}
	
	@Test
	public void testCreateUserConnectionRequest() {
		assertTrue(UserConnectionHelper.createUserConnectionRequest(user1, user2));
		assertEquals(user2, user1.userConnectionRequestsTo.get(0));
		assertEquals(user1, user2.userConnectionRequestsFrom.get(0));
	}
	
	@Test
	public void testCreateUserConnectionRequestTwice() {
		assertTrue(UserConnectionHelper.createUserConnectionRequest(user1, user2));
		assertFalse(UserConnectionHelper.createUserConnectionRequest(user1, user2));
	}
	
	@Test
	public void testRemoveUserConnectionRequest() {
		assertTrue(UserConnectionHelper.createUserConnectionRequest(user1, user2));
		assertEquals(1, user1.userConnectionRequestsTo.size());
		assertEquals(1, user2.userConnectionRequestsFrom.size());
		assertTrue(UserConnectionHelper.removeUserConnectionRequest(user1, user2));
		assertEquals(0, user1.userConnectionRequestsTo.size());
		assertEquals(0, user2.userConnectionRequestsFrom.size());
	}
	
	@Test
	public void testRemoveUserConnectionRequestTwice() {
		assertTrue(UserConnectionHelper.createUserConnectionRequest(user1, user2));
		assertEquals(1, user1.userConnectionRequestsTo.size());
		assertEquals(1, user2.userConnectionRequestsFrom.size());
		assertTrue(UserConnectionHelper.removeUserConnectionRequest(user1, user2));
		assertEquals(0, user1.userConnectionRequestsTo.size());
		assertEquals(0, user2.userConnectionRequestsFrom.size());
		assertFalse(UserConnectionHelper.removeUserConnectionRequest(user1, user2));
		assertEquals(0, user1.userConnectionRequestsTo.size());
		assertEquals(0, user2.userConnectionRequestsFrom.size());
	}
	
	@Test
	public void testCreateBiDirectionalUserConnectionRequest() {
		assertTrue(UserConnectionHelper.createUserConnectionRequest(user1, user2));
		assertFalse(UserConnectionHelper.createUserConnectionRequest(user2, user1));
	}
	
	@Test
	public void testCreateUserConnectionRequestForConnectedUsers() {
		assertTrue(UserConnectionHelper.createUserConnection(user1, user2));
		assertFalse(UserConnectionHelper.createUserConnectionRequest(user1, user2));
	}

}
