package com.agilitysciences.alsdk.onboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tx {

	private String $namespace;
	private String $contract;
	
	private IdentityList $i;

	
	public String get$namespace() {
		return $namespace;
	}
	public void set$namespace(String $namespace) {
		this.$namespace = $namespace;
	}
	public String get$contract() {
		return $contract;
	}
	public void set$contract(String $contract) {
		this.$contract = $contract;
	}
	@JsonProperty("$i")
	public IdentityList getIdentity() {
		return $i;
	}
	public void setIdentity(IdentityList identity) {
		this.$i = identity;
	}
	@Override
	public String toString() {
		return "Tx [$namespace=" + $namespace + ", $contract=" + $contract + ", identity=" + $i + "]";
	}

	

}
