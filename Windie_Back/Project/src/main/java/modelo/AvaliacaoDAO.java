package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConexaoBanco;

public class AvaliacaoDAO {
	
	private static AvaliacaoDAO instance;
	
	public static AvaliacaoDAO getInstance() {
		if(instance == null) {
			
			return new AvaliacaoDAO();
		}else {
			return instance;
		}
	}
	
	public int getAvaliacoesJogo(int jogoID) throws SQLException {
		String sql = "select count(*) as avaliacoes from avaliacao where jogo_id = ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, jogoID);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt("avaliacoes");
	}
	
	public float getNotaJogo(int jogoID) throws SQLException {
		String sql = "select coalesce(sum(nota)/count(nota)) as nota from avaliacao where jogo_id = ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, jogoID);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getFloat("nota");
		
	}

}
