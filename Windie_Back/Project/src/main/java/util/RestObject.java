package util;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class RestObject { //objeto serve de modelo padrão para comunicação rest
	
	
	public String token;
	public String body;
	public boolean sucesso =  true; //SUCESSO ou ERRO
	public Erro erro; //objeto de erro caso de errado
	
	public RestObject(String token, String body) {
		super();
		this.token = token;
		this.body = body;
	}
	
	public RestObject(String token, Object body,ErrorCodes errorCode,String mensagemErro,String stackTrace) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		this.sucesso = false;
		this.token = token;
		this.body = ow.writeValueAsString(body);
		 
		erro = new Erro(errorCode.getValue(),mensagemErro,stackTrace);
	}
	
	public RestObject(String token, Object body) throws JsonProcessingException {
		super();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		this.sucesso = true;
		this.token = token;
		this.body = ow.writeValueAsString(body);
	}

	public static RestObject Desserialize(String json) {
		JSONObject jsonObj = new JSONObject(json);
		
		try {
			return new RestObject(jsonObj.getString("token"), jsonObj.get("body").toString());
		}catch (JSONException e) {
			return new RestObject(null, jsonObj.get("body").toString());
		}
	}
	

}


class Erro {
	public int cod;// 1: erro de autenticacao, 2 erro de validação de campo 
	public String mensagem; // a mensagem de erro gerada
	public String stackTrace;
	public Erro(int cod, String mensagem, String stackTrace) {
		super();
		this.cod = cod;
		this.mensagem = mensagem;
		this.stackTrace = stackTrace;
	}
}



