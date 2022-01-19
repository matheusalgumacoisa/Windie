package com.tc.windie.apiVisao;

import java.sql.SQLException;

import javax.security.sasl.AuthenticationException;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.AvaliarJogos;

import dao.UsuarioDAO;
import modelo.AvaliacaoModelo;
import util.CustomException;
import util.Debug;
import util.ErrorCodes;
import util.RestObject;
import util.TokenManager;

@RestController
@RequestMapping("/avaliar")
public class ApiAvaliarJogos {
	
	
	@PostMapping(path = "nota")
	public RestObject Avaliar(@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		Debug.logRequest("Nova avaliação: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		

		try {
			String token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			int usuario_id;
			usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(token));
			try {
				AvaliarJogos.getInstance().Avaliar(usuario_id, jsonObj.getInt("jogo_id"),jsonObj.getInt("nota") );
				return new RestObject(token,"");
			}catch (CustomException e) {
				e.printStackTrace();
				return new RestObject(token,"",ErrorCodes.autenticacao,e.getMessage(),e.getStackTraceText());
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}
		
	}
	
	
	@PostMapping(path = "getAvaliacao")
	public RestObject  getAvaliacao(@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		Debug.logRequest("Get avaliação: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		String token;
		try {
			token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			int usuario_id;
			usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(token));
			AvaliacaoModelo avModelo = AvaliarJogos.getInstance().getAvaliacao(usuario_id,jsonObj.getInt("jogo_id"));
			return new RestObject(token,avModelo);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}

	}
	
	@PostMapping(path = "getHoras")
	public RestObject getHorasJogadas(@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		Debug.logRequest("Get horas: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		try {
			String token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			int usuario_id;
			usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(token));
			float horas_jogadas = AvaliarJogos.getInstance().getHorasJogadas(jsonObj.getInt("jogo_id"), usuario_id);
			
			return new RestObject(token,horas_jogadas);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}		
		
	}
	

}
