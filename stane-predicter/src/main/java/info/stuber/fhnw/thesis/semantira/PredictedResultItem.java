package info.stuber.fhnw.thesis.semantira;

public class PredictedResultItem {

	private int party;
	private int question;
	private float hitScore = Float.MAX_VALUE;
	private float sentimentScore;
	private String sentimentPolarity;
	private String text;
	private String source;

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
