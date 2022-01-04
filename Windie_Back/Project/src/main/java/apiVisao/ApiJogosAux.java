package apiVisao;

import java.sql.SQLException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import controle.ManterGenero;

@RequestMapping("/jogos")
@RestController
public class ApiJogosAux {
	
	
	@GetMapping(path = "generos")
	public String getListaGeneros() throws SQLException, JsonProcessingException{
		
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonOut = ow.writeValueAsString(ManterGenero.getInstance().getListaGeneros());
		
		
		return "{\"id\":\"0\", "
				+ "\"title\":\"generos\","
				+ "\"body\": "+jsonOut+"}";
		
		//return ManterGenero.getInstance().getListaGeneros();
	}

}
