package Server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import TLV.Msg;
import TLV.MsgStruct;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseDataEncoder extends MessageToByteEncoder<Msg> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
		byte[] type = msg.getType().getBytes();
		byte[] msgB = msg.getMsg();
		byte[] key = msg.getKey();

		int lenM = msgB.length;
		int lenK = key.length;

		byte[] SendMsg = new byte[MsgStruct.B_TYPE_LENGTH + MsgStruct.B_KEY_LENGTH + lenK + MsgStruct.B_MSG_LENGTH
				+ lenM];
		int i = 0;

		// Write type
		for (i = 0; i < MsgStruct.B_TYPE_LENGTH; i++) {
			SendMsg[i] = type[i];
		}

		byte[] by = new byte[MsgStruct.B_KEY_LENGTH];
		ByteBuffer bb = ByteBuffer.wrap(by);
		bb.order(ByteOrder.LITTLE_ENDIAN); // Use little-endian order here

		// write length key
		bb.asIntBuffer().put(lenK);//
		int h = i;
		for (int k = 0; k < by.length; h++, k++) {
			SendMsg[h] = by[k];
		}

		// write key
		int m = h;
		for (int k = 0; k < lenK; k++, m++) {
			SendMsg[m] = key[k];
		}

		byte[] byLength = new byte[MsgStruct.B_MSG_LENGTH];
		ByteBuffer bbLength = ByteBuffer.wrap(byLength);
		bbLength.order(ByteOrder.LITTLE_ENDIAN); // Use little-endian order here

		// write length msg
		bbLength.asIntBuffer().put(lenM);//
		int j = m;
		for (int k = 0; k < byLength.length; j++, k++) {
			SendMsg[j] = byLength[k];
		}

		// write message
		int n = j;
		for (int k = 0; k < lenM; k++, n++) {
			SendMsg[n] = msgB[k];
		}
		try {
			out.writeBytes(SendMsg);
		} catch (Exception e) {
			System.out.println("Error writing message");
			e.printStackTrace();
		}
	}

	private byte[] encode(byte[] str) throws Exception {
		// Tạo public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(KeyServer.publicKeyC);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = factory.generatePublic(spec);

		// Mã hoá dữ liệu
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, pubKey);
		return c.doFinal(str);
	}

}