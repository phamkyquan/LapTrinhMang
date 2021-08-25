package EncodeDecode;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class Decryption {

	public static void main(String[] args) {
		try {
			// Đọc file chứa private key
			FileInputStream fis = new FileInputStream("D:\\privateKey.rsa");
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();

			// Tạo private key
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = factory.generatePrivate(spec);

			// Giải mã dữ liệu
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.DECRYPT_MODE, priKey);
			byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(
					"EXhoXvrJsQfD2y26+IrGH99oE9nOQMbw3iQ0jkWfHHnfqtsO8CQbQDYF9M6H49U6V2oLZuI94GAom9KH87t+R97B/npxoAM/hoNak23swB8ZDR5FMDozQ4xaYWhjUirrvQZ9fx/zI96SEqJMlkWf6ARFihL5iPldxsLDIRKERRWljBf0REf1vbtjzzMs8K4BXmjnO9tJmop0NHW2RddzpPv1iNA/l29eZmwHZQSdSUjDB3FLEraTSMTKRd92jT1j2AgWoXx8BdhMCOa5AXAMxfcNgR9Q32SFlT1322OPp1rP0MFUNflZHlElI1zfUy69nbZk4sIQYjAHL3zHcj37gQ=="));
			System.out.println("Dữ liệu sau khi giải mã: " + new String(decryptOut));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}