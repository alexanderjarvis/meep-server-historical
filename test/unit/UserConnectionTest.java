package unit;

import models.User;
import models.UserConnection;
import models.helpers.UserConnectionHelper;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.JavaBeanDumper;

import play.Logger;
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
		user1 = new User();
		user1.save();
		user2 = new User();
		user2.save();
		UserConnectionHelper.createUserConnection(user1, user2);
	}
	
	@Test
	public void testModelSizes() {
		assertEquals(2, User.findAll().size());
		assertEquals(2, UserConnection.findAll().size());
	}
	
	@Test
	public void testUserConnectionsSize() {
		User user = User.all().first();
		assertEquals(1, user.connections.size());
	}
	
	@Test
	public void testTraverseUserConnectionGraph() {
		// Test owner of connection
		assertTrue(user1.connections.get(0).user.equals(user1));
		
		// Test traversing to other user equals the other user
		assertTrue(user1.connections.get(0).userConnection.user.equals(user2));
		assertTrue(user2.connections.get(0).userConnection.user.equals(user1));
	}
	
	@Test
	public void testDeleteUserConnectionSize() {	
		int sizeBefore = user1.connections.size();
		UserConnectionHelper.removeUserConnection(user1, user2);
		int sizeAfter = user1.connections.size();
		
		assertTrue(sizeAfter < sizeBefore);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDeleteUserConnection1() {
		UserConnection con1 = user1.connections.get(0);
		UserConnectionHelper.removeUserConnection(user1, user2);
		con1.refresh();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDeleteUserConnection2() {
		UserConnection con2 = user2.connections.get(0);
		UserConnectionHelper.removeUserConnection(user1, user2);
		con2.refresh();
	}

}
