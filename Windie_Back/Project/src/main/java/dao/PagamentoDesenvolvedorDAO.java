package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConexaoBanco;
import util.CustomException;

public class PagamentoDesenvolvedorDAO {
	
	private static PagamentoDesenvolvedorDAO instance;
	
	public static PagamentoDesenvolvedorDAO getInstance() {
		if(instance == null) instance = new PagamentoDesenvolvedorDAO();
		return instance;
	}
	
	
	public float getValorPagoMes(int desenvolvedor_id,Date mes_referente) throws SQLException, CustomException {
		String sql = "select valor from  pagamento_desenvolvedor where mes_referente = ? and desenvolvedor_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setDate(1,mes_referente);
		pst.setInt(2, desenvolvedor_id);
		
		ResultSet rst = pst.executeQuery();
		if(rst.next()) {
		
			return rst.getFloat("valor");
		}else {
			throw new CustomException("Sem pagamentos para esse desenvolvedor nesse mês ");
		}
		
	}
	
	
	public boolean seRecebeuMes(int desenvolvedor_id,Date mes_referente) throws SQLException, CustomException {
		String sql = "select valor from  pagamento_desenvolvedor where mes_referente = ? and desenvolvedor_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setDate(1,mes_referente);
		pst.setInt(2, desenvolvedor_id);
		
		ResultSet rst = pst.executeQuery();
		if(rst.next()) {
		
			return true;
		}else {
			return false;
		}
		
	}
	
	public void inserirPagamento(float valor,Date mes_referente,int desenvolvedor_id) throws SQLException {
		String sql = "insert into pagamento_desenvolvedor (valor,mes_referente,desenvolvedor_id) values  (?,?,?)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setFloat(1, valor);
		pst.setDate(2, mes_referente);
		pst.setInt(3, desenvolvedor_id);
		
		pst.execute();
	}
	
	public void inserirDataDePagamento(int desenvolvedor_id,Date mes_referente, Date data_pagamento) throws SQLException {
		String sql = "update pagamento_desenvolvedor set data = ? where mes_referente = ? and desenvolvedor_id = ? ";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setDate(1, data_pagamento);
		pst.setDate(2, mes_referente);
		pst.setInt(3, desenvolvedor_id);
		
		pst.execute();
	}
	
	public int getNewBatchID() throws SQLException {
		String sql = "select nextval('pagamento_desenvolvedor_pag_batch_id_seq'::regclass)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		return rst.getInt("nextval");
	}


	public void inserirBatchId(int desenvolvedor_id, Date mes_referente, int batch_id) throws SQLException {
		String sql = "update pagamento_desenvolvedor set pag_batch_id = ? where mes_referente = ? and desenvolvedor_id = ? ";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, batch_id);
		pst.setDate(2, mes_referente);
		pst.setInt(3, desenvolvedor_id);
		
		pst.execute();
		
	}

}
