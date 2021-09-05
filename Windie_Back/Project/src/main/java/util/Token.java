package util;

public class Token {

	private String token;
	private String user;
	
	public Token(String token, String user) {
		
		this.user = user;
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
}
