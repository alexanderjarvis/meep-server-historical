package unit;
import static org.junit.Assert.assertNotNull;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class UserTest extends UnitTest {
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
	}
	
	@Test
	public void testAddUser() {
		User user = new User();
		assertNotNull(user);
	}

}