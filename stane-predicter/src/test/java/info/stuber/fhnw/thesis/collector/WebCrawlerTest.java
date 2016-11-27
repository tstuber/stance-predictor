package info.stuber.fhnw.thesis.collector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WebCrawlerTest {

	
	@Test
	public void WebCrawler_GetSingleURLTest() throws MalformedURLException {
		// Arrange.
		URL url = new URL("http://www.bbc.com/news/uk-politics-29642613");
		List<Coding> codings = new ArrayList<Coding>();
		codings.add(new Coding(1,1,url));
		ISourceLoader sourceLoader = new SourceLoader(codings);
		WebCrawler crawler = new WebCrawler(sourceLoader);
		
		crawler.scrapSites();
		crawler.makeReport();
	}
}
