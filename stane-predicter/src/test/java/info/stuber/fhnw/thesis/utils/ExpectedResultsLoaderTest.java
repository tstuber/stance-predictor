package info.stuber.fhnw.thesis.utils;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class ExpectedResultsLoaderTest {

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
		Assert.assertTrue(res.size() == 29);
	}
	
	@Test
	public void GetExpectedAnswersByQuestionTest(){
		List<ExpectedResult> res = ExpectedResultsLoader.getResultsByQuestion(1);
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() == 5);
	}
	
	@Test
	public void GetAllExpectedAnswersTest(){
		List<ExpectedResult> res = ExpectedResultsLoader.getAllResults();
		
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() == 145);
	}
}
