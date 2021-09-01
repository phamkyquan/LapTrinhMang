package ConnectDB;

import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import io.netty.channel.ChannelId;

public class InsertStatement {
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
}
