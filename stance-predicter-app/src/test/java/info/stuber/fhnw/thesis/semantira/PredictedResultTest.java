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
	public void predictedResult_WeightedMeanTest() {
		
		// Arrange.
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 16, 0.5f, "Positive", "text"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 12, 0.0f, "Neutral", "text"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 2, -0.5f, "Negative", "text"));
		
		// 1. (16)  0.2
		// 2. (12)  0.0
		// 3. ( 3) -0.2
		// expected: close to 0.2

		// Calculatoin: (16) * 0.2 + (12) * 0.0 + ( 3) * -0.2 / (16+12+3)
		
		// Assert.
		Assert.assertTrue(result.size() == 3);
		Assert.assertEquals(0.23333333, result.getWeightedMean(), 0.01);
	}
	
	@Test
	public void predictedResult_WeightedMeanInvalidTest() {
		
		// Arrange.
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 16, 0.5f, "Positive", "text"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.0f, "Neutral"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 2, -0.5f, "Negative", "text"));
		
		// Assert.
		Assert.assertTrue(result.size() == 3);
		Assert.assertEquals(9999, result.getWeightedMean(), 0.01);
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
	
	@Test
	public void predictedResult_MinMaxTest() {
		
		// Arrange.
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 1.0f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.8f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, -1.0f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, -0.8f, "Positive"));
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, 0.7f, "Positive"));
		
		// Assert.
		Assert.assertTrue(result.size() > 1);
		Assert.assertTrue(result.getMax() == 1.0f);
		Assert.assertTrue(result.getMin() == -1.0f);
	}
	
	@Test
	public void predictResult_AnswerTest1() {
		float sentiScore = 1.0f;
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, sentiScore, "Positive"));
		
		Assert.assertTrue(result.getAnswer(sentiScore) == 1);
	}
	
	@Test
	public void predictResult_AnswerTest2() {
		float sentiScore = 0.12f;
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, sentiScore, "Positive"));
		
		Assert.assertTrue(result.getAnswer(sentiScore) == 2);
	}
	
	@Test
	public void predictResult_AnswerTest3() {
		float sentiScore = 0.11f;
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, sentiScore, "Neutral"));
				
		Assert.assertTrue(result.getAnswer(sentiScore) == 3);
	}
	
	@Test
	public void predictResult_AnswerTest4() {
		float sentiScore = -0.24f;
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, sentiScore, "Negativ"));
		
		Assert.assertTrue(result.getAnswer(sentiScore) == 4);
	}
	
	@Test
	public void predictResult_AnswerTest5() {
		float sentiScore = -0.78f;
		PredictedResult result = new PredictedResult(PARTY, QUESTION_ID);
		result.addItem(new PredictedResultItem(PARTY.getId(), QUESTION_ID, sentiScore, "Negativ"));
		
		Assert.assertTrue(result.getAnswer(sentiScore) == 5);
	}
}
