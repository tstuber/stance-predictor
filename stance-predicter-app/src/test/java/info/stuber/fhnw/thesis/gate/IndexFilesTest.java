package info.stuber.fhnw.thesis.gate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.lucene.LuceneConstants;
import info.stuber.fhnw.thesis.utils.Question;

public class IndexFilesTest {

	private IndexFiles indexer = null;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void compareIndexes() {
		
		Searcher searcher_ws1 = new Searcher(new File("C:/Temp/webcrawler_index_ws1"));
		SearchResults res_ws1 = searcher_ws1.search("UKIP foreign policy", 4);
		
		Searcher searcher_ws2 = new Searcher(new File("C:/Temp/webcrawler_index_ws2"));
		SearchResults res_ws2 = searcher_ws2.search("UKIP foreign policy", 4);
		
		Searcher searcher_ws3 = new Searcher(new File("C:/Temp/webcrawler_index_ws3"));
		SearchResults res_ws3 = searcher_ws3.search("UKIP foreign policy", 4);
		
		Searcher searcher_ws4 = new Searcher(new File("C:/Temp/webcrawler_index_ws4"));
		SearchResults res_ws4 = searcher_ws4.search("UKIP foreign policy", 4);
		
		System.out.println(res_ws1.getNumDocs());
		System.out.println(res_ws2.getNumDocs());
		System.out.println(res_ws3.getNumDocs());
		System.out.println(res_ws4.getNumDocs());
	}
	
	@Test
	public void CreateNewIndex() throws IOException {

		// Arrange.
		File tempFolder = testFolder.newFolder("luceneIndex");
		System.out.println(tempFolder.toString());
		this.indexer = new IndexFiles(tempFolder.toString());

		List<String> sentenceList = new ArrayList<String>();
		sentenceList.add("Satz Number eins.");
		sentenceList.add("Second sentence here.");
		sentenceList.add("Third and last one - Sentence Test.");

		Coding coding = new Coding(1, 1, new URL("http://www.google.ch"));

		// Act.
		this.indexer.indexSentences(sentenceList, coding);

		// Assert.
		Searcher searcher = new Searcher(tempFolder);
		Assert.assertTrue(searcher.search("",1).getNumDocs() == 3);
	}

	@Test
	public void CreateNewIndexAndAppend() throws IOException {

		// Arrange.
		File tempFolder = testFolder.newFolder("luceneIndex");
		System.out.println(tempFolder.toString());
		this.indexer = new IndexFiles(tempFolder.toString());

		int party = 1; 
		int question = 1;
		
		List<String> sentenceList = new ArrayList<String>();
		sentenceList.add("This is the first sentence.");
		sentenceList.add("Second sentence here.");
		sentenceList.add("Third and last one and serves as the only test result.");

		Coding coding = new Coding(party, question, new URL("http://www.google.ch"));

		// Act.
		this.indexer.indexSentences(sentenceList, coding);
		this.indexer.indexSentences(sentenceList, coding);

		// Assert.
		Searcher searcher = new Searcher(tempFolder);
		SearchResults res = searcher.search("Test",1);
		Assert.assertTrue(res.getNumDocs() == 6);
		Assert.assertTrue(res.getHits().length == 6);
	}
	
	@Test
	public void CreateNewIndexWithoutHit() throws IOException {

		// Arrange.
		File tempFolder = testFolder.newFolder("luceneIndex");
		System.out.println(tempFolder.toString());
		this.indexer = new IndexFiles(tempFolder.toString());

		List<String> sentenceList = new ArrayList<String>();
		sentenceList.add("Satz Number eins.");
		sentenceList.add("Second sentence here.");
		sentenceList.add("Third and last one - Sentence Test.");

		Coding coding = new Coding(1, 1, new URL("http://www.google.ch"));

		// Act.
		this.indexer.indexSentences(sentenceList, coding);

		// Assert.
		Searcher searcher = new Searcher(tempFolder);
		SearchResults res = searcher.search("Test",2);
		Assert.assertTrue(res.getNumDocs() == 3);
		Assert.assertTrue(res.getHits().length == 0);
	}
	
	@Test
	public void CreateNewIndexTwoParties() throws IOException {

		// Arrange.
		File tempFolder = testFolder.newFolder("luceneIndex");
		System.out.println(tempFolder.toString());
		this.indexer = new IndexFiles(tempFolder.toString());

		List<String> sentenceList = new ArrayList<String>();
		sentenceList.add("Satz Number eins.");
		sentenceList.add("Second sentence here.");
		sentenceList.add("Third and last one - Sentence Test.");

		Coding coding = new Coding(1, 1, new URL("http://www.google.ch"));
		coding.addParty(3);

		// Act.
		this.indexer.indexSentences(sentenceList, coding);

		// Assert.
		Searcher searcher = new Searcher(tempFolder);
		SearchResults res = searcher.search("Test",3);
		Assert.assertTrue(res.getNumDocs() == 3);
		Assert.assertTrue(res.getHits().length == 3);
	}

	//// -------------------------- Searcher Class for testing ------------ //
	private class SearchResults {

		private int numDocs;
		private int maxDocs;
		private ScoreDoc[] hits;
		public int getNumDocs() {
			return numDocs;
		}
		public void setNumDocs(int numDocs) {
			this.numDocs = numDocs;
		}
		public int getMaxDocs() {
			return maxDocs;
		}
		public void setMaxDocs(int maxDocs) {
			this.maxDocs = maxDocs;
		}
		public ScoreDoc[] getHits() {
			return hits;
		}
		public void setHits(ScoreDoc[] hits) {
			this.hits = hits;
		}
	}

	private class Searcher {
		Directory index = null;
		IndexReader reader = null;
		Analyzer analyzer = null;
		File tempFolder = null;

		public Searcher(File tempFolder) {
			this.tempFolder = tempFolder;
		}

		public SearchResults search(String issueStatement, int party) {
			
			SearchResults results = new SearchResults();
			try {
				index = FSDirectory.open(Paths.get(tempFolder.toURI()));
				reader = DirectoryReader.open(index);
				analyzer = new StandardAnalyzer();
				
				String special = issueStatement + " +party:" + party;
				Query q = new QueryParser(LuceneConstants.CONTENTS, analyzer).parse(special);

				System.out.println(q.toString());

				// 3. search
				int hitsPerPage = LuceneConstants.MAX_SEARCH;
				IndexReader reader = DirectoryReader.open(index);
				IndexSearcher searcher = new IndexSearcher(reader);
				TopDocs docs = searcher.search(q, hitsPerPage);
				ScoreDoc[] hits = docs.scoreDocs;
								
				int maxDocs = reader.maxDoc();
				int numDocs = reader.numDocs();

				results.setHits(hits);
				results.setMaxDocs(maxDocs);
				results.setNumDocs(numDocs);
				
				reader.close();
				
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
			
			return results;
		}
	}
}
