package com.tc.windie.apiVisao;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.security.sasl.AuthenticationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.ReceberPagamento;

import dao.DesenvolvedorDAO;
import dao.UsuarioDAO;
import util.Debug;
import util.ErrorCodes;
import util.RestObject;
import util.TokenManager;

@RequestMapping("/painel")
@RestController
public class ApiDevPainel {
	
	@PostMapping(path = "faturadoUltimoMes")
	public RestObject faturadoUltimoMes(@RequestBody String restInput) throws SQLException, JsonProcessingException {
		
		/*String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);*/
		Debug.logInput("faturado no ultimo mes in: "+restInput);
		
		
		try {
			LocalDate data_atual = java.time.LocalDate.now();
			data_atual = Date.valueOf(data_atual.getYear()+"-"+data_atual.getMonthValue()+"-01").toLocalDate();
			data_atual = data_atual.minusMonths(1);
			Date mes = Date.valueOf(data_atual);
			String token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			String usuario_email = TokenManager.getInstance().getUser(token);
			int usuario_id = UsuarioDAO.getInstance().idByEmail(usuario_email);
			int desenvolvdo_id = DesenvolvedorDAO.getInstance().getByUser(usuario_id).getDesenvolvedor_id(); 
			float faturado = ReceberPagamento.getInstance().getFaturado(desenvolvdo_id , mes);
			return new RestObject(token,faturado);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}

	}
	
	@PostMapping(path = "horasUltimoMes")
	public RestObject horasJogadasUltimoMes(@RequestBody String restInput) throws SQLException, JsonProcessingException {
		Debug.logInput("horas jogadas no ultimo mes in: "+restInput);
		
		
		try {
			LocalDate data_atual = java.time.LocalDate.now();
			data_atual = Date.valueOf(data_atual.getYear()+"-"+data_atual.getMonthValue()+"-01").toLocalDate();
			data_atual = data_atual.minusMonths(1);
			Date mes = Date.valueOf(data_atual);
			String token = TokenManager.getInstance().autenticarToken(RestObject.Desserialize(restInput).token);
			String usuario_email = TokenManager.getInstance().getUser(token);
			int usuario_id = UsuarioDAO.getInstance().idByEmail(usuario_email);
			int desenvolvdo_id = DesenvolvedorDAO.getInstance().getByUser(usuario_id).getDesenvolvedor_id(); 
			float jogado = ReceberPagamento.getInstance().getHoras(desenvolvdo_id, mes);
			return new RestObject(token,jogado);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"");
		}

	}
	

}
