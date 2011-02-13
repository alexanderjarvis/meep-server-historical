package oauth2;

import models.User;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenGenerator {
	
	private static final int LENGTH = 32;
	
	/**
	 * 
	 * @return
	 */
	public static String generate() {
		String accessToken = RandomStringUtils.randomAlphanumeric(LENGTH);
		while (User.find("byAccessToken", accessToken) == null) {
			accessToken = RandomStringUtils.randomAlphanumeric(LENGTH);
		}
		return accessToken;
	}

}