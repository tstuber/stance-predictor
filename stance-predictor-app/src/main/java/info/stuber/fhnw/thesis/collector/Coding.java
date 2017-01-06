package info.stuber.fhnw.thesis.collector;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Coding implements Serializable {

	private static final long serialVersionUID = -8278147079109824018L;
	private String content;
	private Exception exception;

	private List<Integer> party;
	private List<Integer> question;
	private String crawledUrlAsString;
	private URL sourceUrl;

	public Coding(int party, int question, URL sourceUrl) {

		if (this.party == null)
			this.party = new ArrayList<Integer>();

		if (this.question == null)
			this.question = new ArrayList<>();

		this.party.add(party);
		this.question.add(question);
		this.sourceUrl = sourceUrl;
		this.exception = null;
	}

	public void addParty(int party) {
		if (!this.party.contains(party))
			this.party.add(party);
	}
	
	// only used for lookup reason within serialized files. 
	public void setUsedUrl(String usedUrl) {
		this.crawledUrlAsString = usedUrl;
	}

	public void addQuestion(int questionId) {
		if (!this.question.contains(questionId))
			this.question.add(questionId);
	}

	public boolean containsParty(int party) {
		return this.party.contains(party);
	}

	public boolean containsQuestion(int questionId) {
		return this.question.contains(questionId);
	}

	public String getContent() {
		return this.content;
	}

	public Exception getException() {
		return exception;
	}

	public List<Integer> getParty() {
		return this.party;
	}

	public List<Integer> getQuestion() {
		return this.question;
	}

	public URL getSourceUrl() {
		return this.sourceUrl;
	}

	public String printDebug() {
		StringBuilder sb = new StringBuilder();
		sb.append("Question: ");
		for (int question : this.question) {
			sb.append(question + ",");
		}

		sb.append(" Party: ");
		for (int party : this.party) {
			sb.append(party + ",");
		}

		sb.append(" URL: " + this.sourceUrl);

		System.out.println(sb.toString());
		return sb.toString();
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}
