package info.stuber.fhnw.thesis.semantira;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import info.stuber.fhnw.thesis.utils.Props;
import info.stuber.fhnw.thesis.utils.Question;

public class ReportHandler {

	String evaluationName;

	private int WINDOW_SIZE = Props.evalWindowSize();
	private boolean ONLY_SENTIMENTAL = Props.evalOnlySentiment();
	private boolean ONLY_FIRST_HIT = Props.evalFirstHit();
	private boolean INVERSE_QUESTION_MODE = Props.evalInverseQuestion();
	private String ANALYZER = Props.evalAnalyzer();

	public ReportHandler() {

		// Create evaluation-Shortcut.
		this.evaluationName = "WS" + WINDOW_SIZE + "_" + firstLetter(ONLY_SENTIMENTAL) + "_"
				+ firstLetter(ONLY_FIRST_HIT) + "_" + firstLetter(INVERSE_QUESTION_MODE) + "_" + ANALYZER;
	}

	private String firstLetter(boolean input) {
		return (input == true) ? "T" : "F";
	}

	public void saveReport(List<PredictedResult> results) {

		StringBuilder sb = new StringBuilder();

		// Create target output file.
		String date = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		String date_report = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		File file = new File("./reports/Report_" + this.evaluationName + "_" + date + ".html");

		// Calculate result.
		int total = results.size();
		int correct = 0;
		for (PredictedResult res : results) {
			if (res.isSuccess(res.getAnswer(res.getMin())))
				correct++;
		}
		int wrong = total - correct;

		float correctPerc = correct * 1.0f / total * 100;
		float wrongPerc = wrong * 1.0f / total * 100;

		System.out.println("correct: " + correct + " (percentage: " + correctPerc + ")");
		System.out.println("wrong: " + wrong + " (percentage: " + wrongPerc + ")");
		System.out.println("total: " + total);

		// HEADER.
		sb.append("<!DOCTYPE html><html><head>");
		sb.append("<title>Stance Prediction Report</title>");
		sb.append("<style>.hiddenRow {padding: 0!important;}</style>");
		sb.append("<style>.detail { padding: 10px;}</style>");
		sb.append(
				"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>");
		sb.append(
				"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'>");
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
		sb.append("<p>" + correctPerc + "% prediction accuracy (" + correct + " out of " + total
				+ " correctly predicted)");

		sb.append("<div class='progress'>");
		sb.append("<div class='progress-bar progress-bar-success progress-bar-striped' style='width: " + correctPerc
				+ "%'>");
		sb.append(" <span class='sr-only'>" + correctPerc + "% Complete (success)</span>");
		sb.append("</div>");
		sb.append("<div class='progress-bar progress-bar-danger progress-bar-striped' style='width: " + wrongPerc
				+ "%'>");
		sb.append(" <span class='sr-only'>" + wrongPerc + "% Complete (danger)</span>");
		sb.append("</div>");
		sb.append("</div>");

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

			// TODO: use best metric for the evaluation.
			int result = res.getAnswer(res.getMin());

			sb.append("<tr>");
			sb.append("<td>" + count + "</td>");
			sb.append("<td>" + res.getParty() + "</td>");
			sb.append("<td>" + res.getQuestionId() + "</td>");
			sb.append("<td>" + res.getExpectedAnswer() + "</td>");
			if (res.isSuccess(result))
				sb.append("<td class='success'>" + result + "</td>");
			else
				sb.append("<td class='danger'>" + result + "</td>");
			sb.append("<td><a class='accordion-toggle' data-toggle='collapse' href='#" + target + "'>Details ");
			sb.append("<i class='glyphicon glyphicon-plus'></i></a><td>");

			// DETAILS
			sb.append("</tr>");
			sb.append("<tr>");
			sb.append("<td colspan='6' class='hiddenRow'>");
			sb.append("<div class='accordian-body collapse detail' id='" + target + "'>");
			sb.append("<h4>Details</h4>");
			int itemPos = 0;
			for (PredictedResultItem resItem : res.getResultItems()) {

				// print query just once.
				if (itemPos == 0) {
					sb.append("<p><b>Question: </b>" + Question.getQuestionById(res.getQuestionId()) + "</p>");
					sb.append("<p><b>Query: </b><samp>" + resItem.getQuery() + "</samp></p>");
				}

				if (Props.evalFirstHit() && itemPos != 0)
					break;

				sb.append("<div class='panel-group'>");
				sb.append("<div class='panel panel-default'>");
				sb.append("<div class='panel-heading'>");
				sb.append("<b>Hitscore: </b>" + resItem.getHitScore());
				sb.append(" <b>SentiScore: </b> " + resItem.getSentimentScore());
				sb.append(" <b>SentiPolarity: </b> " + resItem.getSentimentPolarity());
				sb.append("</div>");
				sb.append("<div class='panel-body'>");
				sb.append("<p>" + highlightMatches(resItem.getText(), resItem.getQuery()) + "</p>");
				sb.append("<p class='small'>Source: <a href='" + resItem.getSource() + "'/>" + resItem.getSource()
						+ "</a></p>");
				sb.append("</div>");

				sb.append("</div>");
				sb.append("</div>");

				itemPos++;
			}

			sb.append("</div>");
			sb.append("</td>");
			sb.append("</tr>");

			count++;
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");

		// JavaScript
		sb.append("<script src='https://code.jquery.com/jquery-3.1.1.min.js'></script>");
		sb.append(
				"<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>");
		sb.append("<script>");
		sb.append("var selectIds = $('.accordian-body');");
		sb.append("$(function($) {");
		sb.append("selectIds.on('show.bs.collapse hidden.bs.collapse', function() {");
		sb.append("$(this).parent().parent().prev().find('.glyphicon').toggleClass('glyphicon-minus glyphicon-plus');");
		sb.append("})");
		sb.append("});");
		sb.append("</script>");

		sb.append("</body></html>");

		try {
			Files.write(Paths.get(file.toURI()), sb.toString().getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String highlightMatches(String text, String query) {
		// Cleanup query.
		String q = null;
		q = query.replace("(", "").replace(")","");
		q = q.substring(0, q.indexOf("+"));
		String[] searchWords = q.split("contents\\:");

		for (int i = 0; i < searchWords.length; i++) {

			// ignore empty values.
			if (searchWords[i].isEmpty())
				continue;

			String searchWord = searchWords[i].trim();
			String markedSearchWord = "<mark>" + searchWord + "</mark>";

			text = text.replaceAll(searchWord, markedSearchWord);
			text = text.replaceAll(firstLetterCapital(searchWord), firstLetterCapital(markedSearchWord));
		}

		return text;
	}
	
	private String firstLetterCapital(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}

	@Test
	public void testMatch() {
		String query = "contents:government contents:spending contents:should contents:cut contents:further contents:order contents:balance contents:budget +party:1";
		String text = "99We have cut the EU budget, for the first time in history, and pushed for better value for money for British taxpayers in all spending.";

		System.out.println(text);
	}
}
