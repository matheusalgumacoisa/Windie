package apiVisao;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import controle.Catalogo;
import controle.ManterUsuarios;
import modelo.JogoModelo;
import util.TokenManager;

@RequestMapping("/catalogo")
@RestController
public class VisaoCatalogo {
	
	
	@PostMapping(path = "jogos")
	public String JogoModelo (@RequestBody  String json) throws SQLException, JsonProcessingException {
		System.out.println("Json :"+json);
		JSONObject jsonObj = new JSONObject(json);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return "{\"body\": "+ ow.writeValueAsString(Catalogo.getInstance().getListaJogos(jsonObj.getInt("num_itens") , jsonObj.getInt("page")))+"}";
	}
	
	@PostMapping(path = "buscar")
	public String JogoBusca (@RequestBody  String json) throws SQLException, JsonProcessingException {
		System.out.println("Json :"+json);
		JSONObject jsonObj = new JSONObject(json);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return "{\"body\": "+ ow.writeValueAsString(Catalogo.getInstance().buscaListaJogos(jsonObj.getInt("num_itens") , jsonObj.getInt("page"),jsonObj.getString("termo")))+"}";
	}
	
	@PostMapping(path = "jogo")
	public String getJogo (@RequestBody  String json) throws SQLException, JsonProcessingException {
		System.out.println("Get jogo Json :"+json);
		JSONObject jsonObj = new JSONObject(json);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return "{\"body\": "+ ow.writeValueAsString(Catalogo.getInstance().getJogo(jsonObj.getInt("jogo_id")))+"}";
	}
	
	@PostMapping(path = "seJogoBiblioteca")
	public String seJogoBiblioteca (@RequestBody  String json) throws JSONException, Exception {
		System.out.println("seJogoBiblioteca Json :"+json);
		JSONObject jsonObj = new JSONObject(json);
		int usuario_id = ManterUsuarios.getInstance().idByEmail(TokenManager.getInstance().getUser(jsonObj.getString("token")));
		return "{\"body\": "+"{\"seJogoBiblioteca\": "+ Catalogo.getInstance().seJogoNaBiblioteca(jsonObj.getInt("jogo_id"),usuario_id)+"}"+"}";
	}
	
	@PostMapping(path = "adicionarNaBiblioteca")
	public void adicionarNaBiblioteca  (@RequestBody  String json) throws JSONException, Exception {
		System.out.println("adicionarNaBiblioteca Json :"+json);
		JSONObject jsonObj = new JSONObject(json);
		int usuario_id = ManterUsuarios.getInstance().idByEmail(TokenManager.getInstance().getUser(jsonObj.getString("token")));
		Catalogo.getInstance().adicionarJogoBiblioteca(jsonObj.getInt("jogo_id"),usuario_id);
	}
	@PostMapping(path = "removerDaBiblioteca")
	public void removerDaBiblioteca  (@RequestBody  String json) throws JSONException, Exception {
		System.out.println("removerDaBiblioteca Json :"+json);
		JSONObject jsonObj = new JSONObject(json);
		int usuario_id = ManterUsuarios.getInstance().idByEmail(TokenManager.getInstance().getUser(jsonObj.getString("token")));
		Catalogo.getInstance().removerJogoBiblioteca(jsonObj.getInt("jogo_id"),usuario_id);
	}

}
