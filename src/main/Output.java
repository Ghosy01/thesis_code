package main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Output implements Serializable {

	
	//This class that outputs the resulting HashMap for saving
	
	private Object map = new HashMap<Integer, String>();
	private String path;
	private String filename;
	
	
	public Output(Object map, String path, String filename) {

		this.map = map;
		this.path = path;
		this.filename = filename;

	}
	
	
	public void createBinary() throws IOException {
		
		FileOutputStream fileOutputStream = new FileOutputStream(path + filename);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(map);
		objectOutputStream.close();
		
		
	}
	
	
}
