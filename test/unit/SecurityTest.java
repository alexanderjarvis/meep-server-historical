package unit;

import models.User;

import org.junit.Before;
import org.junit.Test;

import controllers.Security;

public class SecurityTest {
	
	@Before
	public void setUp() {
		User user = new User("alex@password.com", "ASDFGH", "alex", "");
	}
	
	@Test
	public void testAuthenticate() {
		Security.authenticate("alex@password.com", "ASDFGH");
	}

}
