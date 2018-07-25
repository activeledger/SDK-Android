package com.agilitysciences.alsdk.onboard;

public class Identity {

	private String type;
	private String publicKey;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	@Override
	public String toString() {
		return "Identity [type=" + type + ", publicKey=" + publicKey + "]";
	}
	
	
	
}
