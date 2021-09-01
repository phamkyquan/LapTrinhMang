package Client;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

import TLV.Msg;
import TLV.MsgStruct;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class ResponseDataDecoder extends ReplayingDecoder<ResponseDataDecoder.DecodingState> {

	private Msg msg;

	public ResponseDataDecoder() {
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
			if (buffer.readableBytes() < MsgStruct.B_TYPE_LENGTH) {
				buffer.resetReaderIndex();
				return;
			}

			byte[] type = new byte[MsgStruct.B_TYPE_LENGTH];
			buffer.readBytes(type, 0, type.length);
			this.msg.setType(new String(type));
			checkpoint(DecodingState.LENGTH_KEY);
			break;

		case LENGTH_KEY:

			buffer.markReaderIndex();
			if (buffer.readableBytes() < MsgStruct.B_KEY_LENGTH) {
				buffer.resetReaderIndex();
				return;
			}

			byte[] lengthKey = new byte[MsgStruct.B_KEY_LENGTH];
			buffer.readBytes(lengthKey, 0, lengthKey.length);
			this.msg.setLenKey(LITTLEENDIAN(lengthKey));
			checkpoint(DecodingState.KEY);
			break;

		case KEY:
			int lengKey = this.msg.getLenKey();
			if (lengKey > 0) {
				buffer.markReaderIndex();
				if (buffer.readableBytes() < lengKey) {
					buffer.resetReaderIndex();
					return;
				}
			}
			byte[] key = new byte[lengKey];
			buffer.readBytes(key, 0, lengKey);
			this.msg.setKey(key);
			checkpoint(DecodingState.LENGTH_MSG);
			break;

		case LENGTH_MSG:

			buffer.markReaderIndex();
			if (buffer.readableBytes() < MsgStruct.B_MSG_LENGTH) {
				buffer.resetReaderIndex();
				return;
			}

			byte[] lengthMsg = new byte[MsgStruct.B_MSG_LENGTH];
			buffer.readBytes(lengthMsg, 0, lengthMsg.length);
			this.msg.setLenMsg(LITTLEENDIAN(lengthMsg));
			checkpoint(DecodingState.MSG);
			break;

		case MSG:
			int lengMsg = this.msg.getLenMsg();
			if (lengMsg > 0) {
				buffer.markReaderIndex();
				if (buffer.readableBytes() < lengMsg) {
					buffer.resetReaderIndex();
					return;
				}
			}
			byte[] msgM = new byte[lengMsg];
			buffer.readBytes(msgM, 0, lengMsg);
			this.msg.setMsg(msgM);
			out.add(this.msg);
			reset();
			break;

		default:
			throw new Exception("Unknown decoding state: " + state());
		}
	}

	private byte[] decode(byte[] str) throws Exception {
		// Tạo private key
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(KeyClient.privateKeyC.getEncoded());
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = factory.generatePrivate(spec);

		// Giải mã dữ liệu
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.DECRYPT_MODE, priKey);
		return c.doFinal(Base64.getDecoder().decode(str));
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
		TYPE, LENGTH_KEY, KEY, LENGTH_MSG, MSG
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