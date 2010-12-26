package oauth2;

import org.apache.commons.lang.RandomStringUtils;

public class AccessTokenGenerator {
	
	private static final int LENGTH = 32;
	
	public static String generate() {
		return RandomStringUtils.randomAlphanumeric(LENGTH);
	}

}
