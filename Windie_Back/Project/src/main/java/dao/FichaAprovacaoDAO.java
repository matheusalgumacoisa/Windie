package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConexaoBanco;

public class FichaAprovacaoDAO {
	private static FichaAprovacaoDAO instance;
	
	public static FichaAprovacaoDAO getInstance() {
		if( instance == null) instance = new FichaAprovacaoDAO();
		return instance;
	}
	
	public int fichaAprovacaoIdByJogo(int jogo_id) throws SQLException {
		String sql = "select ficha_aprovacao_id from ficha_aprovacao where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return rst.getInt(1);
		
	}

}
