package com.edsolab.BotNet;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.edsolab.BotNet.Server.NettyServer;

@Component
public class NettyServerStarter implements CommandLineRunner {

	protected final Log logger = LogFactory.getLog(getClass());
	private int port = 8082;

	@Override
	public void run(String... args) throws Exception {
		startNettyServer();
		logger.info("Netty Server Started at port " + port);
	}

	private void startNettyServer() throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new NettyServer(port).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
