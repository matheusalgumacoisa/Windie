package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.UsuarioModelo;
import util.ConexaoBanco;
import util.CustomException;

public class UsuarioDAO {

	private static UsuarioDAO instance;
	
	public static UsuarioDAO getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new UsuarioDAO();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public void inserirUsuario(UsuarioModelo usuario) throws SQLException, CustomException{
		String sql = "insert into usuario (email,senha,apelido)  values(?,?,?)";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);

		 psql.setString(1, usuario.getEmail());
		 psql.setString(2, usuario.getSenha());
		 psql.setString(3, usuario.getApelido());
				
		 psql.executeUpdate();
		
	}
	
	

	public void atualizarUsuario(String email, String apelido) throws SQLException {
		String sql = "update usuario set  apelido = ? where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);

		 psql.setString(1, apelido);
		 psql.setString(2, email);
				
		 psql.executeUpdate();
		
	}
	
	public void atualizarSenha(String email, String senha) throws SQLException {
		String sql = "update usuario set  senha = ? where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);

		 psql.setString(1, senha);
		 psql.setString(2, email);
				
		 psql.executeUpdate();
		
	}
	
	public UsuarioModelo getByEmail(String email) throws SQLException {
		String sql = "select * from usuario where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, email);
		ResultSet rst = psql.executeQuery();
		rst.next();
		return new UsuarioModelo(rst.getInt("usuario_id"),rst.getString("email"),rst.getString("senha"), rst.getString("apelido"));
	}
	
	public int idByEmail(String usuarioEmail ) throws SQLException {
			
		String sql = "select usuario_id from usuario where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, usuarioEmail);
		ResultSet rst = psql.executeQuery();
		rst.next();
		return rst.getInt("usuario_id");
	}
	

	
	public UsuarioModelo getUsuarioByEmail(String email) throws SQLException {
		String sql = "select * from usuario where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, email);
		ResultSet rst = psql.executeQuery();
		rst.next();
		return new UsuarioModelo(rst.getInt("usuario_id"),rst.getString("email"),rst.getString("senha"), rst.getString("apelido"));
	}
	
	public boolean seEmailExiste(String email) throws SQLException {
		String sql = "select * from usuario where email = ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, email);
		ResultSet rst = psql.executeQuery();
		if(rst.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean seEmailExisteParaOutroUsuario(String email,int usuario_id) throws SQLException {
		String sql = "select * from usuario where email = ? and usuario_id != ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, email);
		psql.setInt(2, usuario_id);
		ResultSet rst = psql.executeQuery();
		if(rst.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean seApelidoExiste(String apelido) throws SQLException {
		String sql = "select * from usuario where apelido = ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, apelido);
		ResultSet rst = psql.executeQuery();
		if(rst.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean seApelidoExisteParaOutroUsuario(String apelido,int usuario_id) throws SQLException {
		String sql = "select * from usuario where apelido = ? and usuario_id != ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, apelido);
		psql.setInt(2, usuario_id);
		ResultSet rst = psql.executeQuery();
		if(rst.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean seNomeDesenvolvedorExiste(String nome_de_desenvolvedor) throws SQLException {
		String sql = "select * from desenvolvedor where  nome_de_desenvolvedor = ? ";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1,nome_de_desenvolvedor);
		ResultSet rst = psql.executeQuery();
		if(rst.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean seNomeDesenvolvedorExisteParaOutroDesenvolvedor(String nome_de_desenvolvedor,int desenvolvedor_id) throws SQLException {
		String sql = "select * from desenvolvedor where  nome_de_desenvolvedor = ? and desenvolvedor_id != ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1,nome_de_desenvolvedor);
		psql.setInt(2,desenvolvedor_id);
		ResultSet rst = psql.executeQuery();
		if(rst.next()) {
			return true;
		}else {
			return false;
		}
	}
}



