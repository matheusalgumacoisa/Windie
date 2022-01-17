package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConexaoBanco;
import util.Debug;

public class VotoDAO {
	
	private static VotoDAO instance;
	
	public static VotoDAO getInstance() {
		if(instance==null) instance = new VotoDAO();
		return instance;
	}
	
	
	public void atualizarVoto(boolean se_aFavor, int jogo_id, int usuario_id) throws SQLException {
		int ficha_aprovacao_id = FichaAprovacaoDAO.getInstance().fichaAprovacaoIdByJogo(jogo_id);
		String se_favoravel;
		
		if(se_aFavor) {
			se_favoravel = "S";
		}else {
			se_favoravel = "N";
		}
		
		String sql = "update voto set se_favoravel = ? where usuario_id = ? and ficha_aprovacao_id = ? ";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setString(1, se_favoravel);
		pst.setInt(2, usuario_id);
		pst.setInt(3, ficha_aprovacao_id);
		pst.execute();
		
	}
	
	public void votar(boolean se_aFavor, int jogo_id, int usuario_id) throws SQLException {
		int ficha_aprovacao_id = FichaAprovacaoDAO.getInstance().fichaAprovacaoIdByJogo(jogo_id);
		String se_favoravel;
		
		if(se_aFavor) {
			se_favoravel = "S";
		}else {
			se_favoravel = "N";
		}
		
		String sql = "insert into voto (se_favoravel,usuario_id,ficha_aprovacao_id) values (?,?,?)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setString(1, se_favoravel);
		pst.setInt(2, usuario_id);
		pst.setInt(3, ficha_aprovacao_id);
		pst.execute();
		
	}
	
	
	public boolean seJaVotou(int jogo_id, int usuario_id) throws SQLException {
		int ficha_aprovacao_id = FichaAprovacaoDAO.getInstance().fichaAprovacaoIdByJogo(jogo_id);
		
		String sql = "select count(*) votos from voto where ficha_aprovacao_id = ? and usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, ficha_aprovacao_id);
		pst.setInt(2, usuario_id);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		if(rst.getInt("votos")>0) {
			return true;
		}else {
			return false;
		}
		

		
	}
	
	public boolean getVoto(int jogo_id, int usuario_id) throws SQLException {
		int ficha_aprovacao_id = FichaAprovacaoDAO.getInstance().fichaAprovacaoIdByJogo(jogo_id);
		
		String sql = "select se_favoravel from  voto where ficha_aprovacao_id = ? and usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, ficha_aprovacao_id);
		pst.setInt(2, usuario_id);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		String voto = rst.getString("se_favoravel");
		Debug.logDetalhe("voto: "+voto);
		if(voto.equals("S")) {
			return true;
		}else {
			
			return false;
		}
		
	}
	

	

}
