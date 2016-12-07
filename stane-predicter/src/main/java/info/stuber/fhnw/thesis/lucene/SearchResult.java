package info.stuber.fhnw.thesis.lucene;

public class SearchResult {

	private String passage;
	private float hitScore;
	private int ranking;
	private String source;
	
	public SearchResult(String passage, float hitScore, int ranking, String source) {
		this.passage = passage;
		this.hitScore = hitScore;
		this.ranking = ranking;
		this.source = source;
	}
	
	public String getPassage() {
		return this.passage;
	}
	
	public float getHitScore() {
		return this.hitScore;
	}
	
	public int getRanking() {
		return this.ranking;
	}
	
	public String source() {
		return this.source;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof SearchResult)) {
			return false;
		}
		
		SearchResult that = (SearchResult) other;
		
		return this.passage.equals(that.passage);
	}
	
	@Override
	public int hashCode() {
		return (this.source + this.passage).hashCode();
	}
}
