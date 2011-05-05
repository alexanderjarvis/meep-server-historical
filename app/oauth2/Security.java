package oauth2;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Security {
	
	//TODO: randomise the salt with each password and prefix it with the the password hash.
	private final static String SALT = "salt";
	
	public static String sha256hexWithSalt(String data) {
		data += SALT;
		final String hashedData = DigestUtils.sha256Hex(data);
		return hashedData;
	}
	
}
