package com.tc.windie.apiVisao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.security.sasl.AuthenticationException;
import javax.sql.rowset.serial.SerialBlob;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.ManterBiblioteca;

import dao.UsuarioDAO;
import dao.VW_usuarioAssinaturaDAO;
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
	
	@GetMapping(path = "download")
	public  ResponseEntity<InputStreamResource> downloadJogo(@RequestParam String token,@RequestParam int jogo_id) throws JsonProcessingException, JSONException, IOException, SQLException, CustomException {
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		

		Debug.logRequest("baixar jogo da biblioteca: token: "+token+" jogo_id: "+jogo_id);
		int usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(token));
		if(!VW_usuarioAssinaturaDAO.getInstance().seAssinante(usuario_id)) {
			return null;
		}
		
		File file = new File("F:/WindieServerFiles/arquivos_jogo_"+jogo_id+".zip");
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		return ResponseEntity.ok().headers(httpHeaders).contentLength(file.length()).body(resource);
		
		//Blob b =new SerialBlob(ManterBiblioteca.getInstance().getFiles(jsonObj.getInt("jogo_id"),usuario_id));
		//return new ResponseEntity<byte[]>(ManterBiblioteca.getInstance().getFiles(jsonObj.getInt("jogo_id"),usuario_id), httpHeaders, HttpStatus.OK);

	}
	
	@PostMapping(path = "horas")
	public  RestObject registrarHoras(@RequestBody String restInput) throws JsonProcessingException, JSONException, IOException, SQLException{
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);
		Debug.logRequest("registrando horas: "+inputBody);
		
		int usuario_id = UsuarioDAO.getInstance().idByEmail(TokenManager.getInstance().getUser(RestObject.Desserialize(restInput).token));
		
		ManterBiblioteca.getInstance().addHoras(jsonObj.getFloat("horas"), jsonObj.getInt("jogo_id"),usuario_id);
		
		return new RestObject(RestObject.Desserialize(restInput).token,"");
	}

}
