package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConexaoBanco;

public class PagamentoAssinaturaDAO {
	
	private static PagamentoAssinaturaDAO instance;
	
	public static PagamentoAssinaturaDAO getInstance() {
		if(instance == null) instance = new PagamentoAssinaturaDAO();
		return instance;
	}
	
	public float getFaturamentoMes(Date mes_referente) throws SQLException {
		String sql = "select sum(valor) as valores from pagamento_assinatura where mes_referente = ? and data_pagamento is not null";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setDate(1,mes_referente);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		return rst.getFloat("valores");
	}
	
	public int inserirPagamento(float valor, Date mes_referente,int usuario_id,String checkoutKey ) throws SQLException {
		String sql = "insert into pagamento_assinatura (valor,mes_referente,usuario_id,checkout_key) VALUES (?,?,?,?) RETURNING pagamento_id";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setFloat(1, valor);
		pst.setDate(2, mes_referente);
		pst.setInt(3, usuario_id);
		pst.setString(4, checkoutKey);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return rst.getInt("pagamento_id");	
	}
	
	public void inserirDataPagamento(Date data_pagamento,int pagamento_id) throws SQLException {
		String sql = "update pagamento_assinatura set data_pagamento = ? where pagamento_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setDate(1, data_pagamento);
		pst.setInt(2, pagamento_id);
		
		pst.execute();
	}
	
	public List<Integer> getPagamentosEmAberto() throws SQLException{
		List<Integer> lista = new ArrayList<Integer>();
		String sql = "select pagamento_id from pagamento_assinatura where data_pagamento is null";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()) {
			lista.add(rst.getInt("pagamento_id"));
		}
		
		return lista;
	}
	
	public List<Integer> getPagamentosEmAberto(int usuario_id) throws SQLException{
		List<Integer> lista = new ArrayList<Integer>();
		String sql = "select pagamento_id from pagamento_assinatura where data_pagamento is null and usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, usuario_id);
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()) {
			lista.add(rst.getInt("pagamento_id"));
		}
		
		return lista;
	}
	
	public Integer getPagamentoEmAberto(int usuario_id) throws SQLException{

		String sql = "select pagamento_id from pagamento_assinatura where data_pagamento is null and usuario_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, usuario_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		return rst.getInt("pagamento_id");
	}
	
	public String getCheckoutKey(int pagamento_id) throws SQLException {
		String sql = "select checkout_key from pagamento_assinatura where pagamento_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, pagamento_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		return rst.getString("checkout_key");
	}
	
	public void removerPagamento(int pagamento_id) throws SQLException {
		String sql = "delete from pagamento_assinatura where pagamento_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, pagamento_id);
		pst.execute();
	}

	public void inserirCheckoutKey(String checkoutKey, int pagamento_id) throws SQLException {
		String sql = "update pagamento_assinatura set checkout_key = ? where pagamento_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setString(1, checkoutKey);
		pst.setInt(2, pagamento_id);
		
		pst.execute();
	}

}
