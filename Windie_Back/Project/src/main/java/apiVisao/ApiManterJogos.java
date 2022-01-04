package apiVisao;

import java.sql.SQLException;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import controle.ManterJogos;
import controle.ManterUsuarios;
import modelo.JogoModelo;
import util.TokenManager;

@RequestMapping("/mjogos")
@RestController
public class ApiManterJogos {

	@PostMapping(path = "novo")
	public void novoJogo(@RequestBody String json) throws Exception {
		System.out.println("Novo jogo a ser inserido, Json :"+json);
		JSONObject jsonObj = new JSONObject(json);
		byte[] bArray= Base64.getDecoder().decode(jsonObj.getString("imagem_capa").substring(jsonObj.getString("imagem_capa").indexOf(",")+1));// a substring aqui esta pegando somente o binario e descartando os metadados
		JogoModelo novoJogoModelo = new JogoModelo(0, 
													jsonObj.getString("titulo"), 
													jsonObj.getString("descricao"), 
													jsonObj.getString("caminho_executavel"), 
													jsonObj.getString("detalhes"), 
													jsonObj.getString("tags"), 
													jsonObj.getString("visibilidade"), 
													 bArray, 
													jsonObj.getInt("genero"));
		System.out.println("token : "+jsonObj.getString("token"));
		int user_id = ManterUsuarios.getInstance().idByEmail( TokenManager.getInstance().getUser(jsonObj.getString("token")));
		int dev_id = ManterUsuarios.getInstance().devByUser(user_id).getDesenvolvedor_id();
		novoJogoModelo.setDesenvolvedor_id(dev_id);
		
		ManterJogos.getInstance().inserirJogo(novoJogoModelo);
	}
}
