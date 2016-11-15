package info.stuber.fhnw.thesis.semantira;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import info.stuber.fhnw.thesis.utils.Party;
import junit.framework.Assert;

public class PredictionEvaluationTest {
	
	PredictionEvaluation evaluator;
	private Party PARTY = Party.CON;
	private int QUESTION_ID = 1;
	
	@Before
	public void setup() {
		evaluator = new PredictionEvaluation();
	}

	@Test
	public void PredictionEvaluatorTest() {
		evaluator.evaluateSingle(PARTY, QUESTION_ID);
	}
}
