package com.edsolab.BotNet.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.edsolab.BotNet.model.ClientChannel;

public class Test {
	public static void main(String[] args) throws SQLException {
		Connection connection = JDBCConnection.getJDBCConnection();
		String sql = "SELECT * FROM clients_online";
		PreparedStatement pre = connection.prepareStatement(sql);
		ResultSet result = pre.executeQuery();
		while (result.next()) {
			System.out.println("???????");
		}
	}
}
