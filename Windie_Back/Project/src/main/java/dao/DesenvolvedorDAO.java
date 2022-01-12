package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		String sql = "insert into desenvolvedor (nome_de_desenvolvedor,agencia_bancaria,conta_bancaria,usuario_id)  values(?,?,?,?)";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);

		 psql.setString(1, desenvolvedor.getNome_de_desenvolvedor());
		 psql.setInt(2, desenvolvedor.getAgencia_bancaria());
		 psql.setInt(3, desenvolvedor.getConta_bancaria());
		 psql.setInt(4, desenvolvedor.getUsuario_id());
				
			psql.executeUpdate();
		
	}
	
	public void atualizarDesenvolvedor(String nome_desenvolvedor, int conta, int agencia,int usuarioId) throws SQLException {
		
		String sql = "update desenvolvedor set  nome_de_desenvolvedor = ?, agencia_bancaria = ?, conta_bancaria = ? where usuario_id = ?";
		
		PreparedStatement psql2 = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		psql2.setString(1, nome_desenvolvedor);
		psql2.setInt(2, agencia);
		psql2.setInt(3, conta);
		psql2.setInt(4, usuarioId);
		
		psql2.executeUpdate();
	}
	
	public DesenvolvedorModelo getByUser(int usuario_id) throws SQLException { //pega um desenvolvedor por id de usuário
		
		String sql = "select * from desenvolvedor where usuario_id = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, usuario_id);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return new DesenvolvedorModelo(rst.getInt("desenvolvedor_id"),rst.getString("nome_de_desenvolvedor"), rst.getInt("agencia_bancaria"), rst.getInt("conta_bancaria"), rst.getInt("usuario_id"));
		
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
}
