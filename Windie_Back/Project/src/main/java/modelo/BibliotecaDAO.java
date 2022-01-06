package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConexaoBanco;

public class BibliotecaDAO {

	public static BibliotecaDAO instance;
	
	public static BibliotecaDAO getInstance(){
		if(instance == null) {
			instance = new BibliotecaDAO();
		}
		return instance;
	}
	
	public void adicionarJogoNaBiblioteca(int jogo_id, int usuario_id) throws SQLException {
		String sql = "insert into item_biblioteca (jogo_id, usuario_id, horas_jogadas)  values (?,?,0)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, usuario_id);
		pst.execute();
		
	}
	
	public void removerJogoDaBiblioteca(int jogo_id, int usuario_id) throws SQLException {
		String sql = "delete from item_biblioteca where  jogo_id = ? and usuario_id = ? ";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, usuario_id);
		pst.execute();
		
	}
	
	public List<Integer> jogosNaBiblioteca(int usuario_id) throws SQLException{
		
		List<Integer> id_jogos = new ArrayList<>();
		String sql = "select jogo_id from item_biblioteca where usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, usuario_id);
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()) {
			id_jogos.add(rst.getInt(1));
		}
		
		return id_jogos;
	}
	
	public boolean seJogoNaBiblioteca(int jogo_id, int usuario_id) throws SQLException {
		List<Integer> jogos = jogosNaBiblioteca(usuario_id);
		
		for (Integer jogo : jogos) {
			if(jogo == jogo_id) {
				return true;
			}
		}
		
		return false;
	}
}
