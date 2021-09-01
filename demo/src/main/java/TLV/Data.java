package TLV;

public class Data {
	private String tag;
	private int lenKey;
	private byte[] key;
	private int lenData;
	private byte[] data;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getLenKey() {
		return lenKey;
	}

	public void setLenKey(int lenKey) {
		this.lenKey = lenKey;
	}

	public int getLenData() {
		return lenData;
	}

	public void setLenData(int lenData) {
		this.lenData = lenData;

	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
