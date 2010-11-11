package oauth2;

public interface CheckUserAuthentication {
	
	public boolean validCredentials(String client_id, String client_secret);

}
