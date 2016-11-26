package info.stuber.fhnw.thesis.semantira;

public class EvalResult {

	int party = 0;
	int question = 0;
	int expectedAnswer = 0;
	int predictedAnswer = 0;

	public EvalResult(int party, int question, int expectedAnswer, int predictedAnswer) {
		this.party = party;
		this.question = question;
		this.expectedAnswer = expectedAnswer;
		this.predictedAnswer = predictedAnswer;
	}

	public String toString() {

		return party + "\t" + question + "\t\t" + expectedAnswer + "\t" + predictedAnswer;
	}
	
	public boolean isSuccess() {
		boolean result = false;
		
		if((expectedAnswer == 3 || expectedAnswer == 6)  && predictedAnswer == 3)
			result = true;
		else if((expectedAnswer == 1 || expectedAnswer == 2) && (predictedAnswer == 1 || predictedAnswer == 2))
			result = true;
		else if((expectedAnswer == 4 || expectedAnswer == 5) && (predictedAnswer == 4 || predictedAnswer == 5))
			result = true;
		
		return result;
	}
}
