package info.stuber.fhnw.thesis.semantira;

import java.util.List;

import info.stuber.fhnw.thesis.utils.ExpectedResultsLoader;
import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.ExpectedResult;

public class PredictionEvaluation {

	private DocumentSentimentAnalyzer analyzer;

	public PredictionEvaluation() {
		analyzer = new DocumentSentimentAnalyzer();
	}

	public PredictedResult TestSingle(Party party, int questionId) {
		return analyzer.calculateSentiment(party, questionId);
	}

	public boolean evaluateSingle(Party party, int questionId) throws NotSupportedQuestionException {
		boolean result = false;

		PredictedResult predRes = analyzer.calculateSentiment(party, questionId);
		ExpectedResult expRes = ExpectedResultsLoader.getResult(party, questionId);

		if (expRes == null) {
			throw new NotSupportedQuestionException("Not supported Question (" + questionId + ")");
		}

		float exp = expRes.getMedian();
		float pre = predRes.getMean();

		if (exp >= 4 && pre < -0.25)
			result = true;
		else if (exp <= 2 && pre > 0.25)
			result = true;
		else if (exp == 3 && (pre >= 0.25 || pre <= 0.25))
			result = true;
		else
			result = false;

		System.out.println("Party: \t" + party);
		System.out.println("Question: \t" + questionId);
		System.out.println("Expected Answer (Median): " + expRes.getMedian() + " (" + expRes.getMedianAsText() + ")");
		System.out.println("Predicted Answer (Mean):  " + predRes.getMean());
		System.out.println("Predicted Answer (Median):  " + predRes.getMedian());
		System.out.println("Summary: " + result);

		return result;
	}

	public void CompareQuestion() {
	}

	public void CompareParty() {
	}

	public void CompareAll() {
	}

}
