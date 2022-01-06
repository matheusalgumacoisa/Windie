package controle;

import java.sql.SQLException;
import java.util.List;

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
	
	public void inserirJogo(JogoModelo jogo,List<byte[]> screenshots) throws Exception {
		if(jogo.getVisibilidade() == null || (!jogo.getVisibilidade().equals("PUBLICO")&&!jogo.getVisibilidade().equals("RASCUNHO"))) {
			throw new Exception();
		}
		jogo.setJogo_id(JogoDAO.getInstance().inserirJogo(jogo)); //insere o jogo e pega o id gerado pela sequence do banco
		for (byte[] bs : screenshots) {
			JogoDAO.getInstance().inserirScreenshot(bs, jogo.getJogo_id()); //insere uma screenshot
		}

	}
	
	public List<byte[]> getScreenshots(int jogo_id) throws SQLException{
		
		return JogoDAO.getInstance().getScreenshots(jogo_id);
	}
	

	

}
