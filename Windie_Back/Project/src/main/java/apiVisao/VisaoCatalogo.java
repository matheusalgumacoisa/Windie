package apiVisao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
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
	
	
	@GetMapping(path = "jogos")
	public String JogoModelo () throws SQLException, JsonProcessingException {
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return "{\"body\": "+ ow.writeValueAsString(Catalogo.getInstance().getListaJogos())+"}";
	}

}
