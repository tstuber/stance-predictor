package info.stuber.fhnw.thesis.collector;

import java.util.ArrayList;

public class CrawableDocument {

	private String url;
	private String filename;
	private String content;
	private int id;
	private ArrayList<Exception> ex = null;
	private static String newline = System.getProperty("line.separator");
	
	public CrawableDocument(int id, String url) {
		this.id = id; 
		this.url = url;
		
		this.ex = new ArrayList<Exception>();
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	
	public String getUrl()
	{
		return this.url;
	}
	
	public void setUrl(String url)
	{
		this.url = url; 
	}
	
	public String getFilename()
	{
		return "document_" + id + ".txt";
	}
	
	public ArrayList getException()
	{
		return ex;
	}
	
	public void addException(Exception ex){
		this.ex.add(ex);
	}
	
	public String getContent()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.url);
		sb.append(newline);
		
		if(this.ex.size() > 0)
		{
			sb.append("Exceptions: " + this.ex.size());
			sb.append(newline);
			
			for(Exception ex : this.ex) {
				sb.append(ex.getMessage());
				sb.append(newline);
			}
		}
		
		sb.append(this.content);
			
		return sb.toString();
	}
	
	public boolean hasError()
	{
		return this.ex.size() > 0 ? true : false;
	}
	
	public String getError()
	{
		StringBuilder sb = new StringBuilder();
		
		if(this.ex.size() > 0)
		{
			sb.append("*** ID: " + this.id + " ***");
			sb.append(newline);
			sb.append("URL: " + this.url);
			sb.append(newline);
			
			sb.append("Exceptions: " + this.ex.size());
			sb.append(newline);
			
			for(Exception ex : this.ex) {
				sb.append(ex.getMessage());
				sb.append(newline);
			}
			sb.append(newline);
		}
		
		return sb.toString();
	
	}
	
	public void setContent(String content) {
		
		this.content = content;
	}
	
}
