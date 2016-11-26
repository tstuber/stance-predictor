package info.stuber.fhnw.thesis.semantira;

import org.junit.Test;

import info.stuber.fhnw.thesis.utils.Party;
import junit.framework.Assert;

public class PredictedResultTest {

	private Party PARTY = Party.CON;
	private int QUESTION_ID = 1;
	
	@Test
	public void predictedResultTest() {
		
		// Arrange.
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 1.0f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.8f, "Positive"));
		
		// Assert.
		Assert.assertNotNull(result);
		Assert.assertTrue(result.size() > 1);
	}
	
	@Test
	public void predictedResult_MeanTest() {
		
		// Arrange.
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 1.0f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.8f, "Positive"));
		
		// Assert.
		Assert.assertTrue(result.size() > 1);
		Assert.assertTrue(result.getMean() == 0.9f);
	}
	
	@Test
	public void predictedResult_MedianTest() {
		
		// Arrange.
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 1.0f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.8f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 1.0f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.8f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.7f, "Positive"));
		
		// Assert.
		Assert.assertTrue(result.size() > 1);
		Assert.assertTrue(result.getMedian() == 0.8f);
	}
	
	public void predictResult_AnswerTest1() {
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 1.0f, "Positive"));
		
		Assert.assertTrue(result.getAnswer() == 1);
	}
	
	public void predictResult_AnswerTest2() {
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.74f, "Positive"));
		
		Assert.assertTrue(result.getAnswer() == 2);
	}
	
	public void predictResult_AnswerTest3() {
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.25f, "Neutral"));
		
		Assert.assertTrue(result.getAnswer() == 3);
	}
	
	public void predictResult_AnswerTest4() {
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, -0.26f, "Negativ"));
		
		Assert.assertTrue(result.getAnswer() == 4);
	}
	
	public void predictResult_AnswerTest5() {
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, -0.75f, "Negativ"));
		
		Assert.assertTrue(result.getAnswer() == 5);
	}
}
