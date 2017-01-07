package info.stuber.fhnw.thesis.semantira;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.Props;

public class PredictionRunner {

	PredictionEvaluation evaluator;
	ReportHandler reportHandler;

	private int WINDOW_SIZE = Props.evalWindowSize();
	private Party PARTY = Party.fromInteger(1);
	private int QUESTION_ID = 14;

	public PredictionRunner() {
		this.evaluator = new PredictionEvaluation();
		this.reportHandler = new ReportHandler();
	}

	public static void main(String[] args) {
		PredictionRunner runner = new PredictionRunner();
		runner.evaluate();
		
		// Only for report debugging.
		// runner.evaluateFromFile();
	}

	// Method is more for testing.
	public void evaluateFromFile() {
		String fileName = "Report_WS3_T_F_F_S_20170107-121851.ser";
		File file = new File("./reports/" + fileName);

		List<PredictedResult> results = reportHandler.deserializeResults(file);
		this.reportHandler.createReport(results);
	}

	public void evaluate() {

		// If set to true, reuses the last saved result.
		// No Usage of Web Sentiment Analyzer will be made.
		// List<PredictedResult> results = this.evaluator.evaluateSingle(PARTY, QUESTION_ID, WINDOW_SIZE);
		List<PredictedResult> results = this.evaluator.evaluateAll(WINDOW_SIZE);

		this.reportHandler.createReport(results);
	}
}
