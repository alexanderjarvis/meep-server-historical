package unit;
import models.User;
import models.UserConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import factories.UserConnectionFactory;

import play.Logger;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * 
 * @author alex
 */
public class UserConnectionTest extends UnitTest {
	
	private User user1;
	private User user2;
	private UserConnection con;
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		
		user1 = new User("user1", "password", "", "");
		user2 = new User("user2", "password", "", "");
		user1.save();
		user2.save();
		con = UserConnectionFactory.createUserConnection(user1, user2);
		con.save();
	}
	
	@Test
	public void testUserConnectionSize() {	
		assertEquals(1, user1.connections.size());
		assertEquals(1, user2.connections.size());
	}
	
	@Test 
	public void testDeleteConnection() {
		
		assertEquals(1, user1.connections.size());
		con.delete();
		assertEquals(0, user1.connections.size());
	}
	
	@After
	public void tearDown() {
		Fixtures.deleteAll();
	}
	
	
	

}
