package controle;

import java.sql.SQLException;
import java.util.List;

import dao.BibliotecaDAO;
import dao.GeneroDAO;
import dao.UsuarioDAO;
import dao.VW_JogoClassificDAO;
import modelo.GeneroModelo;
import modelo.VW_JogoClassificModelo;
import util.CustomException;
import util.Ordenacoes;

public class ManterCatalogo {
	
	private static ManterCatalogo instance;
	

	public static ManterCatalogo getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new ManterCatalogo();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public List<VW_JogoClassificModelo> getListaJogos(int num_itens, int page, Ordenacoes ordenacao) throws SQLException, CustomException {
		
		
		List<VW_JogoClassificModelo> jogos = VW_JogoClassificDAO.getInstance().getListaPaginaCatalogo(ordenacao,num_itens,page,null);
		return jogos;
	}
	
	public List<VW_JogoClassificModelo> buscarJogos(int num_itens, int page, String termo_busca, Ordenacoes ordenacao) throws SQLException {
		
		List<VW_JogoClassificModelo> jogos = VW_JogoClassificDAO.getInstance().getListaPaginaCatalogo(ordenacao,num_itens,page,termo_busca);
		return jogos;
		
	}
	
	public VW_JogoClassificModelo getJogo(int jogo_id) throws SQLException {
		
		VW_JogoClassificModelo jogo = VW_JogoClassificDAO.getInstance().getJogo(jogo_id);		
		return jogo;
	}
	
	public boolean seJogoNaBiblioteca(int jogo_id, int usuario_id) throws SQLException {
		return BibliotecaDAO.getInstance().seJogoNaBiblioteca(jogo_id, usuario_id);
	}
	
	public void inserirJogoBiblioteca(int jogo_id, int usuario_id) throws SQLException {
		 BibliotecaDAO.getInstance().inserirJogoNaBiblioteca(jogo_id, usuario_id);
	}
	
	public void removerJogoBiblioteca(int jogo_id, int usuario_id) throws SQLException {
		 BibliotecaDAO.getInstance().removerJogoDaBiblioteca(jogo_id, usuario_id);
	}
	
	
	public List<GeneroModelo> getListaGeneros() throws SQLException{
		return GeneroDAO.getInstance().getLista();
	}
	
	public int idUsuarioByEmail(String email) throws SQLException {

		int id = UsuarioDAO.getInstance().idByEmail(email);
		return id;
	}

}
