package Client;

import java.util.List;

import TLV.Msg;
import TLV.Tlv;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class ResponseDataDecoder extends ReplayingDecoder<Msg> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
	  Tlv tlv = new Tlv();
      out.add(tlv.readMsg(in));
  }
}