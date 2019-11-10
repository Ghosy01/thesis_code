package main;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// CHUNK IS COMPLETE
public class Chunk implements Serializable {
	
	//A 4 kb chunk is either an array of bytes or a position in one of the cache files.
	
	private int position;
	private String dictionary;
	
	/**
	 * Creates a new Chunk
	 * @param position is the position in the dictionary this chunk points to
	 * @param dictionary name of the dictionary does not include path
	 */
	public Chunk (int position, String dictionary) {
		
		this.position = position;
		this.dictionary = dictionary;
		
	}

	public String givePath() {
		return dictionary;
	}
	
	public int givePosition() {
		return position;
	}
	
	
}
