package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Construct {

	private String name;
	
	private ArrayList<String> dictionaries = new ArrayList<String>();
	
	public Construct (String output) {
		name = output;
	}

	

	public byte[] getBytesArray(int position, String file) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(4096);

		Path path = Paths.get(file);
		FileChannel fc = FileChannel.open(path);

		fc.position(position * 4096);

		fc.read(buffer);

		fc.close();

		return buffer.array();

	}
	
	public void addDict(String path) {
		
		dictionaries.add(path);
	}
	
	/**
	 * Returns Key for value 
	 * @param myList
	 * @param target
	 * @return
	 */
	
	private int existInList(HashMap<Integer,String> myList, String value) {
		
		for ( Entry < Integer , String> entry : myList.entrySet()) {
			if (entry.getValue().equals(value)) {
				//System.out.println(entry.getKey());
				return entry.getKey();
			}
		}
		
		
		
		return -1;
	}
	
	/**
	 * Deconstructs a file given a set of dictionaries 
	 * @param File to deconstruct
	 * @param path to puth resulting files
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchAlgorithmException 
	 */
	
	public void deconstructFile(String file,ArrayList<String> dictionaries, String outputPath) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
		
		//THIS METHOD IS DEPRECATED
		if (dictionaries.size() == 0) {
			System.out.println("No dictionaries given proceding to send file across as is");
			//TODO
		}
		
		FileOutputStream out = new FileOutputStream(outputPath + name + ".cache");
		DiskImage myFile = new DiskImage(file);
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
		myFile.createMap();
		HashMap <Integer, String> diskMap = myFile.returnMap();
		
		for(int i = 0 ; i < dictionaries.size() ; i ++) {
			
			HashMap<Integer,String> currentDict = new Input(dictionaries.get(i)).readBinary();
			System.out.println(i);
				for (Integer y : diskMap.keySet()) {
					
					//System.out.println(diskMap.get(y));
					if (currentDict.containsValue(diskMap.get(y))) {
						chunkList.add(new Chunk(existInList(currentDict, diskMap.get(y)), dictionaries.get(i)));
						continue;
					}
					//System.out.println("writing");
					
					out.write(getBytesArray(y,dictionaries.get(i)));
					
				}
			//free memory
			currentDict = null;
		}
		
		
		//Create ArrayList of Chunks
		Output myOut = new Output(chunkList,outputPath,name + ".deconstruct");
		myOut.createBinary();
		out.close();
		
		System.out.println("DONE!");
		
	}
	
	
	public void deconstructFileNew(String filePath,ArrayList<String> dictionaries, String outputPath) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InterruptedException {
		
		
		FileOutputStream out = new FileOutputStream(outputPath + name + ".cache");
		DiskImage myFile = new DiskImage(filePath);
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
		myFile.createMap();
		
		HashMap <Integer, String> diskMap = myFile.returnMap();
		//map to keep trach of chunks that are being written
		HashMap <Integer, String> sameMap = new HashMap<Integer,String> ();
		
		if (dictionaries.size() == 0) {
			//No dictionary
			System.out.println("doing 0");
			int counter = 0;
			
			for (Integer y : diskMap.keySet()) {

				if (sameMap.containsValue(diskMap.get(y))) {			
					chunkList.add(new Chunk (existInList(sameMap,diskMap.get(y)), outputPath + name + ".cache"));
					continue;
				}
				
				sameMap.put(counter, diskMap.get(y) );
				chunkList.add(new Chunk (existInList(sameMap,diskMap.get(y)), outputPath + name + ".cache"));
				counter = counter + 1;
				out.write(getBytesArray(y,filePath));;
				
				
			}
			
			//Create Dictionary
			Output writer = new Output (sameMap,outputPath, name + ".cache" + ".dict");
			writer.createBinary();
			
			//Create Reconstruction file
			
			writer = new Output (chunkList, outputPath, name + ".reconstruct");
			writer.createBinary();
			
			
		}

		if (dictionaries.size() == 1) {

			System.out.println("doing 1");
			HashMap <Integer, String> myDict = new Input (outputPath + dictionaries.get(0)).readBinary();

			int counter = 0;

			for (Integer i : diskMap.keySet()) {

				if (myDict.containsValue(diskMap.get(i))) {
					chunkList.add(new Chunk(existInList(myDict, diskMap.get(i)), outputPath + dictionaries.get(0).substring(0,dictionaries.get(0).length()-5)));
					continue;
				}
				
				if (sameMap.containsValue(diskMap.get(i))) {
					chunkList.add(new Chunk(existInList(sameMap, diskMap.get(i)), outputPath + name + ".cache" ));
					continue;
				}

					out.write(getBytesArray(i, filePath));
					chunkList.add(new Chunk(counter, outputPath + name + ".cache"));
					sameMap.put(counter, diskMap.get(i));
					counter = counter + 1;

				}
			
			
			// Create Dictionary
			Output writer = new Output(sameMap, outputPath, name + ".cache" + ".dict");
			writer.createBinary();

			// Create Reconstruction file

			writer = new Output(chunkList, outputPath, name + ".reconstruct");
			writer.createBinary();
			
		}

		
		if (dictionaries.size() > 1) {
			
			System.out.println("doing several");
			ArrayList<HashMap<Integer, String>> moreDicts = new ArrayList<HashMap<Integer,String>>();
			
			//create dictionaries
			for (int i = 0; i < dictionaries.size(); i++) {
				moreDicts.add(new Input(outputPath + dictionaries.get(i)).readBinary());
			}

			int counter = 0;

			for (Integer i : diskMap.keySet()) {

				if (sameMap.containsValue(diskMap.get(i))) {
					chunkList.add(new Chunk(existInList(sameMap, diskMap.get(i)), outputPath + name + ".cache"));
					continue;
				}

				int check = checkArrays(moreDicts, diskMap.get(i));

				if (check >= 0) {
					//System.out.println(check);
					chunkList.add(new Chunk(existInList(moreDicts.get(check), diskMap.get(i)),
							outputPath + dictionaries.get(check).substring(0, dictionaries.get(check).length() - 5)));
					continue;
				}

				out.write(getBytesArray(i, filePath));
				chunkList.add(new Chunk(counter, outputPath + name + ".cache"));
				sameMap.put(counter, diskMap.get(i));
				counter = counter + 1;

			}

			// Create Dictionary
			Output writer = new Output(sameMap, outputPath, name + ".cache" + ".dict");
			writer.createBinary();

			// Create Reconstruction file

			writer = new Output(chunkList, outputPath, name + ".reconstruct");
			writer.createBinary();

		}

	}
	
	private int checkArrays(ArrayList<HashMap<Integer, String>> input, String value) {

		for (int i = 0; i < input.size(); i++) {

			if (input.get(i).containsValue(value)) {
				return i;
			}

		}

		return -1;
	}
	/**
	 * Construct file from parameters into output folder
	 * @param file transferred binary file
	 * @param outPath created disk image reconstructed using caches
	 * @param list list that specifies where and which chunks to get
	 * @throws IOException
	 */
	
	public void constructFile(String outPath, ArrayList<Chunk> list) throws IOException {
		
		FileOutputStream out = new FileOutputStream (outPath);
		
		for (int i = 0 ; i < list.size() ; i ++) {
			
			byte[] buffer = getBytesArray(list.get(i).givePosition(), list.get(i).givePath());
			out.write(buffer);
			
		}
		
		out.close();
		
		
	}
	
	
}
