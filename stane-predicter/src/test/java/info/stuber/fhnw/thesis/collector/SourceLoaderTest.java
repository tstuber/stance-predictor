package info.stuber.fhnw.thesis.collector;

import org.junit.Assert;
import org.junit.Test;

public class SourceLoaderTest {
	
	@Test
	public void SourceLoader_Test() {
		ISourceLoader loader = new SourceLoader();
		
		Assert.assertNotNull(loader);
		System.out.println(loader.getCodingCount());
		Assert.assertTrue(loader.getCodingCount() > 400);
	}
}
