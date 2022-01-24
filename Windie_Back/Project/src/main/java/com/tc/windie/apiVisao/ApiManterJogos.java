package com.tc.windie.apiVisao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.ManterJogos;

import dao.DesenvolvedorDAO;
import dao.UsuarioDAO;
import modelo.JogoModelo;
import util.CustomException;
import util.Debug;
import util.ErrorCodes;
import util.RestObject;
import util.TokenManager;

@RequestMapping("/jogos")
@RestController
public class ApiManterJogos {

	@PostMapping(path = "novo")
	public RestObject novoJogo(@RequestBody String restInput) throws SQLException, JsonProcessingException{
		
		Debug.logRequest("novo jogo: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		String token;
		
		try {
			
			token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			if(jsonObj.optString("imagem_capa").isEmpty()) {
				return new RestObject(token,"",ErrorCodes.validacao,"Insira uma imagem de capa no formado .png ou .jpeg","");
			}
			byte[] img_bArray= Base64.getDecoder().decode(jsonObj.optString("imagem_capa").substring(jsonObj.optString("imagem_capa").indexOf(",")+1));// a substring aqui esta pegando somente o binario e descartando os metadados
			JogoModelo novoJogoModelo = new JogoModelo(0, 
														jsonObj.optString("titulo"), 
														jsonObj.optString("descricao"), 
														jsonObj.optString("caminho_executavel"), 
														jsonObj.optString("detalhes"), 
														jsonObj.optString("tags"), 
														jsonObj.optString("visibilidade"), 
														 img_bArray, 
														jsonObj.optInt("genero"),
														"");
			
			novoJogoModelo.setDesenvolvedor_id(ManterJogos.getInstance().getDevIdByToken(token));
			
			List<byte[]> screenshots = new ArrayList<>(); //cria uma lista de bytearray para armazenar as screenshots
		    
			JSONArray jsonArray = jsonObj.getJSONArray("screenshots"); //pega a lista de screenshots do json
			
			if (jsonArray != null) { 
			   for (int i=0;i<jsonArray.length();i++){ //percorre a lista do json e adiciona os elementos a lista do java
				   byte[] imageByArray = Base64.getDecoder().decode(jsonArray.get(i).toString().substring(jsonArray.get(i).toString().indexOf(",")+1)); //descodifica a imagem base64
				   screenshots.add(imageByArray);
			   } 
			}
			
			try {
				int jogo_id =  ManterJogos.getInstance().inserirJogo(novoJogoModelo,screenshots);
				return new RestObject(token,jogo_id);
			} catch (CustomException e) {
				e.printStackTrace();
				return new RestObject(token,"",ErrorCodes.validacao,e.getMessage(),e.getStackTraceText());
			}
			
			
			
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}
		
		
			
		
	}
	
	@PostMapping(path = "atualizar")
	public RestObject atualizar(@RequestBody String restInput)throws SQLException, JsonProcessingException {
		
		Debug.logRequest("Novo jogo a ser inserido: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		String token;
		try {
			token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			if(jsonObj.optString("imagem_capa").isEmpty()) {
				return new RestObject(token,"",ErrorCodes.validacao,"Insira uma imagem de capa no formado .png ou .jpeg","");
			}		
			byte[] bArray= Base64.getDecoder().decode(jsonObj.getString("imagem_capa").substring(jsonObj.getString("imagem_capa").indexOf(",")+1));// a substring aqui esta pegando somente o binario e descartando os metadados
			JogoModelo jogoEditadoModelo = new JogoModelo(jsonObj.getInt("jogo_id"), 
														jsonObj.getString("titulo"), 
														jsonObj.getString("descricao"), 
														jsonObj.getString("caminho_executavel"), 
														jsonObj.getString("detalhes"), 
														jsonObj.getString("tags"), 
														jsonObj.getString("visibilidade"), 
														 bArray, 
														jsonObj.getInt("genero"),
														"");
	
			jogoEditadoModelo.setDesenvolvedor_id(ManterJogos.getInstance().getDevIdByToken(token));
			
			if(!ManterJogos.getInstance().seDesenvolvedorDoJogo(jogoEditadoModelo.getJogo_id(), jogoEditadoModelo.getDesenvolvedor_id())){
				throw new AuthenticationException("Não é desenvolvedor desse jogo");
			}
			
			List<byte[]> screenshots = new ArrayList<>(); //cria uma lista de bytearray para armazenar as screenshots
	    
			JSONArray jsonArray = jsonObj.getJSONArray("screenshots"); //pega a lista de screenshots do json
			if (jsonArray != null) { 
			   for (int i=0;i<jsonArray.length();i++){ //percorre a lista do json e adiciona os elementos a lista do java
				   byte[] imageByArray = Base64.getDecoder().decode(jsonArray.get(i).toString().substring(jsonArray.get(i).toString().indexOf(",")+1)); //descodifica a imagem base64
				   screenshots.add(imageByArray);
			   } 
			}
			
			
			try {
				ManterJogos.getInstance().atualizarJogo(jogoEditadoModelo,screenshots);
				return new RestObject(token,"");
			} catch (CustomException e) {
				e.printStackTrace();
				return new RestObject(token,"",ErrorCodes.validacao,e.getMessage(),e.getStackTraceText());
			}
			
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}
	}
	
	@PostMapping(path = "screenshots")
	public RestObject getScreenshots(@RequestBody String restInput) throws Exception {
		
		Debug.logRequest("get screenshots jogo: "+restInput);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		
		
		return new RestObject(null,ManterJogos.getInstance().getScreenshots(jsonObj.getInt("jogo_id")));
	}
	
	@PostMapping(path = "jogosByDesenvolvedor")
	public RestObject getjogosByDesenvolvedor(@RequestBody String restInput) throws Exception{
		
		Debug.logRequest("jogos by Desenvolvedor: "+restInput);
		//String inputBody = RestObject.Desserialize(restInput).body;
		//JSONObject jsonObj = new JSONObject(inputBody);
	
		
		return new RestObject (null,ManterJogos.getInstance().getJogosByDesenvolvedor(ManterJogos.getInstance().getDevIdByToken(RestObject.Desserialize(restInput).token)));
	}
	
	@PostMapping(path = "salvarArquivos")
	public RestObject salvarArquivos(@RequestBody String restInput) throws SQLException, JsonProcessingException, AuthenticationException{
		Debug.logRequest("salvarArquivos: "+RestObject.Desserialize(restInput).token);
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		String token =RestObject.Desserialize(restInput).token;
		

		try {
			
			int desenvolvedorUserId = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(token));
			
			if(!ManterJogos.getInstance().seDesenvolvedorDoJogo(jsonObj.getInt("jogo_id"), DesenvolvedorDAO.getInstance().getByUser(desenvolvedorUserId).getDesenvolvedor_id())){
				throw new AuthenticationException("Não é desenvolvedor desse jogo");
			}
			
			token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			byte[] arquivo = /*jsonObj.get("arquivo").toString().getBytes();*/Base64.getDecoder().decode(jsonObj.get("arquivo").toString().substring(jsonObj.get("arquivo").toString().indexOf(",")+1));
			ManterJogos.getInstance().salvarArquivos(jsonObj.getInt("jogo_id"),arquivo);
			return new RestObject(token,"");
		}catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}catch (IOException e) {
			e.printStackTrace();
			return new RestObject(token,"",ErrorCodes.validacao,"Erro ao salvar arquivo",e.getMessage());
		}
	}
	
	
	@GetMapping(path = "generos")
	public RestObject getListaGeneros() throws SQLException, JsonProcessingException{
		
		return new RestObject(null,ManterJogos.getInstance().getListaGeneros());
	}
	
	@PostMapping(path = "jogo")
	public RestObject getJogo (@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("get Jogo: "+inputBody);
		

		
		return new RestObject(null,ManterJogos.getInstance().getJogo(jsonObj.getInt("jogo_id")));
	}
	
	
}
