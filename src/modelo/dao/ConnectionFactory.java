package modelo.dao;

import java.sql.*;
public class ConnectionFactory {
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost/loja" + 
			"?useTimezone=true&serverTimezone=America/Sao_Paulo";
		return DriverManager.getConnection(
				url, "user", "password");
	}
}

