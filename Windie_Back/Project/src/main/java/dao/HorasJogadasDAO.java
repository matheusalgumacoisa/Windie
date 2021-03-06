package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.Horas_jogadas_mesModelo;
import util.ConexaoBanco;

public class HorasJogadasDAO {
	
	private static HorasJogadasDAO instance;
	
	public static HorasJogadasDAO getInstance() {
		if(instance==null) instance = new HorasJogadasDAO();
		return instance;
	}
	
	public Horas_jogadas_mesModelo getHorasJogadasMesJogo(int jogo_id,Date mes) throws SQLException {
		String sql = "select * from horas_jogadas_mes where jogo_id = ? and mes_referente = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setDate(2, mes);
		
		ResultSet rst = pst.executeQuery();
		if(rst.next()) {
			return new Horas_jogadas_mesModelo(rst.getFloat("horas"), rst.getDate("mes_referente"), rst.getInt("jogo_id"), rst.getInt("horas_jogadas_mes_id"));
		}else {
			return new Horas_jogadas_mesModelo(0, mes, jogo_id, 0);
		}
	}
	
	
	public float getHorasJogadasMesTotal(Date mes) throws SQLException {
		String sql = "select sum(horas) as horas from horas_jogadas_mes where mes_referente = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setDate(1, mes);
		
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return rst.getFloat("horas");
	}
	
	public void addHorasJOgadasMes(Date mes,float horas,int jogo_id) throws SQLException {
		String sql = "select * from horas_jogadas_mes where jogo_id = ? and mes_referente = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setDate(2, mes);
		
		ResultSet rst = pst.executeQuery();
		
		if(rst.next()) {
			pst.close();
			sql = "update horas_jogadas_mes set horas = ? where jogo_id = ? and mes_referente = ?";
			pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
			pst.setFloat(1, horas);
			pst.setInt(2, jogo_id);
			pst.setDate(3, mes);
			
			pst.execute();
			
		}else {
			pst.close();
			sql = "insert into  horas_jogadas_mes (horas,mes_referente,jogo_id)  values(?,?,?)";
			pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
			pst.setFloat(1, horas);
			pst.setDate(2, mes);
			pst.setInt(3, jogo_id);
			
			pst.execute();
			
		}
		
	}
	
	
	
	

}
