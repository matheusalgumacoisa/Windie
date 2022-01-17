package controle;

import java.sql.SQLException;
import java.util.List;

import dao.VW_JogoClassificDAO;
import dao.VW_usuarioAssinaturaDAO;
import dao.VotoDAO;
import modelo.VW_JogoClassificModelo;
import util.CustomException;
import util.Ordenacoes;

public class AprovarJogos {
	
	private static AprovarJogos instance;
	
	public static AprovarJogos getInstance() {
		if(instance == null) instance = new AprovarJogos();
		return instance;
	}
	
	public List<VW_JogoClassificModelo> getListaJogos(Ordenacoes ordem, int itens_por_pagina, int pagina) throws SQLException {
		return VW_JogoClassificDAO.getInstance().getListaPaginaAprovacao(ordem, itens_por_pagina, pagina, null);
	}
	
	public List<VW_JogoClassificModelo> buscarJogos(Ordenacoes ordem, int itens_por_pagina, int pagina,String termo_busca) throws SQLException {
		return VW_JogoClassificDAO.getInstance().getListaPaginaAprovacao(ordem, itens_por_pagina, pagina,  termo_busca);
	}
	
	
	public void votar(boolean se_aFavor, int jogo_id, int usuario_id) throws SQLException, CustomException {
		
		if(!VW_usuarioAssinaturaDAO.getInstance().seAssinante(usuario_id)) {
			throw new CustomException("Votos são permmitidos apenas para assinantes");
		}
		
		if(VotoDAO.getInstance().seJaVotou(jogo_id, usuario_id)) {
			VotoDAO.getInstance().atualizarVoto(se_aFavor, jogo_id, usuario_id);
		}else {
			VotoDAO.getInstance().votar(se_aFavor, jogo_id, usuario_id);
		}
		
	}
	
	
	public boolean seVotou(int jogo_id, int usuario_id) throws SQLException {
		return VotoDAO.getInstance().seJaVotou(jogo_id, usuario_id);
	}
	
	public boolean getVoto(int jogo_id, int usuario_id) throws SQLException {
		return VotoDAO.getInstance().getVoto(jogo_id, usuario_id);
	}

}
