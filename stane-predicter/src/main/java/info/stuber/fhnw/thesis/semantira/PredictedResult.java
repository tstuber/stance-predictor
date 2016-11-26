package info.stuber.fhnw.thesis.semantira;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.stuber.fhnw.thesis.utils.Party;

public class PredictedResult {

	private List<PredictedResultItem> resultItemList;
	private Party party;
	private int questionId;

	public PredictedResult(Party party, int questionId) {
		this.resultItemList = new ArrayList<PredictedResultItem>();
		this.party = party;
		this.questionId = questionId;
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
		return score[score.length - 1];

	}

	public float getMin() {
		return getScoreArray()[0];
	}

	public float getMean() {
		float[] score = getScoreArray();
		float sum = 0;
		for (int i = 0; i < score.length; i++) {
			sum += score[i];
		}
		return sum / score.length;
	}

	public float getMedian() {
		float[] score = getScoreArray();
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
	public int getAnswer() {
		float m = this.getMean();
		int result = 6;

		if (m < -0.75)
			result = 5;
		else if (m >= -0.75 && m < -0.25)
			result = 4;
		else if (m >= -0.25 && m < 0.25)
			result = 3;
		else if (m >= 0.25 && m < 0.75)
			result = 2;
		else if (m >= 0.75)
			result = 1;
		
		return result;
	}
}
