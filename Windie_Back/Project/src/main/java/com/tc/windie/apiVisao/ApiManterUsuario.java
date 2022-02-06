package com.tc.windie.apiVisao;

import java.sql.SQLException;

import javax.security.sasl.AuthenticationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.ManterUsuarios;

import modelo.DesenvolvedorModelo;
import util.CustomException;
import util.Debug;
import util.ErrorCodes;
import util.RestObject;
import util.TokenManager;


@RequestMapping("/usuario")
@RestController
public class ApiManterUsuario {
	
	@PostMapping(path = "cadastrar")
	public RestObject cadastrarUsuario(@RequestBody String restInput) throws  JSONException, SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logInput("R01 Cadastrar usuario: "+inputBody);

		if(!jsonObj.getString("email").equals(jsonObj.getString("email_confirm"))) {
			
			return new RestObject("","",ErrorCodes.validacao,"E-mails fornecidos não correspondem","ApiManterUsuario");
			//throw new  CustomException("E-mails fornecidos não correspondem");
		}
		
		if(!jsonObj.getString("senha").equals(jsonObj.getString("senha_confirm"))) {
			
			return new RestObject("","",ErrorCodes.validacao,"Senhas fornecidas não correspondem","ApiManterUsuario");
			//throw new CustomException("Senhas fornecidas não correspondem");
		}
		
		try {
			ManterUsuarios.getInstance().cadastrarUsuario(jsonObj.getString("email"), jsonObj.getString("senha"), jsonObj.getString("apelido"));
		} catch (CustomException e) {
			e.printStackTrace();
			return new RestObject("","",ErrorCodes.validacao,e.getMessage(),e.getStackTrace().toString());
		}
		
		return new RestObject("","");
	}
	
	@PostMapping(path = "cadastrarDesenvolvedor")
	public RestObject cadastrarDesenvolvedor(@RequestBody String restInput) throws JSONException, SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logInput("R02 Cadastrar desenvolvedor : "+inputBody);
		
		String token = RestObject.Desserialize(restInput).token;
	
		try {
			ManterUsuarios.getInstance().cadastrarDadosDesenvolvedor(jsonObj.getString("nome_de_desenvolvedor"), 
					jsonObj.getString("email_paypal"),
					TokenManager.getInstance().getUser(token));
			return new RestObject(TokenManager.getInstance().autenticarToken(token), "");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}catch (CustomException e) {
			e.printStackTrace();
			try {
				return new RestObject(TokenManager.getInstance().autenticarToken(token), "",ErrorCodes.validacao,e.getMessage(),e.getStackTraceText());
			}catch (AuthenticationException e1) {
				e1.printStackTrace();
				return new RestObject(null,"",ErrorCodes.autenticacao,e1.getMessage(),"");
			}
		}
		
		
		
	}
	
	@PostMapping(path = "atualizar")
	public RestObject atualizarUsuario(@RequestBody String restInput) throws JSONException, SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logInput("R03 atualizar usuario : "+inputBody);
		
		String token = RestObject.Desserialize(restInput).token;
		
		try {
			if(ManterUsuarios.getInstance().seEmailDesenvolvedor(TokenManager.getInstance().getUser(token))){
				ManterUsuarios.getInstance().atualizarUsuario(TokenManager.getInstance().getUser(token),
						jsonObj.getString("apelido"), jsonObj.getString("nome_desenvolvedor"),
						jsonObj.getString("email_paypal"));
			}else {
				ManterUsuarios.getInstance().atualizarUsuario(TokenManager.getInstance().getUser(token),
						jsonObj.getString("apelido"), "",
						"");
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),e.getStackTrace().toString());
		} catch (CustomException e) {
			e.printStackTrace();
			try {
				return new RestObject(TokenManager.getInstance().autenticarToken(token),"",ErrorCodes.validacao,e.getMessage(),e.getStackTrace().toString());
			} catch (AuthenticationException e1) {
				e1.printStackTrace();
				return new RestObject(null,"",ErrorCodes.autenticacao,e1.getMessage(),e1.getStackTrace().toString());
			}
		}
		
		try {
			return new RestObject(TokenManager.getInstance().autenticarToken(token), "");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),e.getStackTrace().toString());
		}
		
		
	}
	
	@PostMapping(path = "desenvolvedor")
	public RestObject getDesenvolvedor(@RequestBody String restInput) throws SQLException, Exception {

		Debug.logInput("get desenvolvedor : "+restInput);
	
		String token = RestObject.Desserialize(restInput).token;
		DesenvolvedorModelo dev = ManterUsuarios.getInstance().devByUser(ManterUsuarios.getInstance().GetIdByEmail(TokenManager.getInstance().getUser(token)));
		
		return new RestObject(null, dev);


	}
	
	@PostMapping(path = "mudarSenha")
	public RestObject mudarSenha(@RequestBody String restInput) throws JSONException, SQLException, JsonProcessingException {
		Debug.logInput("mudar senha : "+restInput);
		String token = RestObject.Desserialize(restInput).token;
		JSONObject jsonObj = new JSONObject(RestObject.Desserialize(restInput).body);
		try {
			token = TokenManager.getInstance().autenticarToken(token);
			String email = TokenManager.getInstance().getUser(token);
			if(!jsonObj.getString("nova").equals(jsonObj.getString("confirmar"))) {
				return new RestObject(token,"",ErrorCodes.validacao,"As senhas informadas não são as mesmas","ApiManterUsuario.mudarSenha()");
			}
			ManterUsuarios.getInstance().mudarSenha(jsonObj.getString("antiga"), jsonObj.getString("nova"), email);
			return new RestObject(token,"");
		}catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"ApiManterUsuario.mudarSenha()");
		}catch (CustomException e) {
			e.printStackTrace();
			return new RestObject(token,"",ErrorCodes.validacao,e.getMessage(),e.getStackTraceText());
		}
	}
	
	
/*	@PostMapping(path = "nickName")
	public String getApelido(@RequestBody  String json) throws Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("R07 Get apelido: "+json);
		System.out.println("Token: "+jsonObj.getString("token"));

		return "{\"body\": \""+ManterUsuarios.getInstance().getApelidoByEmail(TokenManager.getInstance().getUser(jsonObj.getString("token")))+"\"}";

	}*/
	
	
	/*@PostMapping(path = "usuarioForm")
	public String getUsuarioForm(@RequestBody  String json) throws JSONException, Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonOut = ow.writeValueAsString(ManterUsuarios.getInstance().getUsuarioForm(TokenManager.getInstance().getUser(jsonObj.getString("token"))));
		
		System.out.println("Get usuarioForm token: "+jsonObj.getString("token"));
		
		return jsonOut;//"{\"body\": \""+"olá"+"\"}";
	}*/
	

}
