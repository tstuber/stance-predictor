package info.stuber.fhnw.thesis.semantira;

import java.io.Serializable;

public class PredictedResultItem implements Serializable {

	private int party;
	private int question;
	private float hitScore = Float.MAX_VALUE;
	private float sentimentScore;
	private String sentimentPolarity;
	private String text;
	private String source;
	private String query;

	public PredictedResultItem(int party, int question, float sentimentScore, String sentimentPolarity) {
		this.party = party;
		this.question = question;
		this.sentimentScore = sentimentScore;
		this.sentimentPolarity = sentimentPolarity;
	}
	
	public PredictedResultItem(int party, int question, float hitScore, float sentimentScore, String sentimentPolarity, String text) {
		this.party = party;
		this.question = question;
		this.hitScore = hitScore;
		this.sentimentScore = sentimentScore;
		this.sentimentPolarity = sentimentPolarity;
		this.text = text;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public String getSource() {
		return this.source;
	}

	public float getHitScore() {
		return this.hitScore;
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
	
	public String getText() {
		return text;
	}
}
