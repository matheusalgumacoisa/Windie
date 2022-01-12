package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.GeneroModelo;
import util.ConexaoBanco;

public class GeneroDAO {
	
	public static GeneroDAO instance;
	
	public static GeneroDAO getInstance() {
		if(instance == null) {
			instance = new GeneroDAO();
		}
		
		return instance;
	}
	
	public List<GeneroModelo> getLista() throws SQLException{
		String sql = "select genero_id,genero_nome from genero order by genero_nome";
		PreparedStatement pst = ConexaoBanco.getInstance().getPreparedStatement(sql);
		ResultSet rst = pst.executeQuery();
		List<GeneroModelo> generos = new ArrayList<>();
		
		while(rst.next()) {
			generos.add(new GeneroModelo(rst.getInt(1),rst.getString(2)));
		}
		
		return generos;	
	}

}
