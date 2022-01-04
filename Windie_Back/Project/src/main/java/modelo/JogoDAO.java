package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConexaoBanco;
import util.Ordenacoes;

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
	
	public List<JogoModelo> getJogoLista(Ordenacoes ordem,int num_itens, int page, String termo_busca) throws SQLException {
		
		int limit = num_itens;
		int offset = (num_itens*page)-num_itens;
		String sql = "";
		if(ordem == Ordenacoes.popularidade) {
			sql = "select j.jogo_id,j.titulo,j.descricao,j.caminho_executavel,j.detalhes,j.tags,j.visibilidade,j.imagem_capa,j.genero,j.desenvolvedor_id \r\n"
					+ "from jogo j \r\n"
					+ "left join avaliacao av\r\n"
					+ "on av.jogo_id = j.jogo_id\r\n";
			if(termo_busca != null) sql = sql	+ "where  lower(j.titulo) like  lower(?) or  lower(j.titulo) like  lower(?)  or  lower(j.titulo) like  lower(?) or  lower(j.titulo) like  lower(?)\r\n";
			sql = sql	+ "group by j.jogo_id\r\n"
					+ "order by count(av.jogo_id ) desc\r\n"
					+ "limit ? OFFSET ?";
		}else
		if(ordem == Ordenacoes.classificacao) {
			sql = "select j.jogo_id,j.titulo,j.descricao,j.caminho_executavel,j.detalhes,j.tags,j.visibilidade,j.imagem_capa,j.genero,j.desenvolvedor_id \r\n"
					+ "from jogo j \r\n"
					+ "left join avaliacao av\r\n"
					+ "on av.jogo_id = j.jogo_id\r\n";
					if(termo_busca != null) sql = sql	+ "where  lower(j.titulo) like  lower(?) or  lower(j.titulo) like  lower(?)  or  lower(j.titulo) like  lower(?) or  lower(j.titulo) like  lower(?)\r\n";
					sql = sql + "group by j.jogo_id\r\n"
					+ "order by coalesce(sum(av.nota),0) desc\r\n"
					+ "limit ? OFFSET ?";
		}
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
	//	System.out.println("sql: "+sql);
		
		if(termo_busca != null) {
			psql.setString(1, "%"+termo_busca+"%");
			psql.setString(2, "%"+termo_busca);
			psql.setString(3, termo_busca+"%");
			psql.setString(4, termo_busca);
			psql.setInt(5, limit);
			psql.setInt(6, offset);
		}else {
			psql.setInt(1, limit);
			psql.setInt(2, offset);
		}
		
		ResultSet rst = psql.executeQuery();
		
		List<JogoModelo> lista = new ArrayList<>();

		//for (int i = 0; i<20;i++) {
		while(rst.next()) {
			
			lista.add(new JogoModelo(rst.getInt("jogo_id"),rst.getString("titulo") , rst.getString("descricao"), 
					rst.getString("caminho_executavel"), rst.getString("detalhes"), rst.getString("tags"), 
					rst.getString("visibilidade"), rst.getBytes("imagem_capa"), rst.getInt("genero")));
		}
		//	rst = psql.executeQuery();
		//}
		
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
	

	
	public int getJogosNumero() throws SQLException {
		String sql = "select count(*) from jogo";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt(1);
	}
	
	public void inserirJogo(JogoModelo modelo) throws SQLException {
		String sql = "insert into jogo (titulo, descricao, caminho_executavel,detalhes,tags,visibilidade,imagem_capa,genero,desenvolvedor_id)\r\n"
				+ "VALUES (?, ?, ?,?,?,?,?,?,?)";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, modelo.getTitulo());
		psql.setString(2, modelo.getDescricao());
		psql.setString(3, modelo.getCaminho_executavel());
		psql.setString(4, modelo.getDetalhes());
		psql.setString(5, modelo.getTags());
		psql.setString(6, modelo.getVisibilidade());
		psql.setBytes(7, modelo.getImagem_capa());
		psql.setInt(8, modelo.getGenero());
		psql.setInt(9, modelo.getDesenvolvedor_id());
		
		psql.execute();
		
	}
	
}


