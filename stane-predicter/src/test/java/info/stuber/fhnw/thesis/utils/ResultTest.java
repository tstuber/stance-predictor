package info.stuber.fhnw.thesis.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResultTest {
	
	private ExpectedResult res;
	private static final String AGREEMENT = "agreement";
	private static final String DISAGREEMENT = "disagreement";
	private static final String NEUTRAL = "neutral";
	
	@Test
	public void AgreementResultTest() {
		res = new ExpectedResult(1,1,1,1);
		
		Assert.assertTrue(res.getSentiment() == AGREEMENT);
	}
	
	@Test
	public void DisAgreementResultTest() {
		res = new ExpectedResult(1,1,4,1);
		
		Assert.assertTrue(res.getSentiment() == DISAGREEMENT);
	}
	
	@Test
	public void NeutralResultTest() {
		res = new ExpectedResult(1,1,3,1);
		
		Assert.assertTrue(res.getSentiment() == NEUTRAL);
	}
	
	@Test
	public void NoOpinionResultTest() {
		res = new ExpectedResult(1,1,6,1);
		
		Assert.assertTrue(res.getSentiment() == NEUTRAL);
	}

}
