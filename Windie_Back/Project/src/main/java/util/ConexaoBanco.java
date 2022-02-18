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
		/*connection = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/Windie",
				"postgres",
				"postgres");*/
		connection = DriverManager.getConnection(
		"jdbc:postgresql://ec2-3-227-15-75.compute-1.amazonaws.com:5432/dfk8dohve85dfj",
		"qmaxvsnbnrvisi",
		"0a3989f19ddcccdb4bad8f352c98343a04bbeb6fd53e87db1aeafc8b56c01d82");
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

