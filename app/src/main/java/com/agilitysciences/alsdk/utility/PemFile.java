package com.agilitysciences.alsdk.utility;

import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

//import org.bouncycastle.util.io.pem.PemObject;
//import org.bouncycastle.util.io.pem.PemReader;

public class PemFile {
	private PemObject pemObject;
	 
	public PemFile(File filename) throws FileNotFoundException, IOException {
		PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(filename)));
		try {
			this.pemObject = pemReader.readPemObject();
		} finally {
			pemReader.close();
		}
	}
 
	public PemObject getPemObject() {
		return pemObject;
	}
}
