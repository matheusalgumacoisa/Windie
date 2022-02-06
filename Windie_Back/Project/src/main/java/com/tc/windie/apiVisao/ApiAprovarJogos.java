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
import com.tc.windie.controle.AprovarJogos;

import dao.UsuarioDAO;
import modelo.Voto;
import util.CustomException;
import util.Debug;
import util.ErrorCodes;
import util.Ordenacoes;
import util.RestObject;
import util.TokenManager;

@RequestMapping("/aprovar")
@RestController
public class ApiAprovarJogos {
	@PostMapping(path = "jogos")
	public RestObject JogoModelo (@RequestBody String restInput) throws SQLException, JsonProcessingException, JSONException, CustomException {
		Debug.logRequest("jogos em aprovação: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		try {
			String token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			return new RestObject(token,AprovarJogos.getInstance().getListaJogos(Ordenacoes.popularidade,jsonObj.getInt("num_itens") , jsonObj.getInt("page")));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}
		
	}
	
	@PostMapping(path = "pesquisar")
	public RestObject JogoPesquisa (@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("buscar jogos em aprovação: "+inputBody);
		
		return new RestObject(null, AprovarJogos.getInstance().buscarJogos(Ordenacoes.popularidade,jsonObj.getInt("num_itens") , jsonObj.getInt("page"),jsonObj.getString("termo")));		
	}
	
	@PostMapping(path = "votar")
	public RestObject votar(@RequestBody String restInput) throws JsonProcessingException, JSONException, SQLException{
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("votar jogos em aprovação: "+inputBody);
		
		try {
			String token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			int usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(token));
			try {
				AprovarJogos.getInstance().votar(jsonObj.getBoolean("voto"), jsonObj.getInt("jogo_id"), usuario_id);
				return new RestObject(token,"");
			}catch (CustomException e) {
				return new RestObject(token,"",ErrorCodes.validacao,e.getMessage(),e.getStackTraceText());
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}
	}
	 
	@PostMapping(path = "seVotou")
	public RestObject seVotou(@RequestBody String restInput) throws SQLException, JsonProcessingException, JSONException, CustomException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("se votou: "+inputBody);
		
		try {
			String token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			int usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(token));
			
			boolean se_votou = AprovarJogos.getInstance().seVotou(jsonObj.getInt("jogo_id"), usuario_id);
			if(se_votou) {
				boolean voto = AprovarJogos.getInstance().getVoto(jsonObj.getInt("jogo_id"), usuario_id); 
				return new RestObject(token,new Voto(se_votou, voto));
			}else {
				return new RestObject(token,new Voto(se_votou, false));
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}
	}
	

	
}
