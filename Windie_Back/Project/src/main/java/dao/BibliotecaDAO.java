package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConexaoBanco;
import util.Debug;

public class BibliotecaDAO {

	public static BibliotecaDAO instance;
	
	public static BibliotecaDAO getInstance(){
		if(instance == null) {
			instance = new BibliotecaDAO();
		}
		return instance;
	}
	
	public void inserirJogoNaBiblioteca(int jogo_id, int usuario_id) throws SQLException { //adiciona um dado jogo de um dado usuario à sua biblioteca
		String sql = "insert into item_biblioteca (jogo_id, usuario_id, horas_jogadas)  values (?,?,0)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, usuario_id);
		pst.execute();
		
	}
	
	public void removerJogoDaBiblioteca(int jogo_id, int usuario_id) throws SQLException { //remove um dado jogo de um dado usuario da sua biblioteca
		String sql = "delete from item_biblioteca where  jogo_id = ? and usuario_id = ? ";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, usuario_id);
		pst.execute();
		
	}
	
	public List<Integer> getJogosDaBiblioteca(int usuario_id) throws SQLException{ //retorna uma lista de jogo_id na biblioteca de um dado usuário
		
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
	
	public boolean seJogoNaBiblioteca(int jogo_id, int usuario_id) throws SQLException { // retorna true se dado jogo esta presente na biblioteca de dado usuário
		List<Integer> jogos = getJogosDaBiblioteca(usuario_id);
		Debug.logDetalhe("jogo_id: "+jogo_id+"usuario_id: "+usuario_id);
		for (Integer jogo : jogos) {
			if(jogo == jogo_id) {
				return true;
			}
		}
		
		return false;
	}
	
	public float getHorasJogadas(int jogo_id, int usuario_id) throws SQLException {
		if(!seJogoNaBiblioteca(jogo_id, usuario_id)) { return 0;}
		String sql = "select horas_jogadas from item_biblioteca where jogo_id = ? and usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, usuario_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return rst.getFloat("horas_jogadas");
	}
	
	public void addHorasJogadas(int jogo_id, int usuario_id,float horas) throws SQLException{
		if(!seJogoNaBiblioteca(jogo_id, usuario_id)) { inserirJogoNaBiblioteca(jogo_id, usuario_id); }
		if(horas>100) { horas = 100;}
		horas = getHorasJogadas(jogo_id, usuario_id) + horas;
		String sql = "update item_biblioteca set horas_jogadas = ? where jogo_id = ? and usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setFloat(1,horas);
		pst.setInt(2, jogo_id);
		pst.setInt(3, usuario_id);
		
		pst.execute();
	}
}
