package info.stuber.fhnw.thesis.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class SourceLoader {

	private Set<Coding> list = null;
	int sameSourceCount = 0;
	int sameEverythingCount = 0;

	public SourceLoader() {
		list = new HashSet<Coding>();
		list = readSourceFile();
	}

	public static void main(String[] args) {
		SourceLoader loader = new SourceLoader();
		loader.print();

	}

	private Set<Coding> readSourceFile() {

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
					String source = values[7].trim();
					boolean existing = false;

					// check if url is already known
					for (Coding existingCoding : this.list) {
						if (source.equals(existingCoding.getSource())) {
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
						this.list.add(new Coding(party, question, source));
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}

		System.out.println("Sources Files loaded.");

		return this.list;
	}

	public void print() {
		for (Coding coding : this.list) {
			coding.printDebug();
		}
		System.out.println();
		System.out.println("Total: " + getCodingCount());
	}

	public Set<Coding> getCodings() {
		return this.list;
	}

	public int getCodingCount() {
		return this.list.size();
	}

}
