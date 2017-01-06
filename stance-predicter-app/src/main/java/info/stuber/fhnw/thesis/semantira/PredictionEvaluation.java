package info.stuber.fhnw.thesis.semantira;

import java.util.ArrayList;
import java.util.List;

import info.stuber.fhnw.thesis.utils.ExpectedResultsLoader;
import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.ExpectedResult;

public class PredictionEvaluation {

	private DocumentSentimentAnalyzer analyzer;

	public PredictionEvaluation() {
		analyzer = new DocumentSentimentAnalyzer();
	}


	/***
	 * Evaluates a specific question for a specific party.
	 * 
	 * @param party
	 * @param questionId
	 * @param windowSize
	 * @return
	 * @throws NotSupportedQuestionException
	 */
	public List<PredictedResult> evaluateSingle(Party party, int questionId, int windowSize) {

		List<PredictedResult> evaluationResults = new ArrayList<PredictedResult>();

		PredictedResult predRes = analyzer.returningHitScoreToCalculateToWeightScore(party, questionId, windowSize);

		evaluationResults.add(predRes);

		return evaluationResults;
	}

	/**
	 * Evaluates a specific question along all parties.
	 * 
	 * @param question
	 * @param windowSize
	 * @return
	 */
	public List<PredictedResult> evaluateQuestion(int question, int windowSize) {

		List<ExpectedResult> expectedResults = ExpectedResultsLoader.getResultsByQuestion(question);
		List<PredictedResult> evaluationResults = new ArrayList<PredictedResult>();

		for (ExpectedResult expRes : expectedResults) {
			Party party = Party.fromInteger(expRes.getParty());
			PredictedResult predRes = analyzer.returningHitScoreToCalculateToWeightScore(party, question, windowSize);
			evaluationResults.add(predRes);
		}

		return evaluationResults;
	}

	/**
	 * Evaluation all question of a specific party.
	 * 
	 * @param party
	 * @param windowSize
	 * @return
	 */
	public List<PredictedResult> evaluateParty(Party party, int windowSize) {

		List<ExpectedResult> expectedResults = ExpectedResultsLoader.getResultsByParty(party);
		List<PredictedResult> evaluationResults = new ArrayList<PredictedResult>();

		for (ExpectedResult expRes : expectedResults) {
			int question = expRes.getQuestion();
			PredictedResult predRes = analyzer.returningHitScoreToCalculateToWeightScore(party, question, windowSize);
			evaluationResults.add(predRes);
		}

		return evaluationResults;
	}

	/***
	 * Evaluation all questions and all parties.
	 * 
	 * @param windowSize
	 * @return
	 */
	public List<PredictedResult> evaluateAll(int windowSize) {

		List<ExpectedResult> expectedResults = ExpectedResultsLoader.getAllResults();
		List<PredictedResult> evaluationResults = new ArrayList<PredictedResult>();

		for (ExpectedResult expRes : expectedResults) {
			int question = expRes.getQuestion();
			Party party = Party.fromInteger(expRes.getParty());
			PredictedResult predRes = analyzer.returningHitScoreToCalculateToWeightScore(party, question, windowSize);
			evaluationResults.add(predRes);
		}

		return evaluationResults;
	}

}
