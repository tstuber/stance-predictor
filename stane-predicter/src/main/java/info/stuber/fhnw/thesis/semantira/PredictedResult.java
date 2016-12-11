package info.stuber.fhnw.thesis.semantira;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.stuber.fhnw.thesis.utils.ExpectedResult;
import info.stuber.fhnw.thesis.utils.ExpectedResultsLoader;
import info.stuber.fhnw.thesis.utils.Party;

public class PredictedResult {

	private List<PredictedResultItem> resultItemList;
	private Party party;
	private int questionId;
	private int expectedAnswer;

	public PredictedResult(Party party, int questionId) {
		this.resultItemList = new ArrayList<PredictedResultItem>();
		this.party = party;
		this.questionId = questionId;
		this.expectedAnswer = ExpectedResultsLoader.getSingleResult(party, questionId).getAnswer();
	}

	public void addItem(PredictedResultItem resultItem) {
		this.resultItemList.add(resultItem);
	}

	public Party getParty() {
		return this.party;
	}

	public int getQuestionId() {
		return this.questionId;
	}

	public int size() {
		return this.resultItemList.size();
	}

	public float getMax() {
		float[] score = getScoreArray();
		
		if(score.length == 0)
			return 0f;
		
		return score[score.length - 1];
	}

	public float getMin() {
		float[] score = getScoreArray();
		
		if(score.length == 0)
			return 0f;
		
		return getScoreArray()[0];
		
	}

	public float getMean() {
		float[] score = getScoreArray();
		
		if(score.length == 0)
			return 0f;
		
		float sum = 0;
		for (int i = 0; i < score.length; i++) {
			sum += score[i];
		}
		return sum / score.length;
	}

	public float getWeightedMean() {
		float totalWeight = 0f;
		float accumulatedMean = 0f;

		for (PredictedResultItem item : resultItemList) {

			float sentiScore = item.getSentimentScore();
			float hitScore = item.getHitScore();

			// Missing HitScore. Weighting can't be completed. Abort with max
			// Values.
			if (hitScore == Float.MAX_VALUE) {
				return 9999f;
			}

			totalWeight += hitScore;
			accumulatedMean += hitScore * sentiScore;
		}

		return accumulatedMean / totalWeight;
	}

	public float getMedian() {
		float[] score = getScoreArray();
		if(score.length == 0)
			return 0f;
		
		int middle = score.length / 2;
		if (score.length % 2 == 1) {
			return score[middle];
		} else {
			return (score[middle - 1] + score[middle]) / 2.0f;
		}
	}

	private float[] getScoreArray() {
		float[] result = new float[this.resultItemList.size()];
		for (int i = 0; i < this.resultItemList.size(); i++) {
			result[i] = this.resultItemList.get(i).getSentimentScore();
		}
		Arrays.sort(result);
		return result;
	}

	/**
	 * OFFICIAL METRIC TO COMPARE THE ANSWER!!!!!!! Includes the required
	 * mapping
	 ***/
	@Deprecated
	public int getAnswer() {
		return getAnswer(this.getMax());
	}

	public int getAnswer(float sentiScore) {

		int result = 6;

		// Taken default range from Semantria:
		// -0.05 to +0.22

		// neg -- medium -- pos-medium -- pos
		// float neg = -0.75f;
		// float neg_mid = -0.25f;
		// float pos_mid = 0.25f;
		// float pos = 0.75f;

		float neg = -0.75f;
		float neg_mid = -0.05f;
		float pos_mid = 0.12f;
		float pos = 0.75f;

		if (sentiScore < neg)
			result = 5;
		else if (sentiScore >= neg && sentiScore < neg_mid)
			result = 4;
		else if (sentiScore >= neg_mid && sentiScore < pos_mid)
			result = 3;
		else if (sentiScore >= pos_mid && sentiScore < pos)
			result = 2;
		else if (sentiScore >= pos)
			result = 1;

		return result;
	}
	
	public boolean isSuccess(int predictedAnswer) {
		boolean result = false;
		
		if((expectedAnswer == 3 || expectedAnswer == 6)  && predictedAnswer == 3)
			result = true;
		else if((expectedAnswer == 1 || expectedAnswer == 2) && (predictedAnswer == 1 || predictedAnswer == 2))
			result = true;
		else if((expectedAnswer == 4 || expectedAnswer == 5) && (predictedAnswer == 4 || predictedAnswer == 5))
			result = true;
		
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// Display: Min, Max, Mean, WeightedMean, Median
		sb.append("Debug" + "\t");
		sb.append(this.party + "\t");
		sb.append(this.questionId + "\t");
		sb.append(this.expectedAnswer + "\t");
		sb.append(String.format("%7.4f\t\t", this.getMin()));
		sb.append(String.format("%7.4f\t\t", this.getMax()));
		sb.append(String.format("%7.4f\t\t", this.getMean()));
		sb.append(String.format("%7.4f\t\t", this.getWeightedMean()));
		sb.append(String.format("%7.4f\n", this.getMedian()));

		int answerMin = this.getAnswer(this.getMin());
		int answerMax = this.getAnswer(this.getMax());
		int answerMean = this.getAnswer(this.getMean());
		int answerWeightedMean = this.getAnswer(this.getWeightedMean());
		int answerMedian = this.getAnswer(this.getMedian());
		
		// Display: Min, Max, Mean, WeightedMean, Median
		sb.append("Result" + "\t");
		sb.append(this.party + "\t");
		sb.append(this.questionId + "\t");
		sb.append(this.expectedAnswer + "\t");
		sb.append(answerMin + "\t");
		sb.append(this.isSuccess(answerMin) + "\t");
		sb.append(answerMax + "\t");
		sb.append(this.isSuccess(answerMax) + "\t");
		sb.append(answerMean + "\t");
		sb.append(this.isSuccess(answerMean) + "\t");
		sb.append(answerWeightedMean + "\t");
		sb.append(this.isSuccess(answerWeightedMean) + "\t");
		
		sb.append(answerMedian + "\t");
		sb.append(this.isSuccess(answerMedian) + "\t");

		return sb.toString();
	}
}
