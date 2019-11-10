package main;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;

public class HashFile {
	
	private static byte[] myBytes;
	
	
	public HashFile (byte[] bytes) {
		myBytes = bytes;
	}
	

	
	/*
	 * Methods for chunk handling
	 */
	
	public String hashFile () throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return byteArray2Hex(md.digest(myBytes));
		
	}
	
	private String byteArray2Hex(byte[] hash) {

		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();

	}

	
	public void setBytes(byte[] newBytes) {
		
		myBytes = newBytes;
		
	}
}
