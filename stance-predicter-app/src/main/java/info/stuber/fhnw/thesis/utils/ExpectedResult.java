package info.stuber.fhnw.thesis.utils;

public class ExpectedResult {

	private Party party;
	private int question;
	private int answer; // Round 2
	private float agreement; // Round 2

	public ExpectedResult(int party, int question, int answer, float agreement) {

		this.party = Party.fromInteger(party);
		this.question = question;
		this.answer = answer;
		this.agreement = agreement;
	}

	public int getParty() {
		return party.getId();
	}

	public int getQuestion() {
		return question;
	}

	public int getAnswer() {
		return answer;
	}

	public float getAgreement() {
		return agreement;
	}

	public String getSentiment() {

		String sentiment = "neutral";

		if (answer == 4 || answer == 5)
			sentiment = "disagreement";
		if (answer == 1 || answer == 2)
			sentiment = "agreement";

		return sentiment;

	}

	public String getAnswerAsText() {
		StringBuilder sb = new StringBuilder();
		switch (this.answer) {
		case 1:
			sb.append("Total Agreement");
			break;
		case 2:
			sb.append("Agreement");
			break;
		case 3:
			sb.append("Neutral");
			break;
		case 4:
			sb.append("Disagreement");
			break;
		case 5:
			sb.append("Total Disagreement");
			break;
		case 6:
			sb.append("No opinion");
			break;
		}
		return sb.toString();
	}
}
