package com.edsolab.BotNet.Client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.edsolab.BotNet.TLV.Msg;
import com.edsolab.BotNet.TLV.MsgStruct;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestDataEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msgO, ByteBuf out) throws Exception {
		Msg msg = (Msg) msgO;
		encodeMsg(msg, out);
	}

	private void encodeMsg(Msg msg, ByteBuf out) {
		byte[] type = msg.getType().getBytes();
		byte[] key = msg.getKey();
		int lenK = key.length;
		byte[] SendMsg = new byte[MsgStruct.B_TYPE_LENGTH + MsgStruct.B_KEY_LENGTH + lenK];
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
		try {
			out.writeBytes(SendMsg);
		} catch (Exception e) {
			System.out.println("Error writing message");
			e.printStackTrace();
		}
	}
}