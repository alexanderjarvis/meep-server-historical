package unit;
import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

/**
 * @author http://www.playframework.org/
 */
public class BasicTest extends UnitTest {
	
	@Before
    public void setup() {
        Fixtures.deleteAll();
    }

	@Test
	public void createAndRetrieveUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob", "Someone").save();
	    
	    // Retrieve the user with e-mail address bob@gmail.com
	    User bob = User.find("byEmail", "bob@gmail.com").first();
	    
	    // Test 
	    assertNotNull(bob);
	    assertEquals("Bob", bob.firstName);
	}
	
	@Test
	public void tryConnectAsUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob", "Someone").save();
	    
	    // Test 
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNull(User.connect("bob@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	}

}
