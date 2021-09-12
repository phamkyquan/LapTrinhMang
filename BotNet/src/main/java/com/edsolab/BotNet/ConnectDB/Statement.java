package com.edsolab.BotNet.ConnectDB;

import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.edsolab.BotNet.model.ClientChannel;

import io.netty.channel.ChannelId;

public class Statement {

	public static void insert(ChannelId channelId, SocketAddress socketAddress, Date now, String status)
			throws SQLException {
		Connection connection = JDBCConnection.getJDBCConnection();
		String sql = "INSERT INTO client(id, ip, last_seen, status) values(?,?,?,?)";
		PreparedStatement pre = connection.prepareStatement(sql);
		pre.setString(1, channelId.toString());
		pre.setString(2, socketAddress.toString());
		pre.setString(3, now.toString());
		pre.setString(4, status);
		pre.execute();
	}
	public static void insertKey(ChannelId channelId, byte[] key) throws SQLException {
		Connection connection = JDBCConnection.getJDBCConnection();
		String sql = "INSERT INTO publickeys(id, keyClient) values(?,?)";
		PreparedStatement pre = connection.prepareStatement(sql);
		pre.setString(1, channelId.toString());
		pre.setBytes(2, key);
		pre.execute();
	}

	public static byte[] selectKey(ChannelId channelId) throws SQLException {
		Connection connection = JDBCConnection.getJDBCConnection();
		String sql = "SELECT * FROM publickeys WHERE id = ?";
		PreparedStatement pre = connection.prepareStatement(sql);
		pre.setString(1, channelId.toString());
		ResultSet result = pre.executeQuery();
		while (result.next()) {
			return result.getBytes(2);
		}
		return null;
	}

	public static void deleteKey(ChannelId channelId) throws SQLException {
		Connection connection = JDBCConnection.getJDBCConnection();
		String sql = "DELETE FROM publickeys WHERE id = ?";
		PreparedStatement pre = connection.prepareStatement(sql);
		pre.setString(1, channelId.toString());
		pre.execute();
	}
}
