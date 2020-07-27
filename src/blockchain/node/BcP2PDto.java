package blockchain.node;

import java.io.Serializable;

public class BcP2PDto implements Serializable {
	private String header;
	private Object data;
	
	public BcP2PDto() {
		
	}
	
	public BcP2PDto(String header, Object data) {
		this.header = header;
		this.data = data;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
