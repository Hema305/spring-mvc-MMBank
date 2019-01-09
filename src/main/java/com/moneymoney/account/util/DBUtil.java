package com.moneymoney.account.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	
	private static Connection connection;

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		if(connection==null) {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection
				("jdbc:mysql://localhost:3306/bankapp_db", "root", "root");
			connection.setAutoCommit(false);
		}
		return connection;
	}
	public static void commit() throws SQLException {
		if(connection!=null) {
			connection.commit();
		}
	}
	public static void rollback() throws SQLException {
		if(connection!=null) {
			connection.rollback();
		}
	}
	
	public static void closeConnection() throws SQLException {
		if(connection!=null) {
			connection.close();
			connection = null;
		}
	}
}
















