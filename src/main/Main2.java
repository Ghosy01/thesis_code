package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Main2 {
	

	//define input hashlist
	
	/*String path = "C://myFiles/";
	
	File directory = new File(path);
	
	String[] files = directory.list();
	
	FileOutputStream out = new FileOutputStream("C://confix/new2.txt");

	
	for (String a : files) {
		
		File current = new File("C://myFiles/" + a);
		
		System.out.println(a);
		byte [] bytes = Files.readAllBytes(current.toPath());
		
		out.write(bytes);
		
	}
	*/
	
/*	String output = "dsajnda2k1sjndfkjsandasd";
	
	String[] position = {"4","C://myFiles/cache2.bin" };
	
	byte[] bytes = output.getBytes();
	
	
	Chunk byteChunk = new Chunk (bytes);
	
	Chunk positionChunk = new Chunk (Integer.valueOf(position[0]),position[1]);
	
	Construct build = new Construct("C://confix/output.txt");
	
	
	for (int i = 0 ; i < 100 ; i ++) {
		
		if (i % 2 == 0) {
			
			build.addChunk(byteChunk);
			continue;
		}
		
		build.addChunk(positionChunk);

		
	}
	
	build.constructFile();*/
	

	
	
	public static void main (String [] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException, InterruptedException {
		
	
		String originFolder = "C://origin/";
		String targetFolder = "C://target2/";
		
		String[] images = {"disk_image_orig.bin",
								"disk_image_rev1.bin",
								"disk_image_rev2.bin",
								"disk_image_rev3.bin"
		};
		
		String[] dict = {"disk_image_orig.dict",
				"disk_image_rev1.dict",
				"disk_image_rev2.dict",
				"disk_image_rev3.dict"
};
		
		String[] caches = {"disk_image_orig.bin.cache",
				"disk_image_rev1.bin.cache",
				"disk_image_rev2.bin.cache",
				"disk_image_rev3.bin.cache"
};
		String[] dictCaches = {"disk_image_orig.bin.cache.dict",
				"disk_image_rev1.bin.cache.dict",
				"disk_image_rev2.bin.cache.dict",
				"disk_image_rev3.bin.cache.dict"
};
		
		
		
		ArrayList<String> other = new ArrayList<String>();
		//other.add(dictCaches[0]);
		//other.add(dictCaches[1]);
	
	 
		Construct trying = new Construct (images[3]);
		
		trying.deconstructFileNew(originFolder + images[3], other , targetFolder);
		
		/*Input in = new Input (targetFolder + "disk_image_rev1.bin.deconstruct");
		
		
		ArrayList<Chunk> ju = new ArrayList<Chunk>();
		
		ArrayList<String> no = new ArrayList<String>();
		
		trying.deconstructFileNew(originFolder + images[1], no, targetFolder);
		*/
		
		
		trying.constructFile(targetFolder + "disk_image_rev3.bin", (ArrayList<Chunk>) new Input (targetFolder + "disk_image_rev3.bin.reconstruct").readList());
		
		System.out.println(new DiskImage (originFolder + "disk_image_rev3.bin").getFileCheckSum());
		System.out.println(new DiskImage (targetFolder + "disk_image_rev3.bin").getFileCheckSum());
		
	}
}
