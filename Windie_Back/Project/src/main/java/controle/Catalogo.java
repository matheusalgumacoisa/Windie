package controle;

import java.sql.SQLException;
import java.util.List;

import modelo.AvaliacaoDAO;
import modelo.JogoDAO;
import modelo.JogoModelo;
import util.Ordenacoes;

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
	
	public List<JogoModelo> getListaJogos(int num_itens, int page) throws SQLException {
		
		List<JogoModelo> jogos =  JogoDAO.getInstance().getJogoLista(Ordenacoes.popularidade,num_itens,page,null);
		int jogos_numero = JogoDAO.getInstance().getJogosNumero();
			
		for (JogoModelo jogo : jogos) {
			jogo.setAvaliacoes_numero(AvaliacaoDAO.getInstance().getAvaliacoesJogo(jogo.getJogo_id()));
			jogo.setNota(AvaliacaoDAO.getInstance().getNotaJogo(jogo.getJogo_id()));
			jogo.setJogos_numero(jogos_numero);
		}
		return jogos;
	}
	
	public List<JogoModelo> buscaListaJogos(int num_itens, int page, String termo_busca) throws SQLException {
		
		List<JogoModelo> jogos =  JogoDAO.getInstance().getJogoLista(Ordenacoes.popularidade,num_itens,page,termo_busca);
		int jogos_numero = jogos.size();
			
		for (JogoModelo jogo : jogos) {
			jogo.setAvaliacoes_numero(AvaliacaoDAO.getInstance().getAvaliacoesJogo(jogo.getJogo_id()));
			jogo.setNota(AvaliacaoDAO.getInstance().getNotaJogo(jogo.getJogo_id()));
			jogo.setJogos_numero(jogos_numero);
		}
		return jogos;
	}

}
