package com.edsolab.BotNet.Client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	private Bootstrap bootstrap = new Bootstrap();
	private SocketAddress addr;
	private Channel channel;
	private Timer timer;

	public NettyClient(String host, int port, Timer timer) {
		this(new InetSocketAddress(host, port), timer);
	}

	public NettyClient(SocketAddress addr, Timer timer) {
		this.addr = addr;
		this.timer = timer;
		bootstrap.group(new NioEventLoopGroup());
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new RequestDataEncoder(), new ResponseDataDecoder(), new ClientHandler());
			}
		});
		scheduleConnect(10);
	}

	private void doConnect() {
		try {
			ChannelFuture f = bootstrap.connect(addr);
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {// if is not successful, reconnect
						future.channel().close();
						bootstrap.connect(addr).addListener(this);
					} else {// good, the connection is ok
						channel = future.channel();
						// add a listener to detect the connection lost
						addCloseDetectListener(channel);

					}
				}

				private void addCloseDetectListener(Channel channel) {
					// if the channel connection is lost, the
					// ChannelFutureListener.operationComplete() will be called
					channel.closeFuture().addListener(new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							scheduleConnect(5);
						}

					});

				}
			});
		} catch (Exception ex) {
			scheduleConnect(1000);

		}
	}

	private void scheduleConnect(long millis) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				doConnect();
			}
		}, millis);
	}

	public static void main(String... args) {
		new NettyClient("localhost", 8082, new Timer());
	}
}
