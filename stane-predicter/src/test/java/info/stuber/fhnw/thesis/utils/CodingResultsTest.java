package info.stuber.fhnw.thesis.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CodingResultsTest {

	@Test
	public void ReadFileTest() {
		
		List<ExpectedResult> res = ExpectedResultsLoader.getAllResults();
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() == 224);
	}
	
	@Test
	public void ReadFirstResultTest() {
		List<ExpectedResult> res = ExpectedResultsLoader.getAllResults();
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.get(0).getParty() == 1);
		Assert.assertTrue(res.get(0).getQuestion() == 1);
		Assert.assertTrue(res.get(0).getAnswer() == 1.0f);
		Assert.assertTrue(res.get(0).getAgreement() == 1.0f);
	}
	
	@Test
	public void ReadSingleExpectedTest() {
		
		ExpectedResult res = ExpectedResultsLoader.getSingleResult(Party.CON, 1);
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.getParty() == 1);
		Assert.assertTrue(res.getQuestion() == 1);
		Assert.assertTrue(res.getAnswer() == 1.0f);
		Assert.assertTrue(res.getAgreement() == 1.0f);
		Assert.assertTrue(res.getSentiment().equals("agreement"));
	}
}
