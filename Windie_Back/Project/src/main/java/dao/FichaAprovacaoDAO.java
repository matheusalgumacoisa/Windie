package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConexaoBanco;
import util.CustomException;
import util.Debug;

public class FichaAprovacaoDAO {
	private static FichaAprovacaoDAO instance;
	
	public static FichaAprovacaoDAO getInstance() {
		if( instance == null) instance = new FichaAprovacaoDAO();
		return instance;
	}
	
	public int fichaAprovacaoIdByJogo(int jogo_id) throws SQLException, CustomException {
		String sql = "select ficha_aprovacao_id from ficha_aprovacao where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		ResultSet rst = pst.executeQuery();
		
		if(rst.next()) {
		
		return rst.getInt(1);
		}else {
			throw new CustomException("jogo não possui ficha de aprovação");
		}
		
	}
	
	public void inserir(int jogo_id) throws SQLException {
		Debug.logDetalhe("inserindo ficha de aprovação");
		String sql = "insert into ficha_aprovacao (data_inicio,estado,jogo_id)  values(current_date,?,?)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setString(1, "ABERTO");
		pst.setInt(2, jogo_id);
		
		pst.execute();
	}
	
	public boolean sePossuiFichaAprovacao(int jogo_id) throws SQLException, CustomException {
		String sql = "select ficha_aprovacao_id from ficha_aprovacao where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		ResultSet rst = pst.executeQuery();
		
		if(rst.next()) {
		
		return true;
		}else {
			return false;
		}
		
	}

}
