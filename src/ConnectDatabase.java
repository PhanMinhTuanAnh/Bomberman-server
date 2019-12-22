import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDatabase {
	
	static String hostName = "localhost";
	static String dbName = "newservlet12152019";
	static String userName = "root";
	static String password = "No123456";
	
	public static Connection getMySQLConnection() throws SQLException {
		
		String ConnectionUrl = "jdbc:mysql://" + hostName + ":3306/" + dbName;
		return DriverManager.getConnection(ConnectionUrl, userName, password);
		
	}
	
	
}
