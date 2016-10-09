package info.stuber.fhnw.thesis.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.common.net.InternetDomainName;

public class CrawlerUtils {

	private static final Map<String, String> myMap;
	static {
		myMap = new HashMap<String, String>();
		myMap.put("www.theguardian.com", "content__main-column--article");
	}

	public static String getSelector(String url) {
		URI myUrl = null;
		
		try {
			myUrl = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String selector = myMap.get(myUrl.getHost());

		return selector;

	}

}
