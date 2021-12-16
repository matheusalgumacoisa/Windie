package apiVisao;

import java.sql.SQLException;
import java.util.List;

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
import modelo.JogoModelo;

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

}
