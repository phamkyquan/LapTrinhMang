package Client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import TLV.Data;
import TLV.Msg;
import TLV.MsgStruct;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestDataEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msgO, ByteBuf out) throws Exception {
		if(!KeyClient.check) {
			Msg msg = (Msg) msgO;
			encodeMsg(msg,out);
		}
		else {
			Data data = (Data) msgO;
			encodeData(data,out);
		}
	}

	private byte[] encode(byte[] str) throws Exception {
		// Tạo public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(KeyClient.publicKeyS);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = factory.generatePublic(spec);

		// Mã hoá dữ liệu
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, pubKey);
		byte encryptOut[] = c.doFinal(str);
		return Base64.getEncoder().encode(encryptOut);
	}
	
	private String encrypt(String strToEncrypt, String myKey) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] key = myKey.getBytes("UTF-8");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	
	private void encodeMsg(Msg msg, ByteBuf out) {
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
	
	private void encodeData(Data data, ByteBuf out) throws Exception {
		byte[] tag = data.getTag().getBytes();
		//Ma hoa data bang key doi xung
		byte[] dt = encrypt(new String(data.getData()), new String(data.getKey())).getBytes();
		//Ma hoa key bang RSA
		byte[] key = encode(data.getKey());

		int lenD = dt.length;
		int lenK = key.length;

		byte[] SendMsg = new byte[MsgStruct.B_TYPE_LENGTH + MsgStruct.B_KEY_LENGTH + lenK + MsgStruct.B_MSG_LENGTH
				+ lenD];
		int i = 0;

		// Write type
		for (i = 0; i < MsgStruct.B_TYPE_LENGTH; i++) {
			SendMsg[i] = tag[i];
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

		// write length data
		bbLength.asIntBuffer().put(lenD);//
		int j = m;
		for (int k = 0; k < byLength.length; j++, k++) {
			SendMsg[j] = byLength[k];
		}

		// write data
		int n = j;
		for (int k = 0; k < lenD; k++, n++) {
			SendMsg[n] = dt[k];
		}
		try {
			out.writeBytes(SendMsg);
		} catch (Exception e) {
			System.out.println("Error writing message");
			e.printStackTrace();
		}
	}
}