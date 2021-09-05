package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexaoBanco {
	private static ConexaoBanco instance;
	private Connection connection;
	
	private ConexaoBanco() throws SQLException {
		instance = this;
		connection = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/Windie",
				"postgres",
				"postgres");
	}
	
	public static ConexaoBanco getInstance() throws SQLException {
		
		if(instance ==null) {
			
			instance = new ConexaoBanco();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public PreparedStatement getPreparedStatement(String sql) 
			throws SQLException {
		return this.connection.prepareStatement(sql);
	}
}

