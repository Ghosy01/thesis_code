package main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiskImage {
	
	
	private String filePath;
	
	private HashMap<Integer,String> myMap = new HashMap<Integer, String>();
	
	
	/*
	 * Creates a class that contains a file, this file can then be split or read to create hashes
	 */
	public DiskImage(String path) {
		filePath = path;
	}
	
	
	private String checkSum () throws NoSuchAlgorithmException, IOException {
		
		MessageDigest digest = MessageDigest.getInstance("MD5");
		File image = new File(filePath);

	    //Get file input stream for reading the file content
	    FileInputStream fis = new FileInputStream(image);
	     
	    //Create byte array to read data in chunks
	    byte[] byteArray = new byte[1024];
	    int bytesCount = 0;
	      
	    //Read file data and update in message digest
	    while ((bytesCount = fis.read(byteArray)) != -1) {
	        digest.update(byteArray, 0, bytesCount);
	    };
	     
	    //close the stream
	    fis.close();
	    byte[] bytes = digest.digest();
	     
	    //This bytes[] has bytes in decimal format;
	    //Convert it to hexadecimal format
	    StringBuilder sb = new StringBuilder();
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	
	   return sb.toString();
	}

	public String getFileCheckSum() throws NoSuchAlgorithmException, IOException {

		String a = checkSum();

		return a;
	}
	
	
	//Creates a dictionary for the input file
	//Deprecated

	public void  createMap() throws IOException, NoSuchAlgorithmException {
		
		HashMap <Integer, String> map = new HashMap<Integer,String>();
		
		InputStream in = new FileInputStream(filePath);
		 
		byte [] buffer = new byte[4096];
		
		int i = 0;
		
		while(in.read(buffer) != -1) {
			HashFile hash = new HashFile(buffer);
			map.put(i, hash.hashFile());
			i=i+1;
		}
		
		in.close();
		
		myMap = map;
	}
	
	public void  createMapNew() throws IOException, NoSuchAlgorithmException {
		
		HashMap <Integer, String> map = new HashMap<Integer,String>();
		
		InputStream in = new FileInputStream(filePath);
		
		byte [] buffer = new byte[2048];
		
		int i = 0;
		
		while(in.read(buffer) != -1) {
			HashFile hash = new HashFile(buffer);
			
			if (map.containsValue(hash.hashFile())== false) {
			//System.out.println(hash.hashFile());
			map.put(i, hash.hashFile());
			i=i+1;
			continue; }
			
			//i = i + 1;
		}
		
		in.close();
		
		myMap = map;
	}
	
	//Returns the Map for the disk file
	
	public HashMap<Integer, String> returnMap () {
		return myMap;
	}
	
	
	public boolean checkArrays ( HashMap<Integer, String> dictionary , String hash) {
		
		if (dictionary.containsValue(hash)) {
		
		return true;
		}
		
		return false;
	}
	
	

}
