package info.stuber.fhnw.thesis.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
		// loader.print();

	}

	private List<Coding> readSourceFile() {

		File f = new File(GetConfigPropertyValues.getProperty("path_codingurls"));
		BufferedReader reader = null;

		try {

			System.out.println("Try to load Source File");

			FileInputStream fis = new FileInputStream(f);
			reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

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
