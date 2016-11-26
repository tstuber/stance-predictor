package info.stuber.fhnw.thesis.lucene;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class SearchTesterTest {

	SearchTester searcher = null;
	
	@Before
	public void setUp() {
		searcher = new SearchTester();
	}
	
	@Test
	public void GetPathOfIndex_AllowedWindowSizes()
	{
		String expected_ws0 = GetConfigPropertyValues.getProperty("path_index_ws0");
		String expected_ws1 = GetConfigPropertyValues.getProperty("path_index_ws1");
		String expected_ws2 = GetConfigPropertyValues.getProperty("path_index_ws2");
		String expected_ws3 = GetConfigPropertyValues.getProperty("path_index_ws3");
		String expected_ws4 = GetConfigPropertyValues.getProperty("path_index_ws4");
		String expected_ws5 = GetConfigPropertyValues.getProperty("path_index_ws5");
		String actual_ws0 = searcher.getPathOfIndex(0);
		String actual_ws1 = searcher.getPathOfIndex(1);
		String actual_ws2 = searcher.getPathOfIndex(2);
		String actual_ws3 = searcher.getPathOfIndex(3);
		String actual_ws4 = searcher.getPathOfIndex(4);
		String actual_ws5 = searcher.getPathOfIndex(5);

		Assert.assertNull(actual_ws0);
		Assert.assertNull(expected_ws0);
		Assert.assertTrue(expected_ws1.equals(actual_ws1));
		Assert.assertTrue(expected_ws2.equals(actual_ws2));
		Assert.assertTrue(expected_ws3.equals(actual_ws3));
		Assert.assertTrue(expected_ws4.equals(actual_ws4));
		Assert.assertNull(actual_ws5);
		Assert.assertNull(expected_ws5);
	}
}
