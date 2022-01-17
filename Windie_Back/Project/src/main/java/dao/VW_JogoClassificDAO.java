package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.VW_JogoClassificModelo;
import util.ConexaoBanco;
import util.Debug;
import util.Ordenacoes;

public class VW_JogoClassificDAO {
	
	private static VW_JogoClassificDAO instance;

	public static VW_JogoClassificDAO getInstance() {
		if(instance == null) {
			instance = new VW_JogoClassificDAO();
		}
		
		return instance;
	}

	public List<VW_JogoClassificModelo> getListaPaginaCatalogo(Ordenacoes ordem, int itens_por_pagina, int pagina, String termo_busca) throws SQLException { //pega uma lista com um numero dado de itens em uma determinada posição determinada pela pagina, também pode filtrar a busca usando um termo
		
		int limit = itens_por_pagina; //limite de itens usado na consulta
		int offset = (itens_por_pagina*pagina)-itens_por_pagina; //offset em itens na consulta, quantos itens longe do primeiro elemento a consulta deve ser feita
		String orderBy; //ordenação usada na consulta
		String where = ""; //condição especificada pelo termo de busca
		
		switch (ordem) {
		case popularidade:
				orderBy = " order by j.horas_jogadas desc";
			break;

		default:
				orderBy = " order by j.nota desc";
			break;
		}
		
		if(termo_busca != null) where = " where  lower(j.titulo) like  lower(?) or  lower(j.titulo) like  lower(?)  or  lower(j.titulo) like  "
									  + "lower(?) or  lower(j.titulo) like  lower(?) or "
									  + "lower(j.tags) like  lower(?)";
		
		String sql = "";
		if(ordem == Ordenacoes.popularidade) {
			sql = "select j.jogo_id,j.titulo,j.descricao,j.caminho_executavel,j.detalhes,j.tags,j.visibilidade,j.imagem_capa,j.genero,j.desenvolvedor_id,j.nota,j.avaliacoes_numero,j.horas_jogadas \r\n"
					+ "from view_jogo_classific j \r\n"
					+"inner join view_jogo_catalogo jc on jc.jogo_id = j.jogo_id"
					+ where
					+ orderBy
					+ " limit ? OFFSET ?";
		}

		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		
		if(termo_busca != null) { //seta termos de buca caso ele tenha sido informado
			psql.setString(1, "%"+termo_busca+"%");
			psql.setString(2, "%"+termo_busca);
			psql.setString(3, termo_busca+"%");
			psql.setString(4, termo_busca);
			psql.setString(5, "%"+termo_busca+"%");
			psql.setInt(6, limit);
			psql.setInt(7, offset);
		}else {
			psql.setInt(1, limit);
			psql.setInt(2, offset);
		}
		Debug.logDetalhe("getListaPagina  sql: "+sql);
		ResultSet rst = psql.executeQuery();
		
		List<VW_JogoClassificModelo> lista = new ArrayList<>();

		while(rst.next()) {
			
			lista.add(new VW_JogoClassificModelo(rst.getInt("jogo_id"),rst.getString("titulo") , rst.getString("descricao"), 
					rst.getString("caminho_executavel"), rst.getString("detalhes"), rst.getString("tags"), 
					rst.getString("visibilidade"), rst.getBytes("imagem_capa"), rst.getInt("genero"),rst.getInt("desenvolvedor_id"),
					rst.getFloat("nota"),rst.getInt("avaliacoes_numero"),rst.getFloat("horas_jogadas")));
		}
		
		return lista;
	}
	
	public List<VW_JogoClassificModelo> getListaPaginaAprovacao(Ordenacoes ordem, int itens_por_pagina, int pagina, String termo_busca) throws SQLException { //pega uma lista com um numero dado de itens em uma determinada posição determinada pela pagina, também pode filtrar a busca usando um termo
		
		int limit = itens_por_pagina; //limite de itens usado na consulta
		int offset = (itens_por_pagina*pagina)-itens_por_pagina; //offset em itens na consulta, quantos itens longe do primeiro elemento a consulta deve ser feita
		String orderBy; //ordenação usada na consulta
		String where = ""; //condição especificada pelo termo de busca
		
		switch (ordem) {
		case popularidade:
				orderBy = " order by j.horas_jogadas desc";
			break;

		default:
				orderBy = " order by j.nota desc";
			break;
		}
		
		if(termo_busca != null) where = " where  lower(j.titulo) like  lower(?) or  lower(j.titulo) like  lower(?)  or  lower(j.titulo) like  "
									  + "lower(?) or  lower(j.titulo) like  lower(?) or "
									  + "lower(j.tags) like  lower(?)";
		
		String sql = "";
		if(ordem == Ordenacoes.popularidade) {
			sql = "select j.jogo_id,j.titulo,j.descricao,j.caminho_executavel,j.detalhes,j.tags,j.visibilidade,j.imagem_capa,j.genero,j.desenvolvedor_id,j.nota,j.avaliacoes_numero,j.horas_jogadas \r\n"
					+ "from view_jogo_classific j \r\n"
					+"inner join view_jogo_aprovacao jc on jc.jogo_id = j.jogo_id"
					+ where
					+ orderBy
					+ " limit ? OFFSET ?";
		}

		PreparedStatement psql = ConexaoBanco.getInstance().getPreparedStatement(sql);
		
		
		if(termo_busca != null) { //seta termos de buca caso ele tenha sido informado
			psql.setString(1, "%"+termo_busca+"%");
			psql.setString(2, "%"+termo_busca);
			psql.setString(3, termo_busca+"%");
			psql.setString(4, termo_busca);
			psql.setString(5, "%"+termo_busca+"%");
			psql.setInt(6, limit);
			psql.setInt(7, offset);
		}else {
			psql.setInt(1, limit);
			psql.setInt(2, offset);
		}
		Debug.logDetalhe("getListaPagina  sql: "+sql);
		ResultSet rst = psql.executeQuery();
		
		List<VW_JogoClassificModelo> lista = new ArrayList<>();

		while(rst.next()) {
			
			lista.add(new VW_JogoClassificModelo(rst.getInt("jogo_id"),rst.getString("titulo") , rst.getString("descricao"), 
					rst.getString("caminho_executavel"), rst.getString("detalhes"), rst.getString("tags"), 
					rst.getString("visibilidade"), rst.getBytes("imagem_capa"), rst.getInt("genero"),rst.getInt("desenvolvedor_id"),
					rst.getFloat("nota"),rst.getInt("avaliacoes_numero"),rst.getFloat("horas_jogadas")));
		}
		
		return lista;
	}

	public VW_JogoClassificModelo getJogo(int jogo_id) throws SQLException {
		String sql = "select jogo_id,titulo,descricao,caminho_executavel,detalhes,tags,visibilidade,imagem_capa,genero,desenvolvedor_id,nota,avaliacoes_numero,horas_jogadas from view_jogo_classific where jogo_id = ?";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		pst.setInt(1, jogo_id);
		ResultSet rst = pst.executeQuery();
		rst.next();
		
		return new VW_JogoClassificModelo(rst.getInt("jogo_id"),rst.getString("titulo") , rst.getString("descricao"), 
				rst.getString("caminho_executavel"), rst.getString("detalhes"), rst.getString("tags"), 
				rst.getString("visibilidade"), rst.getBytes("imagem_capa"), rst.getInt("genero"),rst.getInt("desenvolvedor_id"),
				rst.getFloat("nota"),rst.getInt("avaliacoes_numero"),rst.getFloat("horas_jogadas"));
	}

}
