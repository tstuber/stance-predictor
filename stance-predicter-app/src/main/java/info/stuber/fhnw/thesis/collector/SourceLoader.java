package info.stuber.fhnw.thesis.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class SourceLoader implements ISourceLoader {

	private List<Coding> codings = null;
	int sameSourceCount = 0;
	int sameEverythingCount = 0;

	public SourceLoader() {
		this.codings = new ArrayList<Coding>();
		this.codings = readSourceFile();
	}
	
	public SourceLoader(List<Coding> codings) {
		this.codings = codings;
	}

	public static void main(String[] args) {
		ISourceLoader loader = new SourceLoader();
		loader.print();
		 System.out.println("Try to find Duplicate"); 
		
		 
		 List<Coding> codings = loader.getCodings();
		Set<String> names = new HashSet<String>();
		for (Coding coding:codings) {
		  if (names.contains(coding.getSourceUrl().toString())) {
		   coding.printDebug();
		  } else {
		    names.add(coding.getSourceUrl().toString());
		    System.out.println(coding.getSourceUrl().toString());
		  }
		}

	}

	private List<Coding> readSourceFile() {

		InputStream inputStream = null;
		BufferedReader reader = null;
		String path = GetConfigPropertyValues.getProperty("path_codingurls");
		
		try {

			System.out.println("Try to load Source File");

			inputStream = PdfCache.class.getClassLoader().getResourceAsStream(path);
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

			String line;

			// skip first line with headers.
			reader.readLine();

			while ((line = reader.readLine()) != null) {

				String[] values = new String[8];
				values = line.split("\t");

				// Check if line has all fields filled (incl. source).
				if (values.length == 8) {
					int party = Integer.parseInt(values[1]);
					int question = Integer.parseInt(values[3]);
					URL source = null;
					
					try {
						source = new URL(values[7].trim());	
					} catch (MalformedURLException mal) {
						String error = "URL: " + source + " Party:"+party+" Question:"+question;
						System.out.println(error);
						continue;
					}
					
					boolean existing = false;

					// check if url is already known
					for (Coding existingCoding : this.codings) {
						if (source.equals(existingCoding.getSourceUrl())) {
							existing = true;

							if (!existingCoding.containsParty(party)) {
								existingCoding.addParty(party);
							}

							if (!existingCoding.containsQuestion(question)) {
								existingCoding.addQuestion(question);
							}
						}
					}

					if (!existing) {
						this.codings.add(new Coding(party, question, source));
					}
				}
			}
		} 
		
		catch (Exception ex) {
			System.out.println(ex);
		}

		System.out.println("Sources Files loaded.");

		return this.codings;
	}

	/* (non-Javadoc)
	 * @see info.stuber.fhnw.thesis.collector.ISourceLoader#print()
	 */
	@Override
	public void print() {
		for (Coding coding : this.codings) {
			coding.printDebug();
		}
		System.out.println();
		System.out.println("Total: " + getCodingCount());
	}

	/* (non-Javadoc)
	 * @see info.stuber.fhnw.thesis.collector.ISourceLoader#getCodings()
	 */
	@Override
	public List<Coding> getCodings() {
		return this.codings;
	}

	/* (non-Javadoc)
	 * @see info.stuber.fhnw.thesis.collector.ISourceLoader#getCodingCount()
	 */
	@Override
	public int getCodingCount() {
		return this.codings.size();
	}

}
