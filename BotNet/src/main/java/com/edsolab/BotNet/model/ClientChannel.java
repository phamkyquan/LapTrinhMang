package com.edsolab.BotNet.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clients_online")
public class ClientChannel {
	@Id
	private String id;
	private String ip;
	private String status;
	
	public ClientChannel() {
		super();
	}
	public ClientChannel(String id, String ip, String status) {
		super();
		this.id = id;
		this.ip = ip;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
