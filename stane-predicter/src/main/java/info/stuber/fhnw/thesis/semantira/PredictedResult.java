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
	
	public float getWeightedMean() {
		float totalWeight = 0f;
		float accumulatedMean = 0f;
		
		for(PredictedResultItem item : resultItemList) {
			
			float sentiScore = item.getSentimentScore();
			float hitScore = item.getHitScore();
			
			// Missing HitScore. Weighting can't be completed. Abort with max Values.
			if(hitScore == Float.MAX_VALUE) {
				return 9999f;
			}
				
			totalWeight += hitScore;
			accumulatedMean += hitScore * sentiScore;
		}
		
		return accumulatedMean/totalWeight;
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
		
		return getAnswer(this.getMean());
	}
	
	public int getAnswer(float sentiScore) {
		
		int result = 6;
		
		// Taken default range from Semantria: 
		// -0.05 to +0.22
		
		// neg -- medium -- pos-medium -- pos
		float neg = -0.75f;
		float neg_mid = -0.25f;
		float pos_mid = 0.25f;
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// Display: Min, Max, Mean, WeightedMean, Median
		sb.append(this.getMin() + "\t");
		sb.append(this.getMax() + "\t");
		sb.append(this.getMean() + "\t");
		sb.append(this.getWeightedMean() + "\t");
		sb.append(this.getMedian() + "\n");
		
		// Display: Min, Max, Mean, WeightedMean, Median
		sb.append(this.getAnswer(this.getMin()) + "\t");
		sb.append(this.getAnswer(this.getMax()) + "\t");
		sb.append(this.getAnswer(this.getMean()) + "\t");
		sb.append(this.getAnswer(this.getWeightedMean()) + "\t");
		sb.append(this.getAnswer(this.getMedian()) + "\n");
		
		return sb.toString();
	}
}
