package com.tc.windie.controle;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import dao.ArquivoDAO;
import dao.BibliotecaDAO;
import dao.HorasJogadasDAO;
import dao.VW_JogoClassificDAO;
import dao.VW_usuarioAssinaturaDAO;
import modelo.JogoModelo;
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
	
	public byte[] getFiles(int jogo_id,int usuario_id) throws IOException, SQLException, CustomException {
		if(VW_usuarioAssinaturaDAO.getInstance().seAssinante(usuario_id)) {
			JogoModelo jogo = ManterJogos.getInstance().getJogo(jogo_id);
			String path = jogo.getArquivo_caminho();
			return ArquivoDAO.getInstance().getArquivo(path);
		}else {
			throw new CustomException("Usuário não tem uma assinatura válida");
		}
	}
	
	
	public void addHoras(float horas,int jogo_id,int usuario_id) throws SQLException {
		LocalDate data_atual = java.time.LocalDate.now();
		Date mes =  Date.valueOf(data_atual.getYear()+"-"+data_atual.getMonthValue()+"-01");
		HorasJogadasDAO.getInstance().addHorasJOgadasMes(mes, horas, jogo_id);
		BibliotecaDAO.getInstance().addHorasJogadas(jogo_id, usuario_id, horas);
	}
	


}
