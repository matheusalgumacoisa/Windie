package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConexaoBanco;

public class ScreenshotDAO {
	
	private static ScreenshotDAO instance;
	
	public static ScreenshotDAO getInstance() {
		if(instance == null ) {
			instance = new ScreenshotDAO();
		}
		
		return instance;
	}
	
	
	public void inserirScreenshot(byte[] imagem, int jogo_id) throws SQLException {
		String sql = "insert into screenshot (imagem, jogo_id)\r\n"
				+ "VALUES (?, ?)";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setBytes(1, imagem);
		pst.setInt(2, jogo_id);
		
		pst.execute();
	}
	
	public List<byte[]> getListaByJogo(int jogo_id) throws SQLException {
		List<byte[]> screenshots = new ArrayList<>();
		String sql = "select imagem from screenshot where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		ResultSet rst = pst.executeQuery();
		while(rst.next()) {
			screenshots.add(rst.getBytes(1));			
		}
		
		return screenshots;
		
	}



	public void limparJogoScreenshots(int jogo_id) throws SQLException {
		String sql = "delete from screenshot where jogo_id = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, jogo_id);
		
		psql.execute();
		
	}

}
