package info.stuber.fhnw.thesis.semantira;

public class PredictedResultItem {

	private int party;
	private int question;
	private float sentimentScore;
	private String sentimentPolarity;

	public PredictedResultItem(int party, int question, float sentimentScore, String sentimentPolarity) {
		this.party = party;
		this.question = question;
		this.sentimentScore = sentimentScore;
		this.sentimentPolarity = sentimentPolarity;
	}

	public int getParty() {
		return party;
	}

	public int getQuestion() {
		return question;
	}

	public float getSentimentScore() {
		return sentimentScore;
	}

	public String getSentimentPolarity() {
		return sentimentPolarity;
	}

}
