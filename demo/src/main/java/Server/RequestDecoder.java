package Server;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

import TLV.Msg;
import TLV.MsgStuct;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class RequestDecoder extends ReplayingDecoder<RequestDecoder.DecodingState> {

	private Msg msg;

	public RequestDecoder() {
		this.reset();
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		switch (state()) {

			case TYPE:
				buffer.markReaderIndex();
				if (buffer.readableBytes() < MsgStuct.B_TYPE_LENGTH) {
					buffer.resetReaderIndex();
					return;
				}
	
				byte[] type = new byte[MsgStuct.B_TYPE_LENGTH];
				buffer.readBytes(type, 0, type.length);
				this.msg.setType(LITTLEENDIAN(type));
				checkpoint(DecodingState.LENGTH);
				break;
	
			case LENGTH:
	
				buffer.markReaderIndex();
				if (buffer.readableBytes() < MsgStuct.B_LENGTH_LENGTH) {
					buffer.resetReaderIndex();
					return;
				}
	
				byte[] length = new byte[MsgStuct.B_LENGTH_LENGTH];
				buffer.readBytes(length, 0, length.length);
				this.msg.setLen(LITTLEENDIAN(length));
				checkpoint(DecodingState.MSG);
				break;
	
			case MSG:
				int lengMsg = this.msg.getLen();
				if (lengMsg > 0) {
					buffer.markReaderIndex();
					if (buffer.readableBytes() < lengMsg) {
						buffer.resetReaderIndex();
						return;
					}
				}
				byte[] msgM = new byte[lengMsg];
				buffer.readBytes(msgM, 0, lengMsg);
				this.msg.setMsg(decode(new String(msgM)));
				out.add(this.msg);
				reset();
				break;

			default:  throw new Exception("Unknown decoding state: " + state());
		}
	}

	private String decode(String str) throws Exception {
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
			byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(str));
			return new String(decryptOut);
	}
	
	public static int LITTLEENDIAN(byte[] b) {
		int t = 0;
		if (b.length != 4) {
			return 0;
		}
		t = 0xff & b[0];
		t = t | (0xff & b[1]) << 8;
		t = t | (0xff & b[2]) << 16;
		t = t | (0xff & b[3]) << 24;
		return t;
	}

	private void reset() {
		checkpoint(DecodingState.TYPE);
		this.msg = new Msg();
	}

	public enum DecodingState {
		TYPE, LENGTH, MSG
	}

	public static int BIGENDIAN(byte[] b) {
		int t = 0;

		if (b.length != 4) {
			return 0;
		}
		t = 0xff & b[0] << 24;
		t = t | (0xff & b[1]) << 16;
		t = t | (0xff & b[2]) << 8;
		t = t | (0xff & b[3]);
		return t;
	}
}
