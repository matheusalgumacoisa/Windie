package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.ConexaoBanco;
import util.CustomException;

public class UsuarioDAO {

	private static UsuarioDAO instance;
	
	public void InserirUsuario(UsuarioModelo usuario) throws SQLException, CustomException{
		String sql = "insert into usuario (email,senha,apelido)  values(?,?,?)";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);

		 psql.setString(1, usuario.getEmail());
		 psql.setString(2, usuario.getSenha());
		 psql.setString(3, usuario.getApelido());
				
		 psql.executeUpdate();
		
	}
	
	public UsuarioModelo getUsuarioByEmail(String email) throws SQLException {
		String sql = "select * from usuario where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, email);
		ResultSet rst = psql.executeQuery();
		rst.next();
		return new UsuarioModelo(rst.getInt("usuario_id"),rst.getString("email"),rst.getString("senha"), rst.getString("apelido"));
	}
	
	public static UsuarioDAO getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new UsuarioDAO();
			return instance;
			
		}else {
			return instance;
		}
	}

	public String apelidoByEmail(String email) throws SQLException {
		String sql = "select apelido from usuario where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, email);
		ResultSet rst = psql.executeQuery();
		rst.next();
		return rst.getString("apelido");
	}

	public void AtualizarUsuario(String email, String apelido) throws SQLException {
		String sql = "update usuario set  apelido = ? where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);

		 psql.setString(1, apelido);
		 psql.setString(2, email);
				
		 psql.executeUpdate();
		
	}
	
	public int idByEmail(String usuarioEmail ) throws SQLException {
			
		String sql = "select usuario_id from usuario where email = ?";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setString(1, usuarioEmail);
		ResultSet rst = psql.executeQuery();
		rst.next();
		return rst.getInt("usuario_id");
	}
	
	public boolean seAssinante(int usuario_id) throws SQLException {
		String sql = "select pagamento_id from pagamento_assinatura"
				+ " where usuario_id = ? and "
				+ "current_date <=  to_date(EXTRACT(YEAR FROM current_date) "
				+ "|| '-'||EXTRACT(MONTH FROM data_pagamento)+1||'-'|| "
				+ "EXTRACT(DAY FROM data_pagamento),'YYYY-MM-DD')";
		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		psql.setInt(1, usuario_id);
		ResultSet rst = psql.executeQuery();
		
		if(rst.next()) {
			
			return true;
		}
		return false;
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
}




/*package persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Blindado;
import util.Retorno;

public class DAOBlindado {
	
	public DAOBlindado() {
		
	}
	public String inserir(Blindado blindado) {
		PreparedStatement pst = null;
		String sql = "insert into blindado (nome,categoria,locomocao,anfibio,peso,decada)  values(?,?,?,?,?,?)";
		
		try {
			
			 pst = Conexao.obterConexao().obterSQLPreparada(sql);

			 pst.setString(1, blindado.getNome());
				pst.setString(2, blindado.getCategoria());
				pst.setString(3, blindado.getLocomocao());
				pst.setString(4, blindado.getAnfibio());
				pst.setString(5, blindado.getPeso());
				pst.setString(6, blindado.getDecada());
					
				pst.executeUpdate();
				
				
				
				return   "Operação bem sucedida!";
				
				
				
		}catch (SQLException e) {
			e.printStackTrace();
			return "Operação não pode ser completada!";
			
		}
		

	}
	
	
	public String  alterar(Blindado blindado) {
		

		PreparedStatement pst = null;
	
		
		String sql = "update blindado set nome = ?,categoria = ?, locomocao = ?,anfibio = ?,peso = ?,decada =? where id = ?  ";
		
		try {
			
			 pst = Conexao.obterConexao().obterSQLPreparada(sql);

			 pst.setString(1, blindado.getNome());
				pst.setString(2, blindado.getCategoria());
				pst.setString(3, blindado.getLocomocao());
				pst.setString(4, blindado.getAnfibio());
				pst.setString(5, blindado.getPeso());
				pst.setString(6, blindado.getDecada());
				pst.setLong(7, blindado.getId());
				
				
				
				
				pst.executeUpdate();
				

				
				return "Operação bem sucedida!";
				
		}catch (SQLException e) {
			
			
			return "Operação não pode ser completada!";
			
		}
	}
	
	public String deletar(Blindado blindado) {
	
		
		PreparedStatement pst = null;
	
		
		String sql = "delete from  blindado where id = ?  ";
		
		try {
			
			 pst = Conexao.obterConexao().obterSQLPreparada(sql);

			
				pst.setLong(1, blindado.getId());
			 
				pst.executeUpdate();
				
				return "Deletado!";
				
		}catch (SQLException e) {
			
			return "Operação não pode ser completada!";
			
			
		}
	}
	
	
	
	public Retorno<List<Blindado>> listar(){
		Retorno<List<Blindado>> retornoLista = new Retorno<List<Blindado>>(true,"OK");
		
		String sql = "select  id,nome,categoria,locomocao,anfibio,peso,decada from blindado";
		
		Retorno<PreparedStatement> retSQL = obterSQLPreparada(sql);
		if(retSQL.isSucesso()==false) {	return new Retorno<List<Blindado>>(retSQL);
		}
		
		Retorno<ResultSet> retResultado = executarSQLPreparadaConsulta(retSQL.getDado());
		if(retResultado.isSucesso()==false) {return new Retorno<List<Blindado>>(retResultado); }
		
		ResultSet rs = retResultado.getDado();
		List<Blindado> lista = new ArrayList<>();
		try {
			while(rs.next()) {
				Blindado a = new Blindado();
				a.setId(rs.getInt("id"));
				a.setNome(rs.getString("nome"));
				a.setCategoria(rs.getString("categoria"));
				a.setLocomocao(rs.getString("locomocao"));
				a.setAnfibio(rs.getString("anfibio"));
				a.setPeso(rs.getString("peso"));
				a.setDecada(rs.getString("decada"));
				lista.add(a);
			}
		}catch(SQLException e) {
			retornoLista.setSucesso(false);
			//TODO: tratar erro de banco melhor
			retornoLista.setMensagem(e.getMessage());
		}
		retornoLista.setDado(lista);
		return retornoLista;
		
	}
	
	private Retorno<ResultSet> executarSQLPreparadaConsulta(PreparedStatement pst) {
		Retorno<ResultSet> ret = new Retorno<>(true,"Consulta Executada com Sucesso!");
		try {
			ResultSet rs = pst.executeQuery();	
			ret.setDado(rs);
			
		} catch (SQLException e) {
		
			throw new RuntimeException(e.getMessage());
			
		}
		return ret;
	}
	private Retorno<PreparedStatement> obterSQLPreparada(String sql) {
		Retorno<PreparedStatement> ret = new Retorno<>(true,"OK");
		PreparedStatement pst = null;
		try {
			 pst = Conexao.obterConexao().obterSQLPreparada(sql);
			 ret.setDado(pst);
		}catch (SQLException e) {
			ret.setSucesso(false);
			if(e.getCause().getClass().getSimpleName().equals("ConnectException")) {
				ret.setMensagem("Erro de Conexão com Banco!");
			}else {
				ret.setMensagem("Erro desconhecido:"+e.getMessage());
			}
		}
		return ret;
	}
}*/