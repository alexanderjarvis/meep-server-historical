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
public class UserConnectionTest extends UnitTest {
	
	User user1;
	User user2;
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
		user1 = User.find("byEmail", "bob@gmail.com").first();
		user2 = User.find("byEmail", "bob2@gmail.com").first();
		UserConnectionHelper.createUserConnection(user1, user2);
	}
	
	@Test
	public void testModelSizes() {
		assertEquals(2, User.findAll().size());
	}
	
	@Test
	public void testUserConnectionsSize() {
		assertEquals(1, user1.userConnectionsTo.size());
	}
	
	@Test
	public void testTraverseUserConnectionGraph() {	
		// Test traversing to other user equals the other user
		assertTrue(user1.userConnectionsTo.get(0).equals(user2));
		assertTrue(user2.userConnectionsFrom.get(0).equals(user1));
	}
	
	@Test
	public void testDeleteUserConnectionSize() {	
		int sizeBefore = user1.userConnectionsTo.size();
		UserConnectionHelper.removeUserConnection(user1, user2);
		int sizeAfter = user1.userConnectionsTo.size();
		
		assertTrue(sizeAfter < sizeBefore);
	}
	
	@Test
	public void testIsUsersConnected() {
		assertTrue(UserConnectionHelper.isUsersConnected(user1, user2));
		User user = new User();
		assertFalse(UserConnectionHelper.isUsersConnected(user1, user));
	}

}
