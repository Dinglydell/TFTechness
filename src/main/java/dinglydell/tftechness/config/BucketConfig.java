package dinglydell.tftechness.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import dinglydell.tftechness.TFTechness;

public class BucketConfig {
	
	public static ArrayList<String> upsideDown = new ArrayList<String>();
	public static ArrayList<String> overflowing = new ArrayList<String>();
	
	private static final String bucketDir = "/Buckets";
	
	private static final String upsideDownDir = "/Inverted";
	
	private static final String overflowingDir = "/Overflowing";
	
	public static void loadConifg(String dirString) {
		File bucket = new File(dirString + bucketDir);
		if (!bucket.exists()) {
			bucket.mkdir();
		}
		
		File dir = new File(dirString + bucketDir + upsideDownDir);
		addFiles(dir,
				upsideDown,
				"inverted",
				new String[] {
						"# The list of fluids that have upside-down buckets, such as energised glowstone from ThermalExpansion.",
						"# Any .txt files in this folder will be read, along with any files in any subfolders.",
						"glowstone",
						"aerotheum"
				});
		dir = new File(dirString + bucketDir + overflowingDir);
		addFiles(dir, overflowing, "overflowring", new String[] {
				"# The list of fluids that have overflowing buckets, such as blazing pyrotheum from ThermalExpansion.",
				"# The back of the bucket texture will not be visible",
				"# Any .txt files in this folder will be read, along with any files in any subfolders.",
				"pyrotheum"
		});
	}
	
	private static void addFiles(File dir, ArrayList<String> list, String type, String[] defaultList) {
		if (!dir.exists()) {
			dir.mkdir();
		}
		ArrayList<File> files = getFiles(dir);
		if (files == null || files.size() == 0) {
			TFTechness.logger.info("No " + type + " bucket config files found. Creating default...");
			File defaultFile = new File(dir, "default.txt");
			try {
				defaultFile.createNewFile();
				PrintWriter out = new PrintWriter(new FileWriter(defaultFile));
				
				for (String s : defaultList) {
					out.println(s);
				}
				out.flush();
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			list.addAll(Arrays.asList(defaultList));
		} else {
			for (File f : files) {
				if (f.isDirectory()) {
					files.addAll(getFiles(f));
				} else {
					try {
						BufferedReader in = new BufferedReader(new FileReader(f));
						String str;
						while ((str = in.readLine()) != null) {
							if (!str.startsWith("#")) {
								list.add(str);
							}
						}
						in.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public static ArrayList<File> getFiles(File dir) {
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File f, String filename) {
				return filename.endsWith(".txt");
			}
		});
		if (files == null) {
			return null;
		}
		return new ArrayList<File>(Arrays.asList(files));
	}
}
