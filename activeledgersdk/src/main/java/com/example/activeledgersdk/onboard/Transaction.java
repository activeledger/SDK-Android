package com.example.activeledgersdk.onboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

private Tx $tx;
private boolean $selfsign;
private Signature $sigs;
public Tx get$tx() {
	return $tx;
}
public void set$tx(Tx $tx) {
	this.$tx = $tx;
}
public boolean is$selfsign() {
	return $selfsign;
}
public void set$selfsign(boolean $selfsign) {
	this.$selfsign = $selfsign;
}


	public Signature get$sigs() {
		return $sigs;
	}

	public void set$sigs(Signature $sigs) {
		this.$sigs = $sigs;
	}
}
