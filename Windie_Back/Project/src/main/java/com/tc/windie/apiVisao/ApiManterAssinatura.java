package com.tc.windie.apiVisao;

import java.io.IOException;
import java.sql.SQLException;

import javax.security.sasl.AuthenticationException;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.ManterAssinatura;
import com.tc.windie.controle.ManterUsuarios;

import util.CustomException;
import util.Debug;
import util.ErrorCodes;
import util.RestObject;
import util.TokenManager;

@RequestMapping("/assinatura")
@RestController
public class ApiManterAssinatura {
	
	/*@PostMapping(path = "checkoutCode")
	public RestObject getCheckoutCode(@RequestBody String restInput) throws SQLException, SAXException, IOException, ParserConfigurationException {
		
		Debug.logRequest("get checkout code: "+restInput);
	
		try {
			
			String usuario_email = TokenManager.getInstance().getUser(RestObject.Desserialize(restInput).token);
			int usuario_id = UsuarioDAO.getInstance().idByEmail(usuario_email);
			
			String code = ManterAssinatura.getInstance().generateCheckout(usuario_id);
			RestObject rsto = new RestObject( RestObject.Desserialize(restInput).token,code);
			
			return  rsto;
			
		}  catch (CustomException e) {
			e.printStackTrace();
			return new RestObject( RestObject.Desserialize(restInput).token,"",ErrorCodes.validacao,e.getMessage(),e.getStackTraceText());
		}

	}*/
	
	
	@PostMapping(path = "pendente")
	public RestObject sePagamentoPendente(@RequestBody String restInput) throws JsonProcessingException, SQLException {
		Debug.logRequest("se pagamento pendente: "+restInput);
		
		int usuario_id;
		try {
			usuario_id = ManterUsuarios.getInstance().GetIdByEmail(TokenManager.getInstance().getUser(RestObject.Desserialize(restInput).token));
			return new RestObject(RestObject.Desserialize(restInput).token, ManterAssinatura.getInstance().sePagamentoPendente(usuario_id));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),"ApiManterAssinatura.sePagamentoPendente()");
		}
		
	}
	
	@PostMapping(path = "getURL")
	public RestObject getPagamentoURL(@RequestBody String restInput) throws SQLException, SAXException, IOException, ParserConfigurationException{
		Debug.logRequest("get Pagamento URL: "+restInput);
		
		int usuario_id;
		usuario_id = ManterUsuarios.getInstance().GetIdByEmail(TokenManager.getInstance().getUser(RestObject.Desserialize(restInput).token));
		String url;
		try {
			url = ManterAssinatura.getInstance().generateStripeCheckout(usuario_id);
			return new RestObject(RestObject.Desserialize(restInput).token,url);
		} catch (CustomException e) {
			e.printStackTrace();
			return new RestObject(RestObject.Desserialize(restInput).token,"",ErrorCodes.validacao,e.getMessage(),e.getStackTraceText());
		}
		
		
		
	}

}
