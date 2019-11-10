package main;

import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Input {
	
	private String input;
	
	
	public Input (String path) {
		
		input = path;
		
	}
	
	public List readList () throws ClassNotFoundException, IOException {
		FileInputStream fileInputStream = new FileInputStream(input);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

		List<Chunk> list =  (List<Chunk>) objectInputStream.readObject();
		objectInputStream.close();

		return list;
	}
	
	
	public HashMap readBinary () throws IOException, ClassNotFoundException {
		
		FileInputStream fileInputStream = new FileInputStream(input);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

		HashMap myNewlyReadInMap = (HashMap) objectInputStream.readObject();
		objectInputStream.close();

		return myNewlyReadInMap;
	
	}

}
