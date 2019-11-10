package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Main3 {

    private Main3() {}

    public static void compressGZIP(File input, File output) throws IOException {
        try (GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(output))){
            try (FileInputStream in = new FileInputStream(input)){
                byte[] buffer = new byte[1024];
                int len;
                while((len=in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
            }
        }
    }

    public static void decompressGzip(File input, File output) throws IOException {
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(input))){
            try (FileOutputStream out = new FileOutputStream(output)){
                byte[] buffer = new byte[1024];
                int len;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {

		String originFolder = "C://origin/";
		String targetFolder = "C://target2/";

		String[] images = { "disk_image_orig.bin", "disk_image_rev1.bin", "disk_image_rev2.bin",
				"disk_image_rev3.bin" };

		String[] dict = { "disk_image_orig.dict", "disk_image_rev1.dict", "disk_image_rev2.dict",
				"disk_image_rev3.dict" };

		String[] caches = { "disk_image_orig.bin.cache", "disk_image_rev1.bin.cache", "disk_image_rev2.bin.cache",
				"disk_image_rev3.bin.cache" };
		String[] dictCaches = { "disk_image_orig.bin.cache.dict", "disk_image_rev1.bin.cache.dict",
				"disk_image_rev2.bin.cache.dict", "disk_image_rev3.bin.cache.dict" };

		compressGZIP(new File(originFolder + images[1]), new File(targetFolder + "new.out"));
		
	}

}
