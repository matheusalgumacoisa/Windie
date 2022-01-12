package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConexaoBanco;

public class VW_usuarioAssinaturaDAO {
	private static VW_usuarioAssinaturaDAO instance;
	
	public static VW_usuarioAssinaturaDAO getInstance() {
		if(instance == null) {
			instance = new VW_usuarioAssinaturaDAO();
		}
		
		return instance;
	}
	
	
	public boolean seAssinante(int usuario_id) throws SQLException {
		String sql = "select assinatura_status from view_usuario_assinatura where usuario_id = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, usuario_id);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		if(rst.getString(1).equals("VALIDA")) {
			
			return true;
		}
		return false;
	}

}
