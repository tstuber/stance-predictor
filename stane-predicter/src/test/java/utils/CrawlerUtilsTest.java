package utils;

import org.junit.Test;

import info.stuber.fhnw.thesis.utils.CrawlerUtils;

public class CrawlerUtilsTest {

	
	@Test
	public void GetSelectorTest()
	{
		String url = "https://www.theguardian.com/world/2016/oct/05/hong-kong-activist-joshua-wong-detained-thailand-china-deportation";
		String res = CrawlerUtils.getSelector(url);
		
		System.out.println(res); 
	}
	
	@Test
	public void GetSelectorWhenNotInListTest()
	{
		String url = "https://www.theguardian.de/world/2016/oct/05/hong-kong-activist-joshua-wong-detained-thailand-china-deportation";
		String res = CrawlerUtils.getSelector(url);
		
		System.out.println(res); 
	}
}
