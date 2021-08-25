package Client;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

import TLV.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Msg msg = new Msg();
        int tag = 123;
        String value = "hello wold";
        msg.setType(tag);
        msg.setMsg(value);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	Msg responseData = (Msg) msg;
    	System.out.println(responseData.getType()+": "+responseData.getMsg());
    }
}