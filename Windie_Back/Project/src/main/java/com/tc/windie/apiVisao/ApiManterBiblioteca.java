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
import com.tc.windie.controle.ManterBiblioteca;

import dao.UsuarioDAO;
import util.CustomException;
import util.Debug;
import util.ErrorCodes;
import util.Ordenacoes;
import util.RestObject;
import util.TokenManager;

@RequestMapping("/biblioteca")
@RestController
public class ApiManterBiblioteca {
	
	@PostMapping(path = "jogos")
	public RestObject JogoModelo (@RequestBody String restInput) throws SQLException, JsonProcessingException, JSONException, CustomException {
		Debug.logRequest("jogos da biblioteca: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		int usuario_id;
		try {
			usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(RestObject.Desserialize(restInput).token));
			return new RestObject(RestObject.Desserialize(restInput).token,ManterBiblioteca.getInstance().getListaJogos(jsonObj.getInt("num_itens") , jsonObj.getInt("page"),Ordenacoes.popularidade,usuario_id));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}

	}
	
	@PostMapping(path = "pesquisar")
	public RestObject JogoPesquisa (@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("buscar jogos na biblioteca: "+inputBody);
		
		int usuario_id;
		try {
			usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(RestObject.Desserialize(restInput).token));
			return new RestObject(RestObject.Desserialize(restInput).token, ManterBiblioteca.getInstance().buscarJogos(jsonObj.getInt("num_itens") , jsonObj.getInt("page"),jsonObj.getString("termo"),Ordenacoes.popularidade,usuario_id));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}
		
				
	}

}
