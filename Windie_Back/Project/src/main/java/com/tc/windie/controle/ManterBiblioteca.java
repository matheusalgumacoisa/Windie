package com.tc.windie.controle;

import java.sql.SQLException;
import java.util.List;

import dao.VW_JogoClassificDAO;
import modelo.VW_JogoClassificModelo;
import util.CustomException;
import util.Ordenacoes;

public class ManterBiblioteca {
	
	private static ManterBiblioteca instance;
	
	public static ManterBiblioteca getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new ManterBiblioteca();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public List<VW_JogoClassificModelo> getListaJogos(int num_itens, int page, Ordenacoes ordenacao,int usuario_id) throws SQLException, CustomException {
		
		
		List<VW_JogoClassificModelo> jogos = VW_JogoClassificDAO.getInstance().getListaPaginaBiblioteca(ordenacao,num_itens,page,null, usuario_id);
		return jogos;
	}
	 
	public List<VW_JogoClassificModelo> buscarJogos(int num_itens, int page, String termo_busca, Ordenacoes ordenacao,int usuario_id) throws SQLException {
		
		List<VW_JogoClassificModelo> jogos = VW_JogoClassificDAO.getInstance().getListaPaginaBiblioteca(ordenacao,num_itens,page,termo_busca, usuario_id);
		return jogos;
		
	}

}
