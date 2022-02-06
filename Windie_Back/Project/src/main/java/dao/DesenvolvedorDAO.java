package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.DesenvolvedorModelo;
import util.ConexaoBanco;

public class DesenvolvedorDAO {

	private static DesenvolvedorDAO instance;
	
	public static DesenvolvedorDAO getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new DesenvolvedorDAO();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public void inserirDesenvolvedor(DesenvolvedorModelo desenvolvedor) throws SQLException{
		String sql = "insert into desenvolvedor (nome_de_desenvolvedor,email_paypal,usuario_id)  values(?,?,?)";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);

		 psql.setString(1, desenvolvedor.getNome_de_desenvolvedor());
		 psql.setString(2, desenvolvedor.getEmail_paypal());
		 psql.setInt(3, desenvolvedor.getUsuario_id());
				
			psql.executeUpdate();
		
	}
	
	public void atualizarDesenvolvedor(String nome_desenvolvedor, String email_paypal,int usuarioId) throws SQLException {
		
		String sql = "update desenvolvedor set  nome_de_desenvolvedor = ?, email_paypal = ? where usuario_id = ?";
		
		PreparedStatement psql2 = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		psql2.setString(1, nome_desenvolvedor);
		psql2.setString(2, email_paypal);
		psql2.setInt(3, usuarioId);
		
		psql2.executeUpdate();
	}
	
	public DesenvolvedorModelo getByUser(int usuario_id) throws SQLException { //pega um desenvolvedor por id de usuário
		
		String sql = "select * from desenvolvedor where usuario_id = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, usuario_id);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return new DesenvolvedorModelo(rst.getInt("desenvolvedor_id"),rst.getString("nome_de_desenvolvedor"), rst.getString("email_paypal"), rst.getInt("usuario_id"));
		
	}

	
	public boolean seUsuarioDesenvolvedor(int usuario_id) throws SQLException {
		String sql = "select nome_de_desenvolvedor from desenvolvedor where usuario_id = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, usuario_id);
		ResultSet rst = psql.executeQuery();
		
		if(rst.next()) {
			
			return true;
		}
		return false;
	}
	


	public boolean seNomeExiste(String nome_desenvolvedor) throws SQLException {
			String sql = "select * from desenvolvedor where nome_de_desenvolvedor = ? ";
			PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
			psql.setString(1, nome_desenvolvedor);
			ResultSet rst = psql.executeQuery();
			if(rst.next()) {
				return true;
			}else {
				return false;
			}
	}
	
	
	public List<Integer> getListIds() throws SQLException{
		String sql = "select desenvolvedor_id from desenvolvedor";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = pst.executeQuery();
		
		List<Integer> ids = new ArrayList<>();
		
		while(rst.next()) {
			ids.add(rst.getInt("desenvolvedor_id"));
		}
		
		return ids;
	}

	public String getPaypal(int desenvolvedor_id) throws SQLException {
		String sql = "select email_paypal from desenvolvedor where desenvolvedor_id = ? ";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return rst.getString("email_paypal");
	}
}
