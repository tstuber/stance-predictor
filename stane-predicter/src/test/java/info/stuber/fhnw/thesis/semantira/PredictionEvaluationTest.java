package info.stuber.fhnw.thesis.semantira;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import info.stuber.fhnw.thesis.utils.Party;
import junit.framework.Assert;

public class PredictionEvaluationTest {

	PredictionEvaluation evaluator;
	private Party PARTY = Party.fromInteger(6);
	private int QUESTION_ID = 17;
	private int WINDOW_SIZE = 4;
	
	// TO large Sample to send: Party 3, Question 23, Window Size 4

	@Before
	public void setup() {
		evaluator = new PredictionEvaluation();
	}

	@Test
	public void Evaluate_All_Test() {

		List<EvalResult> results = evaluator.CompareAll(WINDOW_SIZE);
		printResult(results);
	}
	
	@Test
	public void Evaluate_ByPartyAndQuestion_Test() {

		List<EvalResult> results = evaluator.evaluateSingle(PARTY, QUESTION_ID, WINDOW_SIZE);
		printResult(results);
	}

	@Test
	public void Evaluate_ByParty_Test() {

		List<EvalResult> results = evaluator.compareParty(PARTY, WINDOW_SIZE);
		printResult(results);
	}
	
	@Test
	public void Evaluate_ByQuestion_Test() {

		List<EvalResult> results = evaluator.CompareQuestion(QUESTION_ID, WINDOW_SIZE);
		printResult(results);
	}

	private void printResult(List<EvalResult> results) {
		System.out.println("Party\tQuestion\tExp\tPred\tisSuccess");
		
		int success = 0;
		for (EvalResult res : results) {
			System.out.println(res.toString());
			if(res.isSuccess())
				success++;
		}
		System.out.println(success + " from " + results.size() + " were successful predicted");
		
	}
}
