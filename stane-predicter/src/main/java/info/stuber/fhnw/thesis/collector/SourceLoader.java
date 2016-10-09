package info.stuber.fhnw.thesis.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class SourceLoader {

	private static final String CODING_EXPORT = "C:\\Users\\Thomas\\OneDrive\\FHNW\\Semester 5\\MT\\04_Data\\coding_exports_UK_clean_stuber.txt";

	private Set<String> list = null;

	public SourceLoader() {
		list = new HashSet<String>();
	}

	public void readSourceFile() {
		File f = new File(CODING_EXPORT);
		BufferedReader reader = null;

		try {

			System.out.println("Try to load Source File");
			
			FileInputStream fis = new FileInputStream(f);
			reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

			String line;

			while ((line = reader.readLine()) != null) {

				String[] values = new String[8];
				values = line.split("\t");

				if (values.length == 8)
					list.add(values[7]);

			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public Set<String> getSources() {
		return this.list;
	}

	public int getSourceCount() {
		return this.list.size();
	}

}
