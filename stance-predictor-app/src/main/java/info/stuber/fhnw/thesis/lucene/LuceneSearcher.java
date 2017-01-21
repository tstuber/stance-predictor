package info.stuber.fhnw.thesis.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;
import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.Question;

public class LuceneSearcher {

	private static final String ANALYZER = GetConfigPropertyValues.getProperty("analyzer");

	public LuceneSearcher() {

	}

	public static void main(String[] args) throws IOException, ParseException {

		LuceneSearcher searcher = new LuceneSearcher();
		searcher.retrieveTopDocs(Party.CON, 14, 2);
	}
	
	@Test
	public void testAnalyzer() throws ParseException {
		Analyzer englishAnalyzer = new EnglishAnalyzer();
		Analyzer standardAnalyzer = new StandardAnalyzer();
		
		// String query = "Government spending should be cut further in order to balance the budget.";
		String query = "University tuition fees should be scrapped.";
		
		Query englishQuery = new QueryParser(LuceneConstants.CONTENTS, englishAnalyzer).parse(query);
		Query standardQuery = new QueryParser(LuceneConstants.CONTENTS, standardAnalyzer).parse(query);
		
		System.out.println(query);
		System.out.println(englishQuery.toString());
		System.out.println(standardQuery.toString());
		
	}

	public List<SearchResult> retrieveTopDocs(Party party, int questionId, int windowSize) {
		List<SearchResult> searchResults = new ArrayList<SearchResult>();

		String indexPath = getPathOfIndex(windowSize);
		Analyzer analyzer = null;

		try {
			if (ANALYZER.startsWith("E")) {
				analyzer = new EnglishAnalyzer();
			} else {
				analyzer = new StandardAnalyzer();
			}
			Directory index = FSDirectory.open(Paths.get(indexPath));

			String blockedArticleUk = "http://www.bbc.co.uk/news/uk-politics-29642613";
			String blockedArticleCom = "http://www.bbc.com/news/uk-politics-29642613";

			// 2. query
			
			// Issue Statement query
			String issueStmt = Question.getQuestionById(questionId);
			QueryParser issueStatementQP = new QueryParser(LuceneConstants.CONTENTS, analyzer);
			Query issueStmtQuery = issueStatementQP.parse(issueStmt);
			
			// Party query
			String partyId = Integer.toString(party.getId());
			QueryParser partyQP = new QueryParser(LuceneConstants.PARTY, analyzer);
			Query partyQuery = partyQP.parse(partyId);
			
			// Source query
			TermQuery queryBlacklistUk = new TermQuery(new Term("source", blockedArticleUk));
			TermQuery queryBlacklistCom = new TermQuery(new Term("source", blockedArticleCom));
			
			// Final query
			BooleanQuery.Builder finalQuery = new BooleanQuery.Builder();
			finalQuery.add(issueStmtQuery, Occur.SHOULD);
			finalQuery.add(partyQuery, Occur.MUST);
			finalQuery.add(queryBlacklistUk, Occur.MUST_NOT); // Fixes the problem of the all mixed stance passage.
			finalQuery.add(queryBlacklistCom, Occur.MUST_NOT); // Fixes the problem of the all mixed stance passage.
			
			String queryStr = finalQuery.build().toString();
			
			System.out.println(queryStr);

			// 3. search
			int hitsPerPage = LuceneConstants.MAX_SEARCH;
			IndexReader reader = DirectoryReader.open(index);

			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(finalQuery.build(), hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			System.out.println("maxDocs: " + reader.maxDoc());
			System.out.println("numDocs: " + reader.numDocs());

			// 4. display results
			System.out.println("Found " + hits.length + " hits.\n");
			System.out.printf("Top Results for Party:%s and Question:%d\n", party, questionId);

			int docCount = 0;
			for (int i = 0; i < hits.length; ++i) {

				// Aborts if already 10 documents are collected.
				if (docCount >= 10) {
					break;
				}

				int docId = hits[i].doc;
				Document d = searcher.doc(docId);

				float hitScore = hits[i].score;
				String passage = d.get(LuceneConstants.CONTENTS);
				String source = d.get(LuceneConstants.SOURCE);

				SearchResult res = new SearchResult(passage, hitScore, docCount, source, queryStr);

				// Only adds search result if not a duplicate.
				if (!searchResults.contains(res)) {
					searchResults.add(res);
					docCount++;
					// System.out.println((docCount + 1) + ". (" + hitScore + ")
					// Party:" + party + " Question:" + questionId
					// + " URL:" + source + "\n" + passage);
					System.out.printf("%2d (%7.4f) %s (%s)\n", docCount, hitScore, passage, source);
				}
			}

			// reader can only be closed when there
			// is no need to access the documents any more.
			reader.close();

		} catch (IOException ex) {
			System.out.println(ex.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return searchResults;
	}

	public String getPathOfIndex(int windowSize) {

		if (windowSize > 0 && windowSize <= 4)
			return GetConfigPropertyValues.getProperty("path_index_ws" + windowSize);
		else
			return null;
	}
}
