package info.stuber.fhnw.thesis.collector;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class PdfCacheTest {


	@Test
	public void PdfCache_TestNotCachedDok_Test()
	{
		// Arrange.
		String url = "https://www.etstestrstseafsf.com";
		
		// Act.
		String result = PdfCache.getPdfCacheFilepath(url);
		
		// Assert.
		Assert.assertNull(result);
	}
	
	@Test
	public void PdfCache_TestDocuInList_Test()
	{
		// Arrange.
		String url = "http://www.stuber.info";
		
		// Act.
		String result = PdfCache.getPdfCacheFilepath(url);
		
		// Assert.
		Assert.assertTrue(result.equals("test.txt"));
	}
	
	@Test
	public void PdfCache_LoadPdfFromFile_Test() throws IOException
	{
		// Arrange.
		String url = "http://www.stuber.info";
		
		// Act.
		String result = PdfCache.readFile(url);
		System.out.println(result);
		
		// Assert.
		Assert.assertTrue(result.equals("Das ist ein Testfile."));
	}
}
