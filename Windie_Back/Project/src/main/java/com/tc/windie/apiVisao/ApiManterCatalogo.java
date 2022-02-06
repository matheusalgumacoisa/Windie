package com.tc.windie.apiVisao;

import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.ManterCatalogo;
import com.tc.windie.controle.ManterJogos;
import com.tc.windie.controle.ManterUsuarios;

import dao.JogoDAO;
import util.CustomException;
import util.Debug;
import util.Ordenacoes;
import util.RestObject;
import util.TokenManager;

@RequestMapping("/catalogo")
@RestController
public class ApiManterCatalogo {
	
	
	@PostMapping(path = "jogos")
	public RestObject JogoModelo (@RequestBody String restInput) throws SQLException, JsonProcessingException, JSONException, CustomException {
		Debug.logRequest("jogos do catalogo: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		return new RestObject(null,ManterCatalogo.getInstance().getListaJogos(jsonObj.getInt("num_itens") , jsonObj.getInt("page"),Ordenacoes.popularidade));
	}
	
	@PostMapping(path = "pesquisar")
	public RestObject JogoPesquisa (@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("buscar jogos no catalogo: "+inputBody);
		
		return new RestObject(null, ManterCatalogo.getInstance().buscarJogos(jsonObj.getInt("num_itens") , jsonObj.getInt("page"),jsonObj.getString("termo"),Ordenacoes.popularidade));		
	}
	
	@PostMapping(path = "jogo")
	public RestObject getJogo (@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("get Jogo: "+inputBody);
		
		return new RestObject(null,ManterCatalogo.getInstance().getJogo(jsonObj.getInt("jogo_id")));
	}
	
	@PostMapping(path = "seJogoBiblioteca")
	public RestObject seJogoBiblioteca (@RequestBody String restInput) throws JSONException, Exception {
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("se jogo na biblioteca: "+inputBody);
		
		String token = RestObject.Desserialize(restInput).token;
		
		try {
			int usuario_id = ManterCatalogo.getInstance().idUsuarioByEmail(TokenManager.getInstance().getUser(token));
		
			return new RestObject(null,ManterCatalogo.getInstance().seJogoNaBiblioteca(jsonObj.getInt("jogo_id"),usuario_id));
		}catch(Exception e){
			return new RestObject(null,false);
		}
	}
	
	@PostMapping(path = "adicionarNaBiblioteca")
	public RestObject adicionarNaBiblioteca  (@RequestBody String restInput) throws JSONException, Exception {
		Debug.logRequest("adicionar jogo na biblioteca: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		
		String token = TokenManager.getInstance().autenticarToken( RestObject.Desserialize(restInput).token);
		String user_email = TokenManager.getInstance().getUser(token);
		
		int usuario_id = ManterCatalogo.getInstance().idUsuarioByEmail(user_email);
		ManterCatalogo.getInstance().inserirJogoBiblioteca(jsonObj.getInt("jogo_id"),usuario_id);
		
		return new RestObject(token,"");
	}
	@PostMapping(path = "removerDaBiblioteca")
	public RestObject removerDaBiblioteca  (@RequestBody String restInput) throws JSONException, Exception {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("remover jogo da biblioteca: "+inputBody);
		
		String token = TokenManager.getInstance().autenticarToken( RestObject.Desserialize(restInput).token);
		String user_email = TokenManager.getInstance().getUser(token);
		
		int usuario_id = ManterCatalogo.getInstance().idUsuarioByEmail(user_email);
		
		ManterCatalogo.getInstance().removerJogoBiblioteca(jsonObj.getInt("jogo_id"),usuario_id);
		
		return new RestObject(token,"");
	}
	
	@GetMapping(path = "generos")
	public RestObject getListaGeneros() throws SQLException, JsonProcessingException{
		
		return new RestObject(null,ManterCatalogo.getInstance().getListaGeneros());
	}
	
	@PostMapping(path = "screenshots")
	public RestObject getScreenshots(@RequestBody String restInput) throws Exception {
		
		Debug.logRequest("get screenshots jogo: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		
		return new RestObject(null,ManterJogos.getInstance().getScreenshots(jsonObj.getInt("jogo_id")));
	}
	
	@PostMapping(path = "numero")
	public RestObject getJogosNumero(@RequestBody String restInput) throws Exception {
		
		Debug.logRequest("get jogos numero: "+restInput);
		//String inputBody = RestObject.Desserialize(restInput).body;
		//JSONObject jsonObj = new JSONObject(inputBody);
		int numero = 0;
		
		if(RestObject.Desserialize(restInput).body.equals("CATALOGO")) {
			numero = JogoDAO.getInstance().getJogosCatalogoNumero();
		}
		if(RestObject.Desserialize(restInput).body.equals("APROVACAO")) {
			numero = JogoDAO.getInstance().getJogosAprovNumero();		
		}
		if(RestObject.Desserialize(restInput).body.equals("BIBLIOTECA")) {
			int usuario_id = ManterUsuarios.getInstance().GetIdByEmail(TokenManager.getInstance().getUser(RestObject.Desserialize(restInput).token));
			numero = JogoDAO.getInstance().getJogosBibliotecaNumero(usuario_id);
		}
		
		
		return new RestObject(null,numero);
	}
	
	

}
