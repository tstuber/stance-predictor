package info.stuber.fhnw.thesis.collector;

import java.io.IOException;

import org.junit.Test;

import info.stuber.fhnw.thesis.collector.HtmlToPlainText;

public class HtmlToPlainTextTest {

	private final static String searchDate = "xxxxx";
	
	// www.bbc.com --> "story-body"
	// www.bbc.co.uk --> "column--primary"
	
	//@Test
	public void test() throws IOException
	{
		String url ="http://www.bbc.com/news/world-us-canada-37447016";
		String selector = "story-body";
		HtmlToPlainText.main(url, selector);
	}
	
	//@Test
	public void test_webarchive() throws IOException 
	{
		String url = "http://www.bbc.co.uk/news/uk-politics-29642613";
		
		String selector = "column--primary";
		HtmlToPlainText.main(url, selector);
	}
}
