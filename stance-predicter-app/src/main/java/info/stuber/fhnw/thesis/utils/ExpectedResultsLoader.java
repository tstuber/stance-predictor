package info.stuber.fhnw.thesis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.collector.PdfCache;

public class ExpectedResultsLoader {

	static List<ExpectedResult> resultList;

	public ExpectedResultsLoader() {

	}

	public static List<ExpectedResult> getAllResults() {

		if (resultList == null)
			readSource();
		
		return resultList;
	}
	
	public static ExpectedResult getSingleResult(Party party, int questionId) {
		if (resultList == null)
			readSource();
		
		for(ExpectedResult expected : resultList) {
			if(expected.getParty() == party.getId() && expected.getQuestion() == questionId) {
				return expected;
			}
		}
		
		return null;
	}
	
	public static List<ExpectedResult> getResultsByParty(Party party) {
		if (resultList == null)
			readSource();
		
		List<ExpectedResult> resultsByParty = new ArrayList<ExpectedResult>();
		
		for(ExpectedResult expected : resultList) {
			if(expected.getParty() == party.getId()) {
				resultsByParty.add(expected);
			}
		}
		
		return resultsByParty;
	}
	
	public static List<ExpectedResult> getResultsByQuestion(int questionId) {
		if (resultList == null)
			readSource();
		
		List<ExpectedResult> resultsByQuestion = new ArrayList<ExpectedResult>();
		
		for(ExpectedResult expected : resultList) {
			if(expected.getQuestion() == questionId) {
				resultsByQuestion.add(expected);
			}
		}
		
		return resultsByQuestion;
	}


	private static void readSource() {
		System.out.println("Reading source files for expected results");
		resultList = new ArrayList<ExpectedResult>();
		
		String path = GetConfigPropertyValues.getProperty("path_codingresults");
		InputStream inputStream = null;
		BufferedReader reader = null;

		try {
			inputStream = PdfCache.class.getClassLoader().getResourceAsStream(path);
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

			String line;

			// skip first line with headers.
			reader.readLine();

			while ((line = reader.readLine()) != null) {

				String[] values = new String[8];
				values = line.split("\t");

				// Check if line has all fields filled (incl. source).
				if (values.length == 6) {
					int party = Integer.parseInt(values[0]);
					int question = Integer.parseInt(values[1]);
					int answer = Integer.parseInt(values[4]);
					float agreement = Float.parseFloat(values[5]);

					ExpectedResult result = new ExpectedResult(party, question, answer, agreement);
					resultList.add(result);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			try {
				inputStream.close();
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


}
