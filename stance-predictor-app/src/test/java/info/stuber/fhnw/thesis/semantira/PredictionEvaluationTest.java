package info.stuber.fhnw.thesis.semantira;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.Props;
import junit.framework.Assert;

public class PredictionEvaluationTest {

	PredictionEvaluation evaluator;
	ReportHandler reportHandler;
	String evaluationName;
	private Party PARTY = Party.fromInteger(1);
	private int QUESTION_ID = 14;

	private int WINDOW_SIZE = Props.evalWindowSize();
	private boolean ONLY_SENTIMENTAL = Props.evalOnlySentiment();
	private boolean ONLY_FIRST_HIT = Props.evalFirstHit();
	private boolean INVERSE_QUESTION_MODE = Props.evalInverseQuestion();
	private String ANALYZER = Props.evalAnalyzer();

	// To large Sample to send: Party 3, Question 23, Window Size 4

	@Before
	public void setup() {
		this.evaluator = new PredictionEvaluation();
		this.reportHandler = new ReportHandler();

		// Create evaluation-Shortcut.
		this.evaluationName = "WS" + WINDOW_SIZE + "_" + firstLetter(ONLY_SENTIMENTAL) + "_" + firstLetter(ONLY_FIRST_HIT)
				+ "_" + firstLetter(INVERSE_QUESTION_MODE) + "_" + ANALYZER;
	}

	@Test
	public void Evaluate_ByPartyAndQuestion_Test() {

		List<PredictedResult> results = evaluator.evaluateSingle(PARTY, QUESTION_ID, WINDOW_SIZE);
		printResult(results);
		this.reportHandler.createReport(results);
	}

	@Test
	public void Evaluate_ByQuestion_Test() {

		List<PredictedResult> results = evaluator.evaluateQuestion(QUESTION_ID, WINDOW_SIZE);
		printResult(results);
		this.reportHandler.createReport(results);
	}

	@Test
	public void Evaluate_ByParty_Test() {

		List<PredictedResult> results = evaluator.evaluateParty(PARTY, WINDOW_SIZE);
		printResult(results);
		this.reportHandler.createReport(results);
	}

	@Test
	public void Evaluate_All_Test() {

		List<PredictedResult> results = evaluator.evaluateAll(WINDOW_SIZE);
		printResult(results);
		this.reportHandler.createReport(results);
	}

	private void printResult(List<PredictedResult> results) {
		System.out.println("Type\tParty\tQ-Id\tExp\tMin\t\tMax\t\tAvg\t\tW-Avg\t\tMedium\t\tInvFactor");

		for (PredictedResult res : results) {
			System.out.println(res.toString());
		}

		StringBuffer sb = new StringBuffer();
		sb.append("\nSettings");
		sb.append("\nWINDOW_SIZE: " + WINDOW_SIZE);
		sb.append("\nONLY_SENTIMENTAL: " + ONLY_SENTIMENTAL);
		sb.append("\nONLY_FIRST_HIT:" + ONLY_FIRST_HIT);
		sb.append("\nINVERSE_QUESTION_MODE: " + INVERSE_QUESTION_MODE);
		sb.append("\nANALYZER: " + ANALYZER);

		// Create evaluation-Shortcut.
		sb.append("\n\n" + this.evaluationName);
		System.out.println(sb.toString());
	}

	private String firstLetter(boolean input) {
		return (input == true) ? "T" : "F";
	}
}
