package oauth2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

import play.Logger;

public class Security {
	
	private final static String SALT = "salt";
	
	public static String sha256hexWithSalt(String data) {
		data += SALT;
		final String hashedData = DigestUtils.sha256Hex(data);
		return hashedData;
	}
	
}
