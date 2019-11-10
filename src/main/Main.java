package main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.DeflaterOutputStream;

//import org.apache.commons.lang3.time.StopWatch;



public class Main {
	
	
	private static String dir = "C://files_thesis/";
	private static String file_suffix = ".split";
	private static String dicPath = "C://files_dict/";
	private static String c_path = "C://";
	private static HashMap<String,String> tree = new HashMap<String, String>(); 
	
	
	public static List splitFile(String name, int mBperSplit) throws IOException {

		if (mBperSplit <= 0) {

			throw new IllegalArgumentException(" mBperSplit must be greater than zero ");
		}

		List partFiles = new ArrayList();

		final long sourceSize = new File(name).length();

		final long bytesPerSplit = 1024L * mBperSplit;
		final long numSplits = sourceSize / bytesPerSplit;
		long remainingBytes = sourceSize % bytesPerSplit;
		RandomAccessFile raf = new RandomAccessFile(name, "r");
		int maxReadBufferSize = 4 * 1024; // 4 kilobytes each file
		int partNum = 0;
		
		
		for (; partNum < numSplits; partNum++) {
			BufferedOutputStream bw = newWriteBuffer(partNum, partFiles);
			if (bytesPerSplit > maxReadBufferSize) {
				long numReads = bytesPerSplit / maxReadBufferSize;
				long numRemainingRead = bytesPerSplit % maxReadBufferSize;
				for (int i = 0; i < numReads; i++) {
					readWrite(raf, bw, maxReadBufferSize);
				}
				if (numRemainingRead > 0) {
					readWrite(raf, bw, numRemainingRead);
				}
			} else {
				readWrite(raf, bw, bytesPerSplit);
			}
			bw.close();
		}
		if (remainingBytes > 0) {
			BufferedOutputStream bw = newWriteBuffer(partNum, partFiles);
			readWrite(raf, bw, remainingBytes);
			bw.close();
		}
		raf.close();
		return partFiles;
	}

	private static BufferedOutputStream newWriteBuffer(int partNum, List partFiles) throws IOException {
		String partFileName = dir + "part" + partNum + file_suffix;
		partFiles.add(partFileName);
		return new BufferedOutputStream(new FileOutputStream(partFileName));
	}

	private static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
		byte[] buf = new byte[(int) numBytes];
		int val = raf.read(buf);
		if (val != -1) {
			bw.write(buf);
		}
	}
	
	
	private static String hashFile(String filename) throws NoSuchAlgorithmException, IOException {
		
		
		File file = new File (filename);
		
		MessageDigest complete = MessageDigest.getInstance("SHA-256");
		
		byte [] fileContent = complete.digest(Files.readAllBytes(file.toPath()));
		
		StringBuilder sb = new StringBuilder (fileContent.length * 2);
		
		for (byte b : fileContent) {
			
			sb.append(String.format("%02x", b));
		}
		
		

		return sb.toString();

	}
	
	
	
	
	
	
	/*
	public static void putHash(String a, String b) {
		
		tree.put(a, b);
	}
	
	public static void getDigest(String a) {
		
		tree.get(a);
	}*/
	
	
	//DEPRECATED
	public static void writeHash(HashMap<String, String> a, String path, String name) throws IOException {

		FileOutputStream fileOutputStream = new FileOutputStream(path + name);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(a);
		objectOutputStream.close();

	}
	
	//DEPRECATED

	public static Map readHash(String a) throws IOException, ClassNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(a);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

		Map myNewlyReadInMap = (HashMap) objectInputStream.readObject();
		objectInputStream.close();

		return myNewlyReadInMap;

	}
	
	public static void outputDeflate(String a, String b) throws IOException {

		FileInputStream fin = new FileInputStream(a);
		FileOutputStream fout = new FileOutputStream(b);
		DeflaterOutputStream out = new DeflaterOutputStream(fout);
		
		int data;
		
		  while ((data=fin.read())!=-1) 
	        { 
	            out.write(data); 
	        } 
		fin.close();
		out.close();
	}
	
/*	private static void merge (String folder, String output) throws IOException{
		
		File dir = new File(folder);
		
		PrintWriter pw = new PrintWriter(output);
		
		String[] fileNames = dir.list();
		
		for (String fileName : fileNames) {
			
			File f = new File(dir, fileName);
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			
		
		}
		
		
		
	}
*/
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {

		/*
		 * dir = "C://files_gentoo/"; System.out.println("splitting gentoo"); splitFile
		 * ("C://image_files/livedvd-amd64-multilib-20160704.iso",4); dir =
		 * "C://files_centOS/"; System.out.println("splitting cent0S"); splitFile
		 * ("C://image_files/CentOS-7-x86_64-DVD-1810.iso",4);
		 */
	

		
		
		HashMap<String, String> orig = new HashMap<String, String>();
		HashMap<String, String> dest = new HashMap<String, String>();
		HashMap<String, String> dest2 = new HashMap<String, String>();
			
		
/*		File dir = new File ("C:/files_fedora");
		
		String [] files = dir.list();
		
		
		for (String a : files) {
			
			String hash = hashFile("C:/files_fedora/" + a);
			dest.put(a, hash);

		}
		
		writeHash(dest, dicPath, "fedora_dict.dir");
		*/
		//this is to create dir on the other side
		
		boolean x = true;
	/*	
		orig = (HashMap<String, String>) readHash("C://files_dict/" + "fedora_dict.dir");
		dest = (HashMap<String, String>) readHash("C://files_dict/" + "ubuntu_dict.dir");
		dest2 = (HashMap<String, String>) readHash("C://files_dict/" + "mint_dict.dir");

		
		for (Map.Entry map : orig.entrySet()) {

			if (dest.containsValue(map.getValue()) == false) {
				// dest.put((String)map.getKey(), (String)map.getValue());
				if (dest2.containsValue(map.getValue()) == false) {
					Files.copy(new File("C://files_fedora/" + (String) map.getKey()).toPath(),
							new File("C://files_transfer_fedora/" + (String) map.getKey()).toPath(),
							StandardCopyOption.REPLACE_EXISTING);
				}
			}

		}*/
		
		File folder = new File("C:/files_transfer_mint");
		String[] files = folder.list();
		
		for (String a : files) {
			outputDeflate("C:/files_transfer_mint/"+ a,"C:/files_deflate_mint/"+ a);
		}
		
		
		
		
		//writeHash(dest, dicPath, "dic_mint.dir");
		
		
		/*do {
		Scanner a = new Scanner(System.in) ;
		
		System.out.println("show file");
		
		String b = a.nextLine();
		
		if (b.compareTo("exit") == 0) {
			x = false;
			System.out.println("exit succesfully");
		}
		
		System.out.println(dict.get(b));
		
		
		}while(x = true);*/
		
		System.out.println("Done!");
		
	
	
		

}
}

