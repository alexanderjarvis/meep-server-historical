package oauth2.unit;

import oauth2.Security;

import org.junit.Test;

import play.test.UnitTest;

public class TestSecurity extends UnitTest {
	
	private final static String PASSWORD = "password";
	private final static String SHA256_HASH = "7a37b85c8918eac19a9089c0fa5a2ab4dce3f90528dcdeec108b23ddf3607b99";
	
	@Test
	public void testsha256hexWithSalt() {
		assertEquals(SHA256_HASH, Security.sha256hexWithSalt(PASSWORD));
		assertNotSame(SHA256_HASH, Security.sha256hexWithSalt(PASSWORD + "1"));
	}

}
