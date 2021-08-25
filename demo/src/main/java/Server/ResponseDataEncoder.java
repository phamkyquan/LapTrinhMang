package Server;

import TLV.Msg;
import TLV.Tlv;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseDataEncoder extends MessageToByteEncoder<Msg> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
	  Tlv tlv = new Tlv();
	  tlv.writeMsg(msg.getMsg(), msg.getType(), out);
  }
}