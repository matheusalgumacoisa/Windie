package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.AvaliacaoModelo;
import util.ConexaoBanco;

public class AvaliacaoDAO {
	
	private static AvaliacaoDAO instance;
	
	public static AvaliacaoDAO getInstance() { //singleton
		if(instance == null) {
			
			return new AvaliacaoDAO();
		}else {
			return instance;
		}
	}
	
	public int getNumAvaliacoesJogo(int jogoID) throws SQLException { //pega a quantidade de avaliações que foram feitas para o dado jogo
		String sql = "select count(*) as avaliacoes from avaliacao where jogo_id = ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, jogoID);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt("avaliacoes");
	}
	
	public float getNotaJogo(int jogoID) throws SQLException { //pega a média entre todas as notas dadas em avaliações para o dado jogo
		String sql = "select coalesce(sum(nota)/count(nota)) as nota from avaliacao where jogo_id = ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, jogoID);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getFloat("nota");
		
	}
	
	public boolean seAvaliouJogo(int jogo_id,int usuario_id) throws SQLException {
		String sql = "select count(*) as ocorrencias from avaliacao where jogo_id = ? and usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, usuario_id);
		
		ResultSet rst = pst.executeQuery();
		
		int ocorrencias = rst.getInt("ocorrencias");
		
		if(ocorrencias>0) {
			return true;
		}else {
			return false;
		}	
	}
	
	public void atualizarAvaliacao(AvaliacaoModelo novaAvaliacao) throws SQLException {
		
		String sql = "update avaliacao set nota = ?,jogo_id =?,usuario_id =? where avaliacao_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		pst.setInt(1, novaAvaliacao.getNota());
		pst.setInt(2, novaAvaliacao.getJogo_id());
		pst.setInt(3, novaAvaliacao.getUsuario_id());
		pst.setInt(4, novaAvaliacao.getAvaliacao_id());
		
		pst.execute();
		
	}
	
	public void criarAvaliacao(AvaliacaoModelo novaAvaliacao) throws SQLException {
		String sql = "insert into avaliacao (nota,jogo_id,usuario_id) values(?,?,?)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		pst.setInt(1, novaAvaliacao.getNota());
		pst.setInt(2, novaAvaliacao.getJogo_id());
		pst.setInt(3, novaAvaliacao.getUsuario_id());
		
		pst.execute();
	}
	
	public AvaliacaoModelo getAvaliacao(int jogo_id,int usuario_id) throws SQLException {
		String sql = "select * from avaliacao where jogo_id = ? and usuario_id = ? ";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		pst.setInt(1, jogo_id);
		pst.setInt(1, usuario_id);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return new AvaliacaoModelo(rst.getInt("avaliacao_id"),rst.getInt("nota"),rst.getInt("jogo_id"),rst.getInt("usuario_id"));
		
			
	}

}
