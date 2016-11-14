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

	public SourceLoader() {
		list = new HashSet<Coding>();
		list = readSourceFile();
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
			
			int count = 0;
			
			while ((line = reader.readLine()) != null) {
				
				count++;

				String[] values = new String[8];
				values = line.split("\t");

				// Check if line has all fields filled (incl. source). 
				if (values.length == 8)
				{
					int party = Integer.parseInt(values[1]);
					int question = Integer.parseInt(values[3]);
					String source = values[7];

					Coding coding = new Coding(party, question, source.trim());
					
					// Check if value already exists
					boolean duplicate = false;
					for(Coding storedCoding : list)
					{
						boolean sameParty = party == storedCoding.getParty();
						boolean sameQuestion = question == storedCoding.getQuestion();
						boolean sameSource = source.equals(storedCoding.getSource());
						
						if(sameParty && sameQuestion && sameSource) {
							duplicate = true;
							break;
						}
					}
					
					if(!duplicate){
						list.add(coding);
					}
						
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
		System.out.println("Sources Files loaded.");
		
		return this.list;
	}

	public Set<Coding> getCodings() {
		return this.list;
	}

	public int getCodingCount() {
		return this.list.size();
	}

}
