package info.stuber.fhnw.thesis.semantira;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.Props;

public class PredictionRunner {

	PredictionEvaluation evaluator;
	String evaluationName;
	private Party PARTY = Party.fromInteger(1);
	private int QUESTION_ID = 14;

	private int WINDOW_SIZE = Props.evalWindowSize();
	private boolean ONLY_SENTIMENTAL = Props.evalOnlySentiment();
	private boolean ONLY_FIRST_HIT = Props.evalFirstHit();
	private boolean INVERSE_QUESTION_MODE = Props.evalInverseQuestion();
	private String ANALYZER = Props.evalAnalyzer();

	public PredictionRunner() {
		this.evaluator = new PredictionEvaluation();

		// Create evaluation-Shortcut.
		this.evaluationName = "WS" + WINDOW_SIZE + "_" + firstLetter(ONLY_SENTIMENTAL) + "_"
				+ firstLetter(ONLY_FIRST_HIT) + "_" + firstLetter(INVERSE_QUESTION_MODE) + "_" + ANALYZER;
	}

	public static void main(String[] args) {
		PredictionRunner runner = new PredictionRunner();
		runner.evaluate();
	}

	public void evaluate() {
		// List<PredictedResult> results =
		// this.evaluator.compareAll(WINDOW_SIZE);
		// List<PredictedResult> results = this.evaluator.compareQuestion(1, WINDOW_SIZE);
		List<PredictedResult> results = this.evaluator.evaluateSingle(Party.CON, 1, 3);
		saveReport(results);
	}

	private String firstLetter(boolean input) {
		return (input == true) ? "T" : "F";
	}

	private void saveReport(List<PredictedResult> results) {

		StringBuilder sb = new StringBuilder();

		// Create target output file.
		String date = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		String date_report = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		File file = new File("./reports/Report_" + this.evaluationName + "_" + date + ".html");

		// HEADER.
		sb.append("<!DOCTYPE html><html><head>");
		sb.append("<title>Stance Prediction Report</title>");
		sb.append("<script src='https://code.jquery.com/jquery-3.1.1.min.js'></script>");
		sb.append(
				"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>");
		sb.append(
				"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'>");
		sb.append(
				"<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>");
		sb.append("<script src='https://code.jquery.com/jquery-3.1.1.min.js'></script>");
		sb.append("<style>.hiddenRow {padding: 0!important;}</style>");
		sb.append("</head>");

		// SETTINGS.
		sb.append("<body><div class='container'>");
		sb.append("<h1>Evaluation Report</h1><p><b>Date:</b> " + date_report + "</p>");
		sb.append("<h3>Settings</h3><p><b>Setting Description</b></p>");
		sb.append("<ul>");
		sb.append("<li>Index WindowSize: <kbd>" + WINDOW_SIZE + "</kbd></li>");
		sb.append("<li>OnlySentiment Sentences: <kbd>" + ONLY_SENTIMENTAL + "</kbd></li>");
		sb.append("<li>FirstHit: <kbd>" + ONLY_FIRST_HIT + "</kbd></li>");
		sb.append("<li>InversionFlag: <kbd>" + INVERSE_QUESTION_MODE + "</kbd></li>");
		sb.append("<li>Analyzer: <kbd>" + ANALYZER + "</kbd></li>");
		sb.append("</ul>");
		sb.append("<p>Shortcut: " + this.evaluationName + "</p>");
		
		// SUMMARY.
		sb.append("<h3>Summary</h3>");
		
		int right = 55;
		int wrong = 45;
		
		sb.append("<div class='progress'>");
		sb.append("<div class='progress-bar progress-bar-success progress-bar-striped' style='width: "+right+"%'/>");
		sb.append(" <span class='sr-only'>"+right+"% Complete (success)</span>");
		sb.append("</div>");
		sb.append("<div class='progress-bar progress-bar-danger progress-bar-striped' style='width: "+wrong+"%'/>");
		sb.append(" <span class='sr-only'>"+wrong+"% Complete (danger)</span>");
		sb.append("</div>");
		sb.append("</div>");
		
//		<div class="progress">
//		  <div class="progress-bar progress-bar-success" style="width: 35%">
//		    <span class="sr-only">35% Complete (success)</span>
//		  </div>
//		  <div class="progress-bar progress-bar-warning progress-bar-striped" style="width: 20%">
//		    <span class="sr-only">20% Complete (warning)</span>
//		  </div>
//		  <div class="progress-bar progress-bar-danger" style="width: 10%">
//		    <span class="sr-only">10% Complete (danger)</span>
//		  </div>
//		</div>
		
		
		// RESULTS. 
		sb.append("<h3>Results</h3>");

		sb.append("<table class='table' style='border-collapse:collapse;'>");

		sb.append("<thead><tr>");
		sb.append("<th>#</th>");
		sb.append("<th>Party</th>");
		sb.append("<th>Question</th>");
		sb.append("<th>Expected</th>");
		sb.append("<th>Predicted</th>");
		sb.append("<th>More Infos</th>");
		sb.append("</tr></thead>");

		sb.append("<tbody>");

		int count = 1;
		for (PredictedResult res : results) {
			String target = "collapse" + count;
			int result = res.getAnswer(res.getMin());

			sb.append("<tr>");
			sb.append("<td>" + count + "</td>");
			sb.append("<td>" + res.getParty() + "</td>");
			sb.append("<td>" + res.getQuestionId() + "</td>");
			sb.append("<td>" + res.getExpectedAnswer() + "</td>");
			if (res.isSuccess(result))
				sb.append("<td class='success'>" + result + "</td>"); // TODO:
																		// VERWENDEN
																		// DER
																		// BESTEN
																		// PERFORMANCE!
			else
				sb.append("<td class='danger'>" + result + "</td>"); // TODO:
																		// VERWENDEN
																		// DER
																		// BESTEN
																		// PERFORMANCE!
			sb.append("<td><button type=button class='btn btn-default btn-xs'  data-toggle='collapse' data-target='#"+target+"' class='accordion-toggle'>");
			sb.append("<span class='glyphicon glyphicon-plus-sign' aria-hidden=true></span>");
			sb.append(" More</button></td>");		
			
			sb.append("</tr>");
			sb.append("<tr>");
			sb.append("<td colspan='5' class='hiddenRow'>");
			sb.append("<div class='accordian-body collapse' id='"+target+"'>");
			sb.append("<h3>Debug Information</h3>");
			sb.append("<p>Used answers for Prediction:</p>");
			
			sb.append("</div>");
			sb.append("</td>");
			sb.append("</tr>");

			count++;
		}

		sb.append("</tbody>");
		sb.append("</table></div></body></html>");

		// for each result: print out.

		try {
			Files.write(Paths.get(file.toURI()), sb.toString().getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
