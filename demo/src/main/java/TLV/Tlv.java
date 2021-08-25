package TLV;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

public class Tlv {

	public void writeMsg(String msg, int type, ByteBuf out) {

		byte[] msgB = msg.getBytes(StandardCharsets.UTF_8);
		int len = msgB.length;

		//
		byte[] by = new byte[4];
		ByteBuffer bb = ByteBuffer.wrap(by);
		 bb.order (ByteOrder.LITTLE_ENDIAN); // Use little-endian order here

		byte[] SendMsg = new byte[4 + 4 + len]; // int(type) + int(len) + msg.len
		int i = 0;

		 // Write type
		bb.asIntBuffer().put(type);
		for (i = 0; i < by.length; i++) {
			SendMsg[i] = by[i];
		}

		 bb.asIntBuffer (). put (0); // Empty

		 // write length
		bb.asIntBuffer().put(len);//
		int j = i;
		for (int k = 0; k < by.length; j++, k++) {
			SendMsg[j] = by[k];
		}

		 // write message
		int n = j;
		for (int k = 0; k < msgB.length; k++, n++) {
			SendMsg[n] = msgB[k];
		}
		try {
			out.writeBytes(SendMsg);
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println ("Error writing message");
			e.printStackTrace();
		}

	}

	public Msg readMsg(ByteBuf in) {
		
		Msg msg = new Msg();

		byte[] typeB = new byte[4];

		byte[] lenB = new byte[4];

		try {
			
			in.readBytes(typeB);
			msg.setType(LITTLEENDIAN(typeB));

			in.readBytes(lenB);
			msg.setLen(LITTLEENDIAN(lenB));

			byte[] rmsg = new byte[msg.getLen()];
			in.readBytes(rmsg);
			String msgS = new String(rmsg, StandardCharsets.UTF_8);
			msg.setMsg(msgS);
			return msg;
		} catch (Exception e) {
			 System.out.println ("Error reading data");
			e.printStackTrace();
		}

		return msg;
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

}