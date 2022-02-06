package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.JogoModelo;
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
	
	public int inserir(JogoModelo modelo) throws SQLException { //retorna o id inserido
		String sql = "insert into jogo (titulo, descricao, caminho_executavel,detalhes,tags,visibilidade,imagem_capa,genero,desenvolvedor_id)\r\n"
				+ "VALUES (?, ?, ?,?,?,?,?,?,?) RETURNING jogo_id";
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
		
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt(1); 
	}
	
	public void atualizar(JogoModelo modelo) throws SQLException {
		String sql = "update  jogo set titulo = ?, descricao = ?, caminho_executavel = ?,detalhes = ?,tags = ?,visibilidade = ?,imagem_capa = ?,genero = ?,desenvolvedor_id = ?\r\n"
				+ " where jogo_id = ?";
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
		psql.setInt(10, modelo.getJogo_id());
		
		psql.execute();
	}
	
	public void excluir(int jogo_id) throws SQLException {
		String sql = "delete from screenshot where jogo_id = ?;delete from jogo where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, jogo_id);
		
		pst.execute();
	}
	
	public List<JogoModelo> getListaByDesenvolvedor(int desenvolvedor_id) throws SQLException{ //pega todos os jogos de um determinado desenvolvedor
		String sql = "select jogo_id,titulo,descricao,caminho_executavel,detalhes,tags,visibilidade,imagem_capa,genero,desenvolvedor_id,arquivo_caminho from jogo where desenvolvedor_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, desenvolvedor_id);
		
		ResultSet rst = pst.executeQuery();
		List<JogoModelo> lista = new ArrayList<>();
		
		while(rst.next()) {
			lista.add(new JogoModelo(rst.getInt("jogo_id"),rst.getString("titulo") , rst.getString("descricao"), 
					rst.getString("caminho_executavel"), rst.getString("detalhes"), rst.getString("tags"), 
					rst.getString("visibilidade"), rst.getBytes("imagem_capa"), rst.getInt("genero"),rst.getInt("desenvolvedor_id"),rst.getString("arquivo_caminho")));
		}
		
		return lista;
	}
	

	
	public int getJogosNumero() throws SQLException { //pega a quantidade total de jogos
		String sql = "select count(*) from jogo";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt(1);
	}
	
	public int getJogosCatalogoNumero() throws SQLException { 
		String sql = "select count(*) from view_jogo_catalogo";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt(1);
	}
	
	public int getJogosAprovNumero() throws SQLException { 
		String sql = "select count(*) from view_jogo_aprovacao";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt(1);
	}
	
	public int getJogosBibliotecaNumero(int usuario_id) throws SQLException { 
		String sql = "select count(*) from view_jogo_biblioteca where usuario_id = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, usuario_id);
		ResultSet rst = psql.executeQuery();
		rst.next();
		
		return rst.getInt(1);
	}
	
	public JogoModelo getJogo(int jogo_id) throws SQLException {
		String sql = "select jogo_id,titulo,descricao,caminho_executavel,detalhes,tags,visibilidade,imagem_capa,genero,desenvolvedor_id,arquivo_caminho from jogo where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return new JogoModelo(rst.getInt("jogo_id"),rst.getString("titulo") , rst.getString("descricao"), 
				rst.getString("caminho_executavel"), rst.getString("detalhes"), rst.getString("tags"), 
				rst.getString("visibilidade"), rst.getBytes("imagem_capa"), rst.getInt("genero"),rst.getInt("desenvolvedor_id"),rst.getString("arquivo_caminho"));
	}
	
	public boolean seTituloExiste(String titulo) throws SQLException {
		String sql = "select count(*) as ocorrencias from jogo where titulo = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setString(1, titulo);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		if(rst.getInt("ocorrencias")==0) {
			return false;
		}else {
			return true;
		}
		
	}
	
	public boolean seTituloExisteEmOutroJogo(String titulo, int jogo_id) throws SQLException {
		String sql = "select count(*) as ocorrencias from jogo where titulo = ? and jogo_id != ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setString(1, titulo);
		pst.setInt(2, jogo_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		if(rst.getInt("ocorrencias")==0) {
			return false;
		}else {
			return true;
		}
		
	}

	public boolean seDesenvolvedorDoJogo(int jogo_id, int desenvolvedor_id) throws SQLException {
		String sql = "select count(*) as ocorrencias from jogo where jogo_id = ? and desenvolvedor_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		pst.setInt(2, desenvolvedor_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		if(rst.getInt("ocorrencias")==0) {
			return false;
		}else {
			return true;
		}
	}

	public void salvarCaminhoArquivo(int jogo_id, String path) throws SQLException {
		String sql = "update jogo  set arquivo_caminho = ? where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setString(1, path);
		pst.setInt(2, jogo_id);
		
		pst.execute();
	}
	
}


