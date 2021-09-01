package Server;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ConnectDB.InsertStatement;
import TLV.Data;
import TLV.Msg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {
	Msg msg;

	static final List<Channel> channels = new ArrayList<Channel>();

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws SQLException {
		System.out.println("Client joined - " + ctx);
		Date now = new Date();
		InsertStatement.insert(ctx.channel().id(), ctx.channel().remoteAddress(), now, "Connected");
		channels.add(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msgRequest) throws Exception {
		if (!KeyServer.check) {
			KeyServer.check = true;
			Msg requestData = (Msg) msgRequest;
			KeyServer.publicKeyC = requestData.getKey();
			msg = new Msg();
			// Thuật toán phát sinh khóa - RSA
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);

			// Khởi tạo cặp khóa
			KeyPair kp = kpg.genKeyPair();
			// PublicKey
			PublicKey publicKeyS = kp.getPublic();
			// PrivateKey
			KeyServer.PrivateKeyS = kp.getPrivate();
			System.out.println("Generate key successfully");
			msg.setType("pubK");
			msg.setMsg("public key".getBytes());
			msg.setKey(publicKeyS.getEncoded());
			ctx.writeAndFlush(msg);
		} else {
			Data data = (Data) msgRequest;
			System.out.println("From client: "+new String(data.getData()));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws SQLException {
		System.out.println("Closing connection for client - " + ctx.channel().remoteAddress());
		Date now = new Date();
		InsertStatement.insert(ctx.channel().id(), ctx.channel().remoteAddress(), now, "Disconnected");
		ctx.close();
	}

}
