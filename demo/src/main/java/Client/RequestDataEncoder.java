package Client;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import TLV.Msg;
import TLV.Tlv;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestDataEncoder extends MessageToByteEncoder<Msg> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
	  Tlv tlv = new Tlv();
//	  FileInputStream fis = new FileInputStream("D:\\publicKey.rsa");
//		byte[] b = new byte[fis.available()];
//		fis.read(b);
//		fis.close();
//
//		// Tạo public key
//		X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
//		KeyFactory factory = KeyFactory.getInstance("RSA");
//		PublicKey pubKey = factory.generatePublic(spec);
//
//		// Mã hoá dữ liệu
//		Cipher c = Cipher.getInstance("RSA");
//		c.init(Cipher.ENCRYPT_MODE, pubKey);
//		String str = "helloworld";
//		byte encryptOut[] = c.doFinal(str.getBytes());
//		String strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
		
	  tlv.writeMsg(msg.getMsg(), msg.getType(), out);
      
  }
}