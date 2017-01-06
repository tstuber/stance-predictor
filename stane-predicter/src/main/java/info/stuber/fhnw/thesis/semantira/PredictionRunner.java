package info.stuber.fhnw.thesis.semantira;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	}

	public void evaluate() {
		// List<PredictedResult> results =
		// this.evaluator.compareAll(WINDOW_SIZE);
		// List<PredictedResult> results =
		// this.evaluator.evaluateSingle(Party.CON, 1, 3);

		// List<PredictedResult> results = this.evaluator.compareQuestion(1,
		// WINDOW_SIZE);
		// List<PredictedResult> results = this.evaluator.compareParty(PARTY,
		// WINDOW_SIZE);
		// dirtySave(results);

		List<PredictedResult> results = dirtyLoad();

		this.reportHandler.saveReport(results);
	}

	private void dirtySave(List<PredictedResult> results) {

		try {
			FileOutputStream fout = new FileOutputStream("lastRunToReport.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(results);
			oos.close();
			System.out.println("File stored: lastRunToReport.dat");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private List<PredictedResult> dirtyLoad() {
		List<PredictedResult> results = null;
		try {
			FileInputStream fius = new FileInputStream("lastRunToReport.dat");
			ObjectInputStream ois = new ObjectInputStream(fius);
			results = (List<PredictedResult>) ois.readObject();

			ois.close();
			System.out.println("File loaded: lastRunToReport.dat");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return results;
	}
}
