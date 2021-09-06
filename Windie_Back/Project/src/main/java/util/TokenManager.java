package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

public class TokenManager {

	private List<Token> tokens;
	private static TokenManager instance;
	
	public TokenManager() {
			
		tokens = new ArrayList<Token>();
	}
	
	public static TokenManager getInstance() {
		
		if(instance == null) {
			
			instance = new TokenManager();
		}
		
		return instance;
	}
	
	public String GenerateToken(String user) {
		
		for (Token token : tokens) {
			
			if(token.getUser().equals(user)) {
				
				token.setToken(GenerateTokenNumber());
				return token.getToken();
			}
		}
		
		Token newToken = new Token(GenerateTokenNumber(),user);
		tokens.add(newToken);
		return newToken.getToken();
	}
	
	public String AuthToken(String tokenAuth) throws Exception {
		
		
		
		for (Token tokenJson : tokens) {
			
			JSONObject jsonObj = new JSONObject(tokenJson.getToken());
			String token = jsonObj.getString("token");
			if(token.equals(tokenAuth)) {
				GenerateToken(tokenJson.getUser());
				return tokenJson.getToken();
			}
		}	
		
		throw new Exception("Token Inválido");
	}
	
	public void DestruirToken(String tokenDrop) {
		
		for (Token token : tokens) {
			
			if(token.getToken().equals(tokenDrop)) {
				tokens.remove(token);
			}
		}	
		
	}
	
	public String getUser(String tokenAuth) throws Exception {
		
		for (Token tokenJson : tokens) {
			
			JSONObject jsonObj = new JSONObject(tokenJson.getToken());
			String token = jsonObj.getString("token");
			if(token.equals(tokenAuth)) {
				//GenerateToken(tokenJson.getUser());
				return tokenJson.getUser();
			}
		}	
		
		throw new Exception("Token Inválido");
	}
	
	private String GenerateTokenNumber() {
	    String generatedString = Long.toHexString(Double.doubleToLongBits(Math.random()));
	    System.out.println("token gerado:"+generatedString);
		return "{\"token\": \""+generatedString+"\"}";
	}
}