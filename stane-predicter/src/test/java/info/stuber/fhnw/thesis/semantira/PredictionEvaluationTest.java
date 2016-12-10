package info.stuber.fhnw.thesis.semantira;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import info.stuber.fhnw.thesis.utils.Party;
import junit.framework.Assert;

public class PredictionEvaluationTest {

	PredictionEvaluation evaluator;
	private Party PARTY = Party.fromInteger(4);
	private int QUESTION_ID = 34;
	private int WINDOW_SIZE = 2;
	
	// To large Sample to send: Party 3, Question 23, Window Size 4

	@Before
	public void setup() {
		evaluator = new PredictionEvaluation();
	}

	@Test
	public void Evaluate_ByPartyAndQuestion_Test() {

		List<PredictedResult> results = evaluator.evaluateSingle(PARTY, QUESTION_ID, WINDOW_SIZE);
		printResult(results);
	}

	@Test
	public void Evaluate_ByQuestion_Test() {

		List<PredictedResult> results = evaluator.compareQuestion(QUESTION_ID, WINDOW_SIZE);
		printResult(results);
	}
	
	@Test
	public void Evaluate_ByParty_Test() {

		List<PredictedResult> results = evaluator.compareParty(PARTY, WINDOW_SIZE);
		printResult(results);
	}
	
	@Test
	public void Evaluate_All_Test() {

		List<PredictedResult> results = evaluator.compareAll(WINDOW_SIZE);
		printResult(results);
	}
	
	private void printResult(List<PredictedResult> results) {
		System.out.println("Type\tParty\tQ-Id\tExp\tMin\t\tMax\t\tAvg\t\tW-Avg\t\tMedium");
		
		for (PredictedResult res : results) {
			System.out.println(res.toString());
		}
	}
}
