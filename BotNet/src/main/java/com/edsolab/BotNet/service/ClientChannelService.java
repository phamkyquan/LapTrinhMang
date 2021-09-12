package com.edsolab.BotNet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edsolab.BotNet.Server.ProcessingHandler;
import com.edsolab.BotNet.model.ClientChannel;
import com.edsolab.BotNet.repository.ClientChannelRepository;

@Service
public class ClientChannelService {

	@Autowired
	private ClientChannelRepository client;

	public List<ClientChannel> getAllClient() {
		return ProcessingHandler.ccs;
	}

	public Optional<ClientChannel> findClientById(String id) {
		return client.findById(id);
	}

	public void saveAllClient(String status) {
		for (ClientChannel cc : ProcessingHandler.ccs) {
			cc.setStatus(status);
		}
	}
	
	public void saveClientById(String status, String id) {
		for (ClientChannel cc : ProcessingHandler.ccs) {
			if(cc.getId().equals(id)) {
				cc.setStatus(status);
			}
		}
	}
}
