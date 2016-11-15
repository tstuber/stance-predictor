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
}
