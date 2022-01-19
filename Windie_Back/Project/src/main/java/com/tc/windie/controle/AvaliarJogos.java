package com.tc.windie.controle;

import java.sql.SQLException;

import dao.AvaliacaoDAO;
import dao.BibliotecaDAO;
import modelo.AvaliacaoModelo;
import util.CustomException;

public class AvaliarJogos {
	
	
	private static AvaliarJogos instance;
	
	public static AvaliarJogos getInstance(){
		if(instance==null) return new AvaliarJogos();
		return instance;
	}
	
	public void Avaliar(int usuario_id,int jogo_id,int nota) throws SQLException, CustomException {
		
		if(nota <=0 || nota >5) {
			throw new CustomException("A nota deve estar entre 1 e 5");
		}
		
		if(AvaliacaoDAO.getInstance().seAvaliouJogo(jogo_id, usuario_id)) {
			AvaliacaoModelo novaAvaliacao = AvaliacaoDAO.getInstance().getAvaliacao(jogo_id, usuario_id);
			novaAvaliacao.setNota(nota);
			AvaliacaoDAO.getInstance().atualizarAvaliacao(novaAvaliacao);
		}else {
			AvaliacaoModelo novaAvaliacao = new AvaliacaoModelo(nota,jogo_id,usuario_id);
			AvaliacaoDAO.getInstance().criarAvaliacao(novaAvaliacao);
		}
		
	}
	
	public AvaliacaoModelo getAvaliacao(int usuario_id,int jogo_id) throws SQLException {//caso não tenha avaliado o jogo ainda o retorno da nota é 0
		if(AvaliacaoDAO.getInstance().seAvaliouJogo(jogo_id, usuario_id)) {
			return AvaliacaoDAO.getInstance().getAvaliacao(jogo_id, usuario_id);
		}else {
			return new AvaliacaoModelo(0, jogo_id, usuario_id);
		}
	}
	
	public float getHorasJogadas(int jogo_id, int usuario_id) throws SQLException {
		return BibliotecaDAO.getInstance().getHorasJogadas(jogo_id, usuario_id);
	}
	
	

}
