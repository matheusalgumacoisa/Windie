package controle;

import java.sql.SQLException;

import modelo.JogoDAO;
import modelo.JogoModelo;

public class ManterJogos {
	
	public static ManterJogos instance;
	
	public static ManterJogos getInstance () {
		if(instance == null) {
			instance = new ManterJogos();
		}
		
		return instance;
	}
	
	public void inserirJogo(JogoModelo jogo) throws Exception {
		if(jogo.getVisibilidade() == null || (!jogo.getVisibilidade().equals("PUBLICO")&&!jogo.getVisibilidade().equals("RASCUNHO"))) {
			throw new Exception();
		}
		JogoDAO.getInstance().inserirJogo(jogo);
	}
	

}
