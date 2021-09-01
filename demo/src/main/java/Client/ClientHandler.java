package Client;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import TLV.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private Msg msg;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		msg = new Msg();
		// Thuật toán phát sinh khóa - RSA
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		// Khởi tạo cặp khóa
		KeyPair kp = kpg.genKeyPair();
		// PublicKey
		PublicKey publicKeyC = kp.getPublic();
		// PrivateKey
		KeyClient.privateKeyC = kp.getPrivate();
		System.out.println("Generate key successfully");
		msg.setType("pubK");
		msg.setMsg("public key".getBytes());
		msg.setKey(publicKeyC.getEncoded());
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msgResponse) throws Exception {
		Msg responseData = (Msg) msgResponse;
		KeyClient.publicKeyS = responseData.getKey();
		System.out.println("Conneted.......");
		KeyClient.check = true;
		System.out.print("I: ");
	}
}