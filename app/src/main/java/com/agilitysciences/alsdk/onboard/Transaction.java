package com.agilitysciences.alsdk.onboard;

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
public Signature getSig() {
	return $sigs;
}
public void setSig(Signature sig) {
	this.$sigs = sig;
}
@Override
public String toString() {
	return "Transaction [$tx=" + $tx + ", $selfsign=" + $selfsign + ", sig=" + $sigs + "]";
}




}
