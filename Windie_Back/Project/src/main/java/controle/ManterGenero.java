package controle;

import java.sql.SQLException;
import java.util.List;

import modelo.GeneroDAO;
import modelo.GeneroModelo;

public class ManterGenero {

	public static ManterGenero instance;
	
	public static ManterGenero getInstance() {
		if(instance == null) {
			instance = new ManterGenero();
		}
		return instance;
	}
	
	public List<GeneroModelo> getListaGeneros() throws SQLException{
		return GeneroDAO.getInstance().getGenerosLista();
	}
}
