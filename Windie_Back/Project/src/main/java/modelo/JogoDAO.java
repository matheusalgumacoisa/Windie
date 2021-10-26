package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConexaoBanco;

public class JogoDAO {

	
	private static JogoDAO instance;
	

	public static JogoDAO getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new JogoDAO();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public List<JogoModelo> getJogoLista() throws SQLException {
		String sql = "select * from jogo";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = psql.executeQuery();
		
		List<JogoModelo> lista = new ArrayList<>();
		
		while(rst.next()) {
			
			lista.add(new JogoModelo(rst.getInt("jogo_id"),rst.getString("titulo") , rst.getString("descricao"), 
					rst.getString("caminho_executavel"), rst.getString("detalhes"), rst.getString("tags"), 
					rst.getString("visibilidade"), rst.getBytes("imagem_capa"), rst.getInt("genero")));
		}
		
		return lista;
	}
	
	
	public UsuarioModelo getUsuarioByEmail(String email) throws SQLException {
		String sql = "select * from usuario where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, email);
		ResultSet rst = psql.executeQuery();
		rst.next();
		return new UsuarioModelo(rst.getInt("usuario_id"),rst.getString("email"),rst.getString("senha"), rst.getString("apelido"));
	}
	
}
