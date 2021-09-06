package apiVisao;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import controle.ManterUsuarios;
import util.TokenManager;


@RequestMapping("/usuario")
@RestController
public class VisaoUsuario {
	
	
	@PostMapping(path = "cadastrar")
	public void cadastrarUsuario(@RequestBody  String json) throws SQLException {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("requisição recebida: "+json);
		System.out.println("E-mail: "+jsonObj.getString("email"));

		
		ManterUsuarios.getInstance().CadastrarUsuario(jsonObj.getString("email"), jsonObj.getString("senha"), jsonObj.getString("apelido"));
		
	}
	
	@PostMapping(path = "cadastrarDesenvolvedor")
	public void cadastrarDesenvolvedor(@RequestBody  String json) throws JSONException, Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("Cadastrar desenvolvedor : "+json);
	//	System.out.println("E-mail: "+jsonObj.getString("email"));

		
		ManterUsuarios.getInstance().CadastrarDesenvolvedor(jsonObj.getString("nome_de_desenvolvedor"), jsonObj.getString("agencia_bancaria"), jsonObj.getString("conta_bancaria"),TokenManager.getInstance().getUser(jsonObj.getString("token")));
		
	}
	
	@PostMapping(path = "atualizar")
	public void atualizarUsuario(@RequestBody  String json) throws JSONException, Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("atualizar usuario: "+json);
		System.out.println("Token: "+jsonObj.getString("token"));

		
		ManterUsuarios.getInstance().AtualizarUsuario(TokenManager.getInstance().getUser(jsonObj.getString("token")),jsonObj.getString("apelido"), jsonObj.getString("nome_desenvolvedor"), jsonObj.getString("agencia"),jsonObj.getString("conta"));
		
	}
	
	
	@PostMapping(path = "login")
	public String autenticarUsuario(@RequestBody  String json) throws Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("requisição recebida: "+json);
		System.out.println("E-mail: "+jsonObj.getString("email"));

		if(ManterUsuarios.getInstance().AutenticaUsuario(jsonObj.getString("email"),jsonObj.getString("senha"))) {
			
			return TokenManager.getInstance().GenerateToken(jsonObj.getString("email"));
			
		}else {
			
			throw new Exception("Dados de login invalidos");
		}

		
	}
	
	@PostMapping(path = "autorizar")
	public String autenticarTokenUsuario(@RequestBody  String json) throws Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("check token: "+jsonObj.getString("token"));

		return TokenManager.getInstance().AuthToken(jsonObj.getString("token"));
		
	}
	
	@PostMapping(path = "logout")
	public void encerrarUsuario(@RequestBody  String json) throws Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("logout token: "+jsonObj.getString("token"));

		TokenManager.getInstance().DestruirToken(jsonObj.getString("token"));
		
	}
	
	@PostMapping(path = "nickName")
	public String getNick(@RequestBody  String json) throws Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("Get nickname token: "+jsonObj.getString("token"));

		return "{\"body\": \""+ManterUsuarios.getInstance().apelidoByEmail(TokenManager.getInstance().getUser(jsonObj.getString("token")))+"\"}";

	}
	
	@PostMapping(path = "papel")
	public String getPapel(@RequestBody  String json) throws Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("Get papel token: "+jsonObj.getString("token"));

		return "{\"body\": \""+ManterUsuarios.getInstance().getPapel(TokenManager.getInstance().getUser(jsonObj.getString("token")))+"\"}";

	}
	
	@PostMapping(path = "assinatura")
	public String getUsuarioAssinatura(@RequestBody  String json) throws JSONException, Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		System.out.println("Get assinatura token: "+jsonObj.getString("token"));
		
		return "{\"body\": \""+ManterUsuarios.getInstance().getAssinatura(TokenManager.getInstance().getUser(jsonObj.getString("token")))+"\"}";
	}
	
	@PostMapping(path = "usuarioForm")
	public String getUsuarioForm(@RequestBody  String json) throws JSONException, Exception {
		
		JSONObject jsonObj = new JSONObject(json);
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonOut = ow.writeValueAsString(ManterUsuarios.getInstance().getUsuarioForm(TokenManager.getInstance().getUser(jsonObj.getString("token"))));
		
		System.out.println("Get usuarioForm token: "+jsonObj.getString("token"));
		
		return jsonOut;//"{\"body\": \""+"olá"+"\"}";
	}
	

}