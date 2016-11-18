package info.stuber.fhnw.thesis.collector;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Coding implements Serializable {

	private List<Integer> party;
	private List<Integer> question;
	private String source;

	private String mimeType;
	private String encoding;
	private URL targetUrl;
	private int httpStatus;
	private String content;
	private static String newline = System.getProperty("line.separator");
	// private static final long serialVersionUID = -55857686305273843L;

	public Coding(int party, int question, String source) {

		if (this.party == null)
			this.party = new ArrayList<Integer>();

		if (this.question == null)
			this.question = new ArrayList<>();

		this.party.add(party);
		this.question.add(question);
		this.source = source;
		this.mimeType = "";
		this.encoding = "";
		this.targetUrl = null;
		this.httpStatus = 0;
	}

	public List<Integer> getParty() {
		return this.party;
	}

	public void addParty(int party) {
		if (!this.party.contains(party))
			this.party.add(party);
	}

	public List<Integer> getQuestion() {
		return this.question;
	}

	public boolean containsQuestion(int questionId) {
		return this.question.contains(questionId);
	}

	public boolean containsParty(int party) {
		return this.party.contains(party);
	}

	public void addQuestion(int questionId) {
		if (!this.question.contains(questionId))
			this.question.add(questionId);
	}

	public String getSource() {
		return this.source;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		if (encoding.contains("charset="))
			this.encoding = encoding.split("=")[1];
		else
			this.encoding = encoding;
	}

	public URL getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(URL targetUrl) {
		this.targetUrl = targetUrl;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {

		return this.content;
	}

	public String toString() {
		return "[Code: " + this.getHttpStatus() + "] TargetUrl: " + this.getTargetUrl() + " URL: " + this.source;
	}

	public void printDebug() {
		StringBuilder sb = new StringBuilder();
		sb.append("Question: ");
		for (int question : this.question) {
			sb.append(question + ",");
		}

		sb.append(" Party: ");
		for (int party : this.party) {
			sb.append(party + ",");
		}

		sb.append(" URL: " + this.source);

		System.out.println(sb.toString());
	}
}
