package TLV;

public class Msg {

	private String type = null;
	private int lenKey = 0;
	private byte[] key = null;
	private int lenMsg = 0;
	private byte[] msg = null;

	public int getLenKey() {
		return lenKey;
	}

	public void setLenKey(int lenKey) {
		this.lenKey = lenKey;
	}

	public int getLenMsg() {
		return lenMsg;
	}

	public void setLenMsg(int lenMsg) {
		this.lenMsg = lenMsg;
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

	public byte[] getMsg() {
		return msg;
	}

	public void setMsg(byte[] msg) {
		this.msg = msg;
	}

}
