package util;

import java.util.ArrayList;
import java.util.List;

import javax.security.sasl.AuthenticationException;

public class TokenManager {

	private List<Token> tokens;
	private static TokenManager instance;
	
	public static TokenManager getInstance() {
		
		if(instance == null) {
			
			instance = new TokenManager();
		}
		
		return instance;
	}
	
	public TokenManager() {
			
		tokens = new ArrayList<Token>();
	}
	
	
	public String gerarToken(String user) {
		
		for (Token token : tokens) {
			
			if(token.getUser().equals(user)) {
				
				token.setToken(GenerateTokenNumber());
				return token.getToken();
			}
		}
		
		Token newToken = new Token(GenerateTokenNumber(),user);
		tokens.add(newToken);
		Debug.logOutput("token gerado:"+newToken.getToken());
		return newToken.getToken();
	}
	
	public String autenticarToken(String tokenAuth) throws AuthenticationException {
			
		for (Token tokenObject : tokens) {
			
			String token = tokenObject.getToken();
			if(token.equals(tokenAuth)) {
				gerarToken(tokenObject.getUser());
				return tokenObject.getToken();
			}
		}	
		
		throw new AuthenticationException("Sessão Inválida");
	}
	
	public void destruirToken(String tokenDrop) {
		
		for (Token token : tokens) {
			
			if(token.getToken().equals(tokenDrop)) {
				tokens.remove(token);
			}
		}	
		
	}
	
	public String getUser(String tokenAuth) throws  AuthenticationException  {
		
		for (Token tokenJson : tokens) {
			
			//JSONObject jsonObj = new JSONObject(tokenJson.getToken());
			String token = tokenJson.getToken();// jsonObj.getString("token");
			if(token.equals(tokenAuth)) {
				//GenerateToken(tokenJson.getUser());
				return tokenJson.getUser();
			}
		}	
		
		throw new  AuthenticationException("Sessão Inválida");
	}
	
	private String GenerateTokenNumber() {
	    String token = Long.toHexString(Double.doubleToLongBits(Math.random()));
	    System.out.println("token gerado:"+token);
		return token;
	}
}
