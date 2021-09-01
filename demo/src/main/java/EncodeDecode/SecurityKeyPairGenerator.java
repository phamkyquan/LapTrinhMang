package EncodeDecode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SecurityKeyPairGenerator {

	public static void main(String[] args) {
		try {
			// Thuật toán phát sinh khóa - RSA
			// Độ dài khóa 1024(bits), độ dài khóa này quyết định đến độ an toàn của khóa,
			// càng lớn thì càng an toàn
			// Demo chỉ sử dụng 1024 bit. Nhưng theo khuyến cáo thì độ dài khóa nên tối
			// thiểu là 2048
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);

			KeyPairGenerator kpgK = KeyPairGenerator.getInstance("RSA");
			kpgK.initialize(2048);

			// Khởi tạo cặp khóa
			KeyPair kp = kpg.genKeyPair();

			KeyPair kpK = kpgK.genKeyPair();
			// PublicKey
			PublicKey publicKey = kpK.getPublic();
			// PrivateKey
			PrivateKey privateKey = kp.getPrivate();

			File publicKeyFile = createKeyFile(new File("D:\\publicKey.rsa"));
			File privateKeyFile = createKeyFile(new File("D:\\privateKey.rsa"));

			// Lưu Public Key
			FileOutputStream fos = new FileOutputStream(publicKeyFile);
			fos.write(publicKey.getEncoded());
			fos.close();

			// Lưu Private Key
			fos = new FileOutputStream(privateKeyFile);
			fos.write(privateKey.getEncoded());
			fos.close();

			System.out.println("Generate key successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File createKeyFile(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}
		return file;
	}
}