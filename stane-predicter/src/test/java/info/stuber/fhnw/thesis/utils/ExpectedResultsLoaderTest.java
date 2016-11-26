package info.stuber.fhnw.thesis.utils;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class ExpectedResultsLoaderTest {

	@Test
	public void GetSingleQuestionTest(){
		ExpectedResult res = ExpectedResultsLoader.getSingleResult(Party.CON, 1);
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.getAnswer() == 1.0f);
	}
	
	@Test
	public void GetExpectedAnswersByPartyTest(){
		List<ExpectedResult> res = ExpectedResultsLoader.getResultsByParty(Party.CON);
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() == 32);
	}
	
	@Test
	public void GetExpectedAnswersByQuestionTest(){
		List<ExpectedResult> res = ExpectedResultsLoader.getResultsByQuestion(1);
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() == 7);
	}
	
	@Test
	public void GetAllExpectedAnswersTest(){
		List<ExpectedResult> res = ExpectedResultsLoader.getAllResults();
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() == 224);
	}
}
