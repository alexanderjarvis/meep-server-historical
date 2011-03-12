package unit;
import static org.junit.Assert.assertNotNull;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class UserTest extends UnitTest {
	
	@Before
	public void setUp() {
		Fixtures.deleteAllModels();
	}
	
	@Test
	public void testAddUser() {
		User user = new User();
		assertNotNull(user);
	}
	
	@Test
	public void testGetUser() {
		User user = new User();
		user.email = "alex@jarvis.com";
		user.create();
		assertNotNull(user);
		//User sameUser = User.findById(user.id);
		User sameUser = User.find("byEmail", user.email).first();
	}

}