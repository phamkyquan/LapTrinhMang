package EncodeDecode;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import java.util.Base64;

public class Encrpytion {

	public static void main(String[] args) {
		try {
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
			String msg = "helloworld";
			byte encryptOut[] = c.doFinal(msg.getBytes());
			String strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
			System.out.println("Chuỗi sau khi mã hoá: " + strEncrypt);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}