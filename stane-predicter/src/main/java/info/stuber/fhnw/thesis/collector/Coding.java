package info.stuber.fhnw.thesis.collector;

import java.net.URL;

public class Coding {

	private int party;
	private int question;
	private String source;
	
	private String mimeType;
	private String encoding;
	private URL targetUrl;
	private int httpStatus;
	
	public Coding(int party, int question, String source) {
		this.party = party;
		this.question = question;
		this.source = source;
		this.mimeType = "";
		this.encoding = "";
		this.targetUrl = null;
		this.httpStatus = 0;
	}
	
	public int getParty()
	{
		return this.party;
	}
	
	public int getQuestion() {
		return this.question;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public String getMimeType(){
		return this.mimeType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		if(encoding.contains("charset="))
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
	
	
	public String toString() {
		return "[Code: " + this.getHttpStatus() + "] TargetUrl: " + this.getTargetUrl() + " URL: " + this.source;
	}
}
