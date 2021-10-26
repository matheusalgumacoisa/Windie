package controle;

import java.sql.SQLException;
import java.util.List;

import modelo.JogoDAO;
import modelo.JogoModelo;

public class Catalogo {
	
	private static Catalogo instance;
	

	public static Catalogo getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new Catalogo();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public List<JogoModelo> getListaJogos() throws SQLException {
		
		return JogoDAO.getInstance().getJogoLista();
	}

}
