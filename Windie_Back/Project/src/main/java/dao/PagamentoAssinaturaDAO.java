package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConexaoBanco;

public class PagamentoAssinaturaDAO {
	
	private static PagamentoAssinaturaDAO instance;
	
	public static PagamentoAssinaturaDAO getInstance() {
		if(instance == null) instance = new PagamentoAssinaturaDAO();
		return instance;
	}
	
	public float getFaturamentoMes(Date mes_referente) throws SQLException {
		String sql = "select sum(valor) as valores from pagamento_assinatura where mes_referente = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setDate(1,mes_referente);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		return rst.getFloat("valores");
	}

}
