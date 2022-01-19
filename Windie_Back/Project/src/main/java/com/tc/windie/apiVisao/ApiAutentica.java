package com.tc.windie.apiVisao;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tc.windie.controle.ManterUsuarios;

import modelo.UsuarioDesenvolvedorModelo;
import util.CustomException;
import util.Debug;
import util.ErrorCodes;
import util.RestObject;
import util.TokenManager;

@RequestMapping("/autentica")
@RestController
public class ApiAutentica {
	
	@PostMapping(path = "login")
	public RestObject autenticarUsuario(@RequestBody String restInput) throws JSONException, SQLException, JsonProcessingException{
		
		Debug.logInput("R04 login: "+restInput);
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);

		try {
			if(ManterUsuarios.getInstance().autenticaUsuario(jsonObj.getString("email"),jsonObj.getString("senha"))) {
				
				RestObject restOutput = new RestObject(TokenManager.getInstance().gerarToken(jsonObj.getString("email")),"");
				return restOutput;
				
			}else {
				
				//throw new CustomException("Erro ao entrar");
				RestObject restOutput = new RestObject(null,"",ErrorCodes.autenticacao,"Erro ao entrar","ApiAutentica/autenticarUsuario");
				return restOutput;
			}
		} catch (CustomException e) {
			e.printStackTrace();
			RestObject restOutput = new RestObject(null,"",ErrorCodes.autenticacao,e.getMessage(),e.getStackTraceText());
			return restOutput;
		}
	
	}
	
	@PostMapping(path = "logout")
	public void encerrarSessaoUsuario(@RequestBody String restInput) throws Exception {
		
		Debug.logInput("R06 Logout:"+restInput);
		
		String inputBody = RestObject.Desserialize(restInput).body;
		JSONObject jsonObj = new JSONObject(inputBody);

		TokenManager.getInstance().destruirToken(jsonObj.getString("token"));
		
	}
	
	@PostMapping(path = "dados")
	public RestObject carregarDadosUsuario(@RequestBody String restInput) throws Exception {
		
		Debug.logInput("solicitação de dados:"+restInput);
		
		String token = RestObject.Desserialize(restInput).token;
		
		String usuario_email = TokenManager.getInstance().getUser(token);
		UsuarioSessao usuario;
		if(ManterUsuarios.getInstance().seEmailDesenvolvedor(usuario_email)) {
		UsuarioDesenvolvedorModelo usuarioDesenvolvedor = ManterUsuarios.getInstance().getUsuarioForm(usuario_email);
		int user_id = ManterUsuarios.getInstance().GetIdByEmail(usuario_email);
		
		 usuario = new UsuarioSessao(
				user_id, 
				usuario_email, 
				usuarioDesenvolvedor.getApelido(), 
				usuarioDesenvolvedor.getNome_desenvolvedor(),
				ManterUsuarios.getInstance().devByUser(user_id).getDesenvolvedor_id(),
				ManterUsuarios.getInstance().seEmailAssinantes(usuario_email)
				);
		}else {
					 usuario = new UsuarioSessao(
							 	ManterUsuarios.getInstance().GetIdByEmail(usuario_email), 
								usuario_email, 
								ManterUsuarios.getInstance().getApelidoByEmail(usuario_email), 
								"",
								0,
								ManterUsuarios.getInstance().seEmailAssinantes(usuario_email)
								);
		}
		
		String novoToken = token ;//TokenManager.getInstance().autenticarToken(token);
		RestObject output = new RestObject(novoToken, usuario);
		
		Debug.logOutput("token output: "+novoToken);
		
		return output;
		
	}
	
}

class UsuarioSessao{
	
    public int usuario_id;
    public String email;
    public String apelido;
    public String nome_desenvolvedor;
    public int desenvolvedor_id;
    public boolean se_assinante;
    
	public UsuarioSessao(int usuario_id, String email, String apelido, String nome_desenvolvedor,
			int desenvolvedor_id, boolean se_assinante) {
		super();
		this.usuario_id = usuario_id;
		this.email = email;
		this.apelido = apelido;
		this.nome_desenvolvedor = nome_desenvolvedor;
		this.desenvolvedor_id = desenvolvedor_id;
		this.se_assinante = se_assinante;
	}
	
}
