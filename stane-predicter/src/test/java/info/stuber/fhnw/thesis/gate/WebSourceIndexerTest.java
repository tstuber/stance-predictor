package info.stuber.fhnw.thesis.gate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gate.util.GateException;
import info.stuber.fhnw.thesis.collector.Coding;

public class WebSourceIndexerTest {

	private WebSourceIndexer indexer;
	
	@Before
	public void Setup() throws MalformedURLException, GateException{
		indexer = new WebSourceIndexer();
	}
	
	@Test
	public void CodingWithNoContent_Test() throws InvocationTargetException, GateException, InterruptedException, IOException {
		Coding coding = new Coding(1,1,new URL("http://www.google.ch"));
		
		List<String> result = indexer.splitSentence(coding);
		
		Assert.assertNull(result);
	}
	
	@Test
	public void CodingWithContent_Test()throws InvocationTargetException, GateException, InterruptedException, IOException {
		Coding coding = new Coding(1,1,new URL("http://www.fhnw.ch"));
		coding.setContent("Document splitter test. Second sentence. Last one. And nother one. And one more.");
		
		List<String> result = indexer.splitSentence(coding);
		
		Assert.assertNotNull(result);
		// Depending on windows size and max Sentence lenght the result size will vary. 
		Assert.assertTrue(result.size() > 1);
	}
}
