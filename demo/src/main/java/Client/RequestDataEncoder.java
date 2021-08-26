package Client;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import TLV.Msg;
import TLV.MsgStuct;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestDataEncoder extends MessageToByteEncoder<Msg> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
		byte[] msgB = encode(msg.getMsg()).getBytes();
		int len = msgB.length;

		byte[] SendMsg = new byte[MsgStuct.B_TYPE_LENGTH + MsgStuct.B_LENGTH_LENGTH + len]; // int(type) + int(len) +
																							// msg.len
		int i = 0;

		byte[] byType = new byte[MsgStuct.B_TYPE_LENGTH];
		ByteBuffer bbType = ByteBuffer.wrap(byType);
		bbType.order(ByteOrder.LITTLE_ENDIAN); // Use little-endian order here

		// Write type
		bbType.asIntBuffer().put(msg.getType());
		for (i = 0; i < byType.length; i++) {
			SendMsg[i] = byType[i];
		}

		byte[] byLength = new byte[MsgStuct.B_LENGTH_LENGTH];
		ByteBuffer bbLength = ByteBuffer.wrap(byLength);
		bbLength.order(ByteOrder.LITTLE_ENDIAN); // Use little-endian order here

		// write length
		bbLength.asIntBuffer().put(len);//
		int j = i;
		for (int k = 0; k < byLength.length; j++, k++) {
			SendMsg[j] = byLength[k];
		}

		// write message
		int n = j;
		for (int k = 0; k < len; k++, n++) {
			SendMsg[n] = msgB[k];
		}
		try {
			out.writeBytes(SendMsg);
		} catch (Exception e) {
			System.out.println("Error writing message");
			e.printStackTrace();
		}
	}

	private String encode(String str) throws Exception {
		// Đọc file chứa public key
		FileInputStream fis = new FileInputStream("D:\\publicKey.rsa");
		byte[] b = new byte[fis.available()];
		fis.read(b);
		fis.close();

		// Tạo public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = factory.generatePublic(spec);

		// Mã hoá dữ liệu
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, pubKey);
		byte encryptOut[] = c.doFinal(str.getBytes());
		return Base64.getEncoder().encodeToString(encryptOut);
	}
}