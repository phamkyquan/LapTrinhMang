package com.edsolab.BotNet.ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConnection {
	public static Connection getJDBCConnection() {
		final String url = "jdbc:mysql://localhost:3306/connection";
		final String user = "root";
		final String password = "472000";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(url, user, password);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
