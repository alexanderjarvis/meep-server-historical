package oauth2;

import models.User;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Generates a unique, random, alphanumeric string of 32 characters length intended to be
 * used as an access token for OAuth.
 * 
 * It is imperative that the access token is unique for each User.
 * 
 * @see User
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AccessTokenGenerator {
	
	/**
	 * The length of the access token to be generated.
	 */
	private static final int LENGTH = 32;
	
	/**
	 * Generates and returns a unique access token. Will generate an access token until a unique
	 * one is created in the rare case that one already exists with the same value.
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