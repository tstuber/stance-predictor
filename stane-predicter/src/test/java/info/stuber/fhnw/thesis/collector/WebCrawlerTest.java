package info.stuber.fhnw.thesis.collector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class WebCrawlerTest {
	
	@Test
	public void When_UrlValid_Expect_normalLoad()
	{
		String url = "https://www.theguardian.com/world/2016/oct/05/hong-kong-activist-joshua-wong-detained-thailand-china-deportation";
		Set<String> urls = new HashSet<String>();
		urls.add(url);
		
		WebCrawler crawler = new WebCrawler(urls);
		crawler.crawlSites();
		
	}

}
