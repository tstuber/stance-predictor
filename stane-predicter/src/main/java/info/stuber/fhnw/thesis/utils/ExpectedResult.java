package info.stuber.fhnw.thesis.utils;

public class ExpectedResult {

	private Party party;
	private int question;
	private float median; //Round 2
	private float agreement; //Round 2
	
	public ExpectedResult(int party, int question, float median, float agreement){
		
		this.party = Party.fromInteger(party);
		this.question = question;
		this.median = median;
		this.agreement = agreement;
	}
	
	public int getParty() {
		return party.getId();
	}

	public int getQuestion() {
		return question;
	}

	public float getMedian() {
		return median;
	}

	public float getAgreement() {
		return agreement;
	}

	public String getSentiment() {
		
		String sentiment = "neutral";
		
		if(median > 3.1 && median < 5.9)
			sentiment = "disagreement";
		if(median < 2.9)
			sentiment = "agreement";
		
		return sentiment;
		
	}
}
