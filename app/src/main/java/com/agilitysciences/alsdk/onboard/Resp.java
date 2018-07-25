package com.agilitysciences.alsdk.onboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resp {

	private String $umid;

	public String get$umid() {
		return $umid;
	}

	public void set$umid(String $umid) {
		this.$umid = $umid;
	}

	@Override
	public String toString() {
		return "Resp [$umid=" + $umid + "]";
	}
	
	
}
