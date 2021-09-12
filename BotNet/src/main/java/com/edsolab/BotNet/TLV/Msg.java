package com.edsolab.BotNet.TLV;

public class Msg {

	private String type = null;
	private int lenKey = 0;
	private byte[] key = null;

	public int getLenKey() {
		return lenKey;
	}

	public void setLenKey(int lenKey) {
		this.lenKey = lenKey;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
